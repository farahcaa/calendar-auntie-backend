package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.payment.CreateProductDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

  @PostMapping()
  public ResponseEntity<Object> createPost(@RequestBody CreateProductDTO createProductDTO){
    return ResponseEntity.ok("Hello World");

  }

  @PutMapping
  public ResponseEntity<Object> updatePost(@RequestBody CreateProductDTO createProductDTO){
    //to do
    return null;
  }
}
