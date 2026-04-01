package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.blog.BlogCategoryDTO;
import com.calendar_auntie.model.dtos.blog.BlogPostDTO;
import com.calendar_auntie.services.BlogService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
public class BlogController {

  private final BlogService blogService;

  public BlogController(BlogService blogService) {
    this.blogService = blogService;
  }

  @GetMapping("/categories")
  public ResponseEntity<List<BlogCategoryDTO>> getCategories() {
    return ResponseEntity.ok(blogService.getCategories());
  }

  @GetMapping("/categories/{id}")
  public  ResponseEntity<BlogCategoryDTO> getCategoryById(@PathVariable UUID id){
    return blogService.getCategoryById(id);
  }

  @GetMapping("/categories/{id}/posts")
  public ResponseEntity<List<BlogPostDTO>> getBlogsByCategoryId(@PathVariable UUID id){
    return blogService.getBlogsByCategoryId(id);
  }

  @GetMapping("/post/{id}")
  public ResponseEntity<BlogPostDTO> getPostById(@PathVariable UUID id) {
    return ResponseEntity.ok(blogService.getPostById(id));
  }

  @GetMapping("/posts/recent")
  public ResponseEntity<List<BlogPostDTO>> getPostsByRecent(){
    return blogService.getPostsByRecent();
  }
}