package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.AdminOrderDTO;
import com.calendar_auntie.model.dtos.AdminProductDTO;
import com.calendar_auntie.model.dtos.IndividualOrderDTO;
import com.calendar_auntie.model.dtos.OrderItemDTO;
import com.calendar_auntie.model.dtos.payment.AddressDTO;
import com.calendar_auntie.model.entities.Address;
import com.calendar_auntie.model.entities.Customer;
import com.calendar_auntie.model.entities.Order;
import com.calendar_auntie.model.entities.OrderItem;
import com.calendar_auntie.model.entities.Product;
import com.calendar_auntie.model.enums.AddressType;
import com.calendar_auntie.model.enums.OrderStatus;
import com.calendar_auntie.model.repositories.OrderItemRepository;
import com.calendar_auntie.model.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminOrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  public AdminOrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
    this.orderItemRepository = orderItemRepository;
    this.orderRepository = orderRepository;
  }

  public Page<AdminOrderDTO> getAdminOrders(Pageable pageable) {
    Page<Order> orders = orderRepository.findAllByOrderByUpdatedAtDesc(pageable);

    return orders.map(p-> new AdminOrderDTO(p.getId().toString(), p.getStatus(), p.getUpdatedAt()));
  }

  public Page<AdminOrderDTO> getAdminOrdersByStatus(Pageable pageable, OrderStatus status) {
    Page<Order> orders = orderRepository.findAllByStatusOrderByUpdatedAtDesc(OrderStatus.PAID, pageable);

    return orders.map(p-> new AdminOrderDTO(p.getId().toString(), p.getStatus(), p.getUpdatedAt()));
  }

  public IndividualOrderDTO getAdminOrderById(String orderId){
    Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));

    IndividualOrderDTO orderDTO;
    if(orderOptional.isEmpty()){
      return null;
    }
    Order order = orderOptional.get();
    Address address = order.getShippingAddress();

    Customer customer = order.getCustomer();

    List<OrderItem> orderItemList = orderItemRepository.findAllByOrder(order);

    List<OrderItemDTO> orderItemListDTO = new ArrayList<>();

    for(OrderItem orderItem : orderItemList){

      OrderItemDTO orderItem1 = new OrderItemDTO(orderItem.getId().toString(), orderItem.getTitle(), orderItem.getProduct().getMedia().getFirst().getUrl(), orderItem.getQuantity(), orderItem.getUnitPrice(), orderItem.getTotalPrice() );
      orderItemListDTO.add(orderItem1);
    }
    AddressDTO addressDTO = new AddressDTO().setAddressType(AddressType.ShippingAddress).setCity(address.getCity()).setCountryCode(
      address.getCountryCode()).setPostalCode(address.getPostalCode()).setLine1(address.getLine1()).setLine2(address.getLine2()).setState(address.getState());

    orderDTO = new IndividualOrderDTO(customer.getFirstName() + " "+ customer.getLastName(),customer.getEmail(),order.getTotalPrice(), order.getTaxPrice(),order.getShippingPrice(), addressDTO,orderItemListDTO);

    return orderDTO;
  }

  public void fulfilOrder(String orderId){
    Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));
    if(orderOptional.isPresent()){
      orderOptional.get().setStatus(OrderStatus.FULFILLED);
      orderRepository.save(orderOptional.get());
    }
  }
}
