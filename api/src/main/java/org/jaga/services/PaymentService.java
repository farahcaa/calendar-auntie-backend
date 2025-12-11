package org.jaga.services;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.jaga.model.dtos.payment.AddressDTO;
import org.jaga.model.dtos.payment.CartItemDTO;
import org.jaga.model.dtos.payment.CreatePaymentRequest;
import org.jaga.model.dtos.payment.CustomerInfoDTO;
import org.jaga.model.entities.Address;
import org.jaga.model.entities.Customer;
import org.jaga.model.entities.Order;
import org.jaga.model.entities.OrderItem;
import org.jaga.model.entities.Product;
import org.jaga.model.enums.AddressType;
import org.jaga.model.enums.OrderStatus;
import org.jaga.model.repositories.AddressRepository;
import org.jaga.model.repositories.CustomerRepository;
import org.jaga.model.repositories.OrderItemRepository;
import org.jaga.model.repositories.OrderRepository;
import org.jaga.model.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
  private final Logger log = LoggerFactory.getLogger(PaymentService.class);

  private final CustomerRepository customerRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;
  private final AddressRepository addressRepository;
  private final long shippingAmount;
  private final long taxAmount;

  public PaymentService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository, OrderItemRepository orderItemRepository, AddressRepository addressRepository, @Value("{stripe.payments.shipping-amount}") Long shippingAmount, @Value("{stripe.payments.tax-amount}")  Long taxAmount) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.customerRepository = customerRepository;
    this.orderItemRepository = orderItemRepository;
    this.addressRepository = addressRepository;
    this.shippingAmount = shippingAmount;
    this.taxAmount = taxAmount;
  }

  public boolean validateCart(List<CartItemDTO> items) {
    // basic sanity check
    log.info("Validating cart");
    if (items == null || items.isEmpty()) {
      log.info("No cart items found");
      return false;
    }

    // If the same product appears multiple times, sum quantities
    Map<UUID, Integer> quantityByProduct = new HashMap<>();

    for (CartItemDTO item : items) {
      // null checks
      if (item == null || item.getProductId() == null) {
        log.error("Invalid cart");
        return false;
      }

      // quantity must be > 0
      if (item.getQuantity() <= 0) {
        log.error("Invalid quantity: " + item.getQuantity());
        return false;
      }

      quantityByProduct.merge(item.getProductId(), item.getQuantity(), Integer::sum);
    }

    // Now validate each product against DB
    for (Map.Entry<UUID, Integer> entry : quantityByProduct.entrySet()) {
      UUID productId = entry.getKey();
      int totalRequestedQty = entry.getValue();

      Optional<Product> productOpt = productRepository.findById(productId);
      if (productOpt.isEmpty()) {
        // product doesn't exist
        log.error("Product not found: " + productId);
        return false;
      }

      Product product = productOpt.get();

      // must be active
      if (!product.isActive()) {
        log.error("Product is not active: " + productId);
        return false;
      }

      // must have enough stock
      if (product.getInventoryQty() < totalRequestedQty) {
        log.error("Product has not enough inventory Qty: " + product.getInventoryQty());
        return false;
      }
    }

    return true;
  }

  public long calculatePrice(List<CartItemDTO> requests) throws Exception {
    long total = 0L;
    for(CartItemDTO item : requests) {
      Optional<Product> product = productRepository.findById(item.getProductId());
      if (product.isPresent()) {
        total += (long) (product.get().getInventoryQty() * product.get().getPrice());
      }
    }

    // compute tax (Simple Config for now)
    total += taxAmount + shippingAmount;

    return total;
  }

  public PaymentIntent createIntent(long price, String currency) throws Exception {

    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
      .setAmount(price)
      .setCurrency(currency)
      .setAutomaticPaymentMethods(
        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
          .setEnabled(true)
          .build()
      )
      .build();

    return PaymentIntent.create(params);
  }


  public void createEntities(CreatePaymentRequest request, long price) throws Exception {

    // find customer if not create new customer and set values
    Customer customer = customerRepository.findByEmail(request.getCustomer().getEmail());
    if (customer == null) {
       customer = new Customer();
       customer.setFirstName(request.getCustomer().getFirstName());
       customer.setLastName(request.getCustomer().getLastName());
       customer.setPhone(request.getCustomer().getPhone());
       customer.setEmail(request.getCustomer().getEmail());
    }

    //create new order add values and create order items and add them to the order
    Order order = new Order();

    List<OrderItem> orderItems = new ArrayList<>();
    for (CartItemDTO item : request.getItems()) {
      OrderItem orderItem = new OrderItem();
      orderItem.setQuantity(item.getQuantity());
      orderItem.setProductId(request.getItems().getFirst().getProductId());
      orderItems.add(orderItem);
      orderItemRepository.save(orderItem);
    }

    // save shipping address
    List<Address> addresses =
      addressRepository.findByAddressTypeAndCustomer(AddressType.ShippingAddress, customer);
    String requestedLine1 = request.getCustomer().getShippingAddress().getLine1();

    Address shippingAddress = null;

    if (addresses == null || addresses.isEmpty()) {
      // no addresses at all → create new one
      shippingAddress = createAndSaveNewAddressFromRequest(request.getCustomer().getShippingAddress(), customer);
    } else {
      // check if any existing address matches line1
      Optional<Address> match = addresses.stream()
        .filter(a -> requestedLine1.equalsIgnoreCase(a.getLine1()))
        .findFirst();

      if (match.isPresent()) {
        // reuse existing address with same line1
        shippingAddress = match.get();
      } else {
        // no match on line1 → create new one
        shippingAddress = createAndSaveNewAddressFromRequest(request.getCustomer().getShippingAddress(), customer);
      }
    }

    //save billings address
    List<Address> billingAddresses =
      addressRepository.findByAddressTypeAndCustomer(AddressType.BillingAddress, customer);
    String billingRequestedLine1 = request.getCustomer().getBillingAddress().getLine1();

    Address billingAddress = null;

    if (billingAddresses == null || billingAddresses.isEmpty()) {
      // no addresses at all → create new one
      billingAddress = createAndSaveNewAddressFromRequest(request.getCustomer().getBillingAddress(), customer);
    } else {
      // check if any existing address matches line1
      Optional<Address> match = billingAddresses.stream()
        .filter(a -> billingRequestedLine1.equalsIgnoreCase(a.getLine1()))
        .findFirst();

      if (match.isPresent()) {
        // reuse existing address with same line1
        billingAddress = match.get();
      } else {
        // no match on line1 → create new one
        billingAddress = createAndSaveNewAddressFromRequest(request.getCustomer().getBillingAddress(), customer);
      }
    }
    order.setCustomer(customer);
    order.setShippingAddressId(shippingAddress.getId());
    order.setItems(orderItems);
    order.setStatus(OrderStatus.PENDING);
    order.setBillingAddressId(billingAddress.getId());
    order.setTotalPrice(price);
    order.setTaxPrice(taxAmount);
    order.setShippingPrice(shippingAmount);
    order.setSubtotalPrice(price-taxAmount-shippingAmount);

    orderRepository.save(order);

    customer = customerRepository.save(customer);
  }

  private Address createAndSaveNewAddressFromRequest(AddressDTO request, Customer customer) throws Exception {
    Address address = new Address();
    address.setCustomer(customer);
    address.setAddressType(request.getAddressType());
    address.setLine1(request.getLine1());
    address.setLine2(request.getLine2());
    address.setCity(request.getCity());
    address.setState(request.getState());
    address.setPostalCode(request.getPostalCode());
    address.setCountryCode(request.getCountryCode());

    return addressRepository.save(address);
  }

}
