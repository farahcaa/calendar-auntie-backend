package com.calendar_auntie.model.repositories;

import com.calendar_auntie.model.enums.OrderStatus;
import java.util.UUID;
import com.calendar_auntie.model.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
  Order findByStripePaymentIntentId(String paymentIntentId);

  Page<Order> findAllByStatusOrderByUpdatedAtDesc(OrderStatus status, Pageable pageable);

  Page<Order> findAllByOrderByUpdatedAtDesc( Pageable page);
}
