package org.jaga.model.repositories;

import java.util.UUID;
import org.jaga.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Page<Product> findAllByActiveTrue(Pageable pageable);
}
