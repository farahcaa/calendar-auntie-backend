package com.calendar_auntie.model.repositories;

import java.util.UUID;
import com.calendar_auntie.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
