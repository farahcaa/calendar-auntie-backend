package com.calendar_auntie.model.repositories;

import java.util.UUID;
import com.calendar_auntie.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Page<Product> findAllByActiveTrue(Pageable pageable);
}
