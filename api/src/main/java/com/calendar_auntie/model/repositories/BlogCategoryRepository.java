package com.calendar_auntie.model.repositories;

import com.calendar_auntie.model.dtos.blog.BlogCategoryDTO;
import com.calendar_auntie.model.entities.BlogCategory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, UUID> {
  boolean existsBySlug(String slug);
  Optional<BlogCategory> findBySlug(String slug);
  BlogCategoryDTO findBlogCategoriesById(UUID id);
}