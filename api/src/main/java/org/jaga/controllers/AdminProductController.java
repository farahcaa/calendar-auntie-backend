package org.jaga.controllers;

import org.jaga.model.dtos.payment.CreateProductDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController("/admin/products")
public class AdminProductController {

  @PostMapping("/")
  public ResponseEntity<Object> createPost(@RequestBody CreateProductDTO createProductDTO){
//to do
    return null;
  }

  @PutMapping
  public ResponseEntity<Object> updatePost(@RequestBody CreateProductDTO createProductDTO){
    //to do
    return null;
  }
}
