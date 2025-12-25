package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.ProductReviewCheckoutDTO;
import java.util.List;
import java.util.UUID;
import com.calendar_auntie.model.dtos.ProductDTO;
import com.calendar_auntie.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/products")
  public ResponseEntity<Object> getProducts(
    @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
    Pageable pageable
  ) {
   Page<ProductDTO> products = productService.getProducts(pageable);
   if (products.getTotalElements() == 0) {
     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }
   return ResponseEntity.ok(products);
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Object> getProduct(@PathVariable UUID id) {
    ProductDTO productDTO = productService.getProductById(id);
    if (productDTO == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(productDTO, HttpStatus.OK);
  }
  @PostMapping("/products/items")
  public ResponseEntity<List<ProductReviewCheckoutDTO>> getProductsByIds(
    @RequestBody List<UUID> ids
  ) {
    List<ProductReviewCheckoutDTO> products = productService.getProductsByIds(ids);
    return ResponseEntity.ok(products);
  }

}
