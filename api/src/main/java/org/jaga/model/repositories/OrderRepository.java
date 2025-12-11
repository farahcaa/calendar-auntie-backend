package org.jaga.model.repositories;

import java.util.UUID;
import org.jaga.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
