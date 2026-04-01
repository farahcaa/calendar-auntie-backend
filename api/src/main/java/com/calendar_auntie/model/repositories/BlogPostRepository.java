package com.calendar_auntie.model.repositories;

import com.calendar_auntie.model.dtos.blog.BlogPostDTO;
import com.calendar_auntie.model.entities.BlogPost;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {
  boolean existsBySlug(String slug);
  Optional<BlogPost> findBySlug(String slug);
  boolean existsByCategory_Id(UUID categoryId);

  List<BlogPostDTO> findBlogPostsByCategory_Id(UUID categoryId);

  List<BlogPostDTO> findAllByOrderByCreatedAtDesc();
}