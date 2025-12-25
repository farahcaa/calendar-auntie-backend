package com.calendar_auntie.model.repositories;

import com.calendar_auntie.model.dtos.AdminProductDTO;
import java.util.UUID;
import com.calendar_auntie.model.entities.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Page<Product> findAllByIsActiveTrue(Pageable pageable);

  @NotNull Page<Product> findAll(@NotNull Pageable pageable);
}
