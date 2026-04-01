package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.blog.CreateBlogCategoryRequest;
import com.calendar_auntie.model.dtos.blog.CreateBlogPostRequest;
import com.calendar_auntie.services.AdminBlogService;
import jakarta.validation.Valid;
import java.util.UUID;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/blog")
public class AdminBlogController {

  private final AdminBlogService adminBlogService;

  public AdminBlogController(AdminBlogService adminBlogService){
    this.adminBlogService = adminBlogService;
  }

  @PostMapping()
  public ResponseEntity<Object> createCategory(@RequestBody @Valid CreateBlogCategoryRequest createBlogCategoryRequest){
    return adminBlogService.createCategory(createBlogCategoryRequest);
  }

  @DeleteMapping()
  public ResponseEntity<Object> deleteCategory(UUID id){
    adminBlogService.deleteCategory(id);
    return ResponseEntity.ok("success");
  }

  @PostMapping("/post")
  public ResponseEntity<Object> createPost(@RequestBody @Valid CreateBlogPostRequest createBlogPostRequest){
    return adminBlogService.createPost(createBlogPostRequest);
  }

  @DeleteMapping("/post")
  public ResponseEntity<Object> deletePost(UUID id){
    adminBlogService.deletePost(id);
    return ResponseEntity.ok("success");
  }

}
