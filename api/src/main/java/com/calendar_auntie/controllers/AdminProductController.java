package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.FullAdminProductDTO;
import com.calendar_auntie.model.dtos.payment.CreateProductDTO;
import com.calendar_auntie.services.AdminProductService;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @GetMapping("/{productId}")
  public ResponseEntity<Object> getProductById(@PathVariable UUID productId){
    FullAdminProductDTO fullAdminProductDTO = adminProductService.getAdminProductById(productId);
    if (fullAdminProductDTO == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error draft does not exist");
    }
    return ResponseEntity.ok(fullAdminProductDTO);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<Object> updateProduct(@PathVariable UUID productId, @RequestBody FullAdminProductDTO createProductDTO){
    adminProductService.updateProduct(productId,createProductDTO);
    return ResponseEntity.ok("Success");
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Object> deleteProduct(@PathVariable UUID productId)
    throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
    NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
    InternalException {
    adminProductService.deleteProduct(productId);
    return ResponseEntity.ok("Success");
  }
}
