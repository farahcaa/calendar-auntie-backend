package org.jaga.model.repositories;

import java.util.UUID;
import org.jaga.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
