package com.calendar_auntie.model.repositories;

import com.calendar_auntie.model.entities.ProductMedia;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, UUID> {
}
