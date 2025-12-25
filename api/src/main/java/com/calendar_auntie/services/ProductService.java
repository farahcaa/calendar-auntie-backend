package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.ProductReviewCheckoutDTO;
import java.util.List;
import java.util.UUID;
import com.calendar_auntie.model.dtos.ProductDTO;
import com.calendar_auntie.model.mappers.ProductMapper;
import com.calendar_auntie.model.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  public Page<ProductDTO> getProducts(Pageable pageable){
    return productRepository.findAllByIsActiveTrue(pageable).map(productMapper::toDto);
  }

  public ProductDTO getProductById(UUID id){
    return productRepository.findById(id).map(productMapper::toDto).orElse(null);
  }

  public List<ProductReviewCheckoutDTO> getProductsByIds(List<UUID> ids){
    if (ids == null || ids.isEmpty()) return null;
    return productRepository.findAllById(ids).stream().map(productMapper::toProductReviewCheckoutDTO).toList();
  }
}
