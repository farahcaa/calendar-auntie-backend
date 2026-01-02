package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.CheckoutOrderDTO;
import com.calendar_auntie.model.dtos.CreateOrderRequest;
import com.calendar_auntie.model.dtos.PaymentIntentResult;
import com.calendar_auntie.model.entities.Address;
import com.calendar_auntie.model.entities.Config;
import com.calendar_auntie.model.entities.Customer;
import com.calendar_auntie.model.entities.Order;
import com.calendar_auntie.model.entities.OrderItem;
import com.calendar_auntie.model.entities.Product;
import com.calendar_auntie.model.enums.AddressType;
import com.calendar_auntie.model.enums.OrderStatus;
import com.calendar_auntie.model.repositories.AddressRepository;
import com.calendar_auntie.model.repositories.ConfigRepository;
import com.calendar_auntie.model.repositories.CustomerRepository;
import com.calendar_auntie.model.repositories.OrderRepository;
import com.calendar_auntie.model.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CheckoutService {

  private final PaymentService paymentService;
  private final CustomerRepository customerRepository;
  private final OrderRepository orderRepository;
  private final AddressRepository addressRepository;
  private final ProductRepository productRepository;
  private final ConfigRepository configRepository;

  public CheckoutService(
    PaymentService paymentService,
    CustomerRepository customerRepository,
    OrderRepository orderRepository,
    AddressRepository addressRepository,
    ProductRepository productRepository,
    ConfigRepository configRepository
  ) {
    this.paymentService = paymentService;
    this.customerRepository = customerRepository;
    this.orderRepository = orderRepository;
    this.addressRepository = addressRepository;
    this.productRepository = productRepository;
    this.configRepository = configRepository;
  }

  public PaymentIntentResult createOrder(CreateOrderRequest createOrderRequest) {
    if (createOrderRequest == null) {
      throw new IllegalArgumentException("CreateOrderRequest must not be null");
    }

    // ---- Load config safely ----
    Config config = configRepository.findById("default")
      .orElseThrow(() -> new IllegalStateException("Default config not found"));

    if (createOrderRequest.checkoutAddressDTO() == null) {
      throw new IllegalArgumentException("Checkout address is required");
    }

    if (createOrderRequest.checkoutOrderDTO() == null
      || createOrderRequest.checkoutOrderDTO().isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one item");
    }

    var addressDTO = createOrderRequest.checkoutAddressDTO();

    if (addressDTO.email() == null || addressDTO.email().isBlank()) {
      throw new IllegalArgumentException("Email is required");
    }

    // ---- Find or create customer ----
    Customer customer = customerRepository.findByEmail(addressDTO.email());

    if (customer == null) {
      String fullName = addressDTO.fullName() != null ? addressDTO.fullName().trim() : "";
      String firstName = fullName;
      String lastName = "";

      if (!fullName.isEmpty()) {
        String[] parts = fullName.split("\\s+", 2); // split into at most 2 parts
        firstName = parts[0];
        if (parts.length > 1) {
          lastName = parts[1];
        }
      }

      customer = new Customer()
        .setEmail(addressDTO.email())
        .setFirstName(firstName)
        .setLastName(lastName);

      customer = customerRepository.save(customer);
    }

    // ---- Find or create shipping address ----
    List<Address> addresses =
      addressRepository.findByAddressTypeAndCustomer(AddressType.ShippingAddress, customer);

    Address address;
    if (addresses != null && !addresses.isEmpty()) {
      // Re-use existing shipping address (optionally update fields)
      address = addresses.get(0);
      address
        .setCity(addressDTO.city())
        .setLine1(addressDTO.address1())
        .setState(addressDTO.state())
        .setPostalCode(addressDTO.zip()).setCountryCode("US");
    } else {
      // Create new shipping address
      address = new Address()
        .setAddressType(AddressType.ShippingAddress)
        .setCustomer(customer)
        .setCity(addressDTO.city())
        .setLine1(addressDTO.address1())
        .setName("Shipping Address")
        .setState(addressDTO.state())
        .setPostalCode(addressDTO.zip()).setCountryCode("US");


    }

    // ---- Create order ----
    Order order = new Order();
    order.setCustomer(customer);
    order.setShippingAddress(address);
    order.setStatus(OrderStatus.PENDING); // or whatever enum/value you use
    order.setCreatedAt(Instant.now());    // assuming you have createdAt

    List<OrderItem> orderItems = new ArrayList<>();
    double subtotal = 0.0; // ideally use BigDecimal, but sticking to double for now

    for (CheckoutOrderDTO checkoutOrderDTO : createOrderRequest.checkoutOrderDTO()) {
      if (checkoutOrderDTO == null) {
        continue;
      }

      if (checkoutOrderDTO.quantity() <= 0) {
        // You can either skip or throw; throwing is usually better
        throw new IllegalArgumentException("Quantity must be > 0 for product " + checkoutOrderDTO.id());
      }

      Optional<Product> productOpt = productRepository.findById(checkoutOrderDTO.id());
      if (productOpt.isEmpty()) {
        throw new IllegalArgumentException("Product not found: " + checkoutOrderDTO.id());
      }

      Product product = productOpt.get();

      if( product.getInventoryQty() < checkoutOrderDTO.quantity()){
        return null;
      }

      if(product.getInventoryQty() == checkoutOrderDTO.quantity()){
      product.setActive(false);
      }

      product.setInventoryQty(product.getInventoryQty() - checkoutOrderDTO.quantity());
      double lineTotal = product.getPrice() * checkoutOrderDTO.quantity();
      subtotal += lineTotal;

      OrderItem orderItem = new OrderItem()
        .setOrder(order)
        .setProduct(product)
        .setSku(product.getSku())
        .setQuantity(checkoutOrderDTO.quantity())
        .setTitle(product.getTitle())
        .setUnitPrice(product.getPrice())
        .setTotalPrice(lineTotal);

      orderItems.add(orderItem);
      productRepository.save(product);
    }

    address = addressRepository.save(address);

    if (orderItems.isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one valid item");
    }

    // ---- Shipping / tax calculation ----
    // Right now you had: price * qty + shipping + tax in each line.
    // Usually: subtotal = sum(line); then add shipping & tax ONCE per order.

    double shipping = config.getShipping();
    double tax = config.getTax(); // if this is a flat amount; if it's a percentage, change logic

    double total = subtotal + shipping + tax;

    order.setSubtotalPrice(subtotal);
    order.setShippingPrice(shipping);
    order.setTaxPrice(tax);
    order.setTotalPrice(total);
    order.setBillingAddressId(address.getId());
    order.setItems(orderItems); // assuming mappedBy="order" with cascade
    long amountInCents = Math.round(total * 100);
PaymentIntentResult paymentIntentResult = paymentService.createPaymentIntent(amountInCents,"USD");

order.setStripePaymentIntentID(paymentIntentResult.paymentIntentId());

    // ---- Persist order (and items via cascade) ----
    order = orderRepository.save(order);

    return new PaymentIntentResult(paymentIntentResult.paymentIntentId(),
      paymentIntentResult.clientSecret(), order.getId());
  }
}
