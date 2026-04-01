package com.calendar_auntie.services;

import com.calendar_auntie.controllers.AdminBlogController;
import com.calendar_auntie.model.dtos.blog.CreateBlogCategoryRequest;
import com.calendar_auntie.model.dtos.blog.CreateBlogPostRequest;
import com.calendar_auntie.model.entities.BlogCategory;
import com.calendar_auntie.model.entities.BlogPost;
import com.calendar_auntie.model.repositories.BlogCategoryRepository;
import com.calendar_auntie.model.repositories.BlogPostRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class AdminBlogService {

  private final BlogCategoryRepository blogCategoryRepository;
  private final BlogPostRepository blogPostRepository;

  public AdminBlogService(
    BlogPostRepository blogPostRepository,
    BlogCategoryRepository blogCategoryRepository
  ) {
    this.blogCategoryRepository = blogCategoryRepository;
    this.blogPostRepository = blogPostRepository;
  }

  @Transactional
  public ResponseEntity<Object> createCategory(@RequestBody CreateBlogCategoryRequest request) {
    if (blogCategoryRepository.existsBySlug(request.slug())) {
      return ResponseEntity.status(409).body("conflict please change the slug");
    }

    BlogCategory category = new BlogCategory();
    category.setId(UUID.randomUUID());
    category.setName(request.name());
    category.setSlug(request.slug());
    category.setDescription(request.description());

    OffsetDateTime now = OffsetDateTime.now();
    category.setCreatedAt(now);
    category.setUpdatedAt(now);

    blogCategoryRepository.save(category);
    return ResponseEntity.ok("success");
  }

  @Transactional
  public void deleteCategory(UUID id) {
    BlogCategory category = blogCategoryRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Blog category not found: " + id));

    if (blogPostRepository.existsByCategory_Id(id)) {
      throw new IllegalStateException("Cannot delete category with existing posts: " + id);
    }

    blogCategoryRepository.delete(category);
  }

  @Transactional
  public ResponseEntity<Object> createPost(CreateBlogPostRequest request) {
    if (blogPostRepository.existsBySlug(request.slug())) {
      throw new IllegalArgumentException("Blog post slug already exists: " + request.slug());
    }

    BlogCategory category = blogCategoryRepository.findById(request.categoryId())
      .orElseThrow(() -> new IllegalArgumentException("Blog category not found: " + request.categoryId()));

    BlogPost post = new BlogPost();
    post.setId(UUID.randomUUID());
    post.setCategory(category);
    post.setTitle(request.title());
    post.setSlug(request.slug());
    post.setExcerpt(request.excerpt());
    post.setBodyMarkdown(request.bodyMarkdown());

    OffsetDateTime now = OffsetDateTime.now();
    post.setCreatedAt(now);
    post.setUpdatedAt(now);

    blogPostRepository.save(post);
    return ResponseEntity.ok("success");
  }

  @Transactional
  public void deletePost(UUID id) {
    BlogPost post = blogPostRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

    blogPostRepository.delete(post);
  }

}
