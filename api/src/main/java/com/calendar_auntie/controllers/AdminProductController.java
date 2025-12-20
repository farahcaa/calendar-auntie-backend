package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.FullAdminProductDTO;
import com.calendar_auntie.model.dtos.payment.CreateProductDTO;
import com.calendar_auntie.services.AdminProductService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

  private final AdminProductService adminProductService;

  @Autowired
  public AdminProductController(AdminProductService adminProductService) {
    this.adminProductService = adminProductService;
  }

  @GetMapping()
  public ResponseEntity<Object> getAllProducts(Pageable pageable) {

  return ResponseEntity.ok(adminProductService.getAdminProducts(pageable));

  }
  @PostMapping()
  public ResponseEntity<Object> createPost(){

    UUID success = adminProductService.createProduct();

    return ResponseEntity.ok(success);

  }

  @PutMapping
  public ResponseEntity<Object> updatePost(@RequestBody CreateProductDTO createProductDTO){

    boolean success = adminProductService.updateProduct(createProductDTO);

    if (success) {
      return ResponseEntity.ok("Success");
    }

    return ResponseEntity.badRequest().build();
  }

  @GetMapping("/{productId}")
  public ResponseEntity<Object> getProductById(@PathVariable UUID productId){
    FullAdminProductDTO fullAdminProductDTO = adminProductService.getAdminProductById(productId);
    if (fullAdminProductDTO == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error draft does not exist");
    }
    return ResponseEntity.ok(fullAdminProductDTO);
  }
}
