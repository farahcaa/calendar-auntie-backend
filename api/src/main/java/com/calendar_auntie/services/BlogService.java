package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.blog.BlogCategoryDTO;
import com.calendar_auntie.model.dtos.blog.BlogPostDTO;
import com.calendar_auntie.model.entities.BlogCategory;
import com.calendar_auntie.model.entities.BlogPost;
import com.calendar_auntie.model.repositories.BlogCategoryRepository;
import com.calendar_auntie.model.repositories.BlogPostRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

  private final BlogCategoryRepository blogCategoryRepository;
  private final BlogPostRepository blogPostRepository;

  public BlogService(
    BlogCategoryRepository blogCategoryRepository,
    BlogPostRepository blogPostRepository
  ) {
    this.blogCategoryRepository = blogCategoryRepository;
    this.blogPostRepository = blogPostRepository;
  }

  public List<BlogCategoryDTO> getCategories() {
    return blogCategoryRepository.findAll()
      .stream()
      .map(this::toCategoryDto)
      .toList();
  }

  public BlogPostDTO getPostById(UUID id) {
    BlogPost post = blogPostRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

    return toPostDto(post);
  }

  private BlogCategoryDTO toCategoryDto(BlogCategory category) {
    return new BlogCategoryDTO(
      category.getId(),
      category.getName(),
      category.getSlug(),
      category.getDescription(),
      category.getCreatedAt(),
      category.getUpdatedAt()
    );
  }

  private BlogPostDTO toPostDto(BlogPost post) {
    return new BlogPostDTO(
      post.getId(),
      post.getCategory().getId(),
      post.getCategory().getName(),
      post.getCategory().getSlug(),
      post.getTitle(),
      post.getSlug(),
      post.getExcerpt(),
      post.getBodyMarkdown(),
      post.getCreatedAt(),
      post.getUpdatedAt()
    );
  }

  public ResponseEntity<List<BlogPostDTO>> getBlogsByCategoryId(UUID id) {
    List<BlogPostDTO> blogPosts = blogPostRepository.findBlogPostsByCategory_Id(id);
    return ResponseEntity.ok(blogPosts); // empty list is fine
  }

  public ResponseEntity<BlogCategoryDTO> getCategoryById(UUID id) {
    BlogCategoryDTO dto = blogCategoryRepository.findBlogCategoriesById(id);

    if (dto == null) {
      return ResponseEntity.notFound().build(); // 404 instead of 204
    }

    return ResponseEntity.ok(dto);
  }

  public ResponseEntity<List<BlogPostDTO>> getPostsByRecent() {

    List<BlogPostDTO> blogPostDTOList = blogPostRepository.findAllByOrderByCreatedAtDesc();
    if (blogPostDTOList == null ) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(blogPostDTOList.stream().limit(4).toList());
  }
}