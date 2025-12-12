package com.calendar_auntie.model.repositories;

import java.util.UUID;
import com.calendar_auntie.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
