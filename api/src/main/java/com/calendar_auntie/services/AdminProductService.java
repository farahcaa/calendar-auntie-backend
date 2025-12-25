package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.AdminProductDTO;
import com.calendar_auntie.model.dtos.FullAdminProductDTO;
import com.calendar_auntie.model.entities.Product;
import com.calendar_auntie.model.entities.ProductMedia;
import com.calendar_auntie.model.repositories.ProductRepository;


import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {

  private final ProductRepository productRepository;
  private final MediaService mediaService;

  @Autowired
  public AdminProductService(ProductRepository productRepository, MediaService mediaService) {
    this.productRepository = productRepository;
    this.mediaService =mediaService;
  }

  public Page<AdminProductDTO> getAdminProducts(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);

    System.out.println(products);
    return products.map(p -> {
      String thumbnail = (p.getMedia() != null && !p.getMedia().isEmpty())
        ? p.getMedia().getFirst().getUrl()
        : null; // or "" or a placeholder URL

      return new AdminProductDTO(thumbnail, p.getId(), p.getPrice(), p.getTitle());
    });
  }


  public UUID createProduct() {
    Product product = new Product().setTitle("").setPrice(0).setActive(false).setSku("").setDescription("");
    productRepository.save(product);
    return product.getId();
  }

  public void updateProduct(UUID productId, FullAdminProductDTO createProductDTO) {
    Optional<Product> product = productRepository.findById(productId);
    if (product.isEmpty()) {
      return;
    }
    product.get().setActive(createProductDTO.isActive());
    product.get().setDescription(createProductDTO.description());
    product.get().setPrice(createProductDTO.price());
    product.get().setInventoryQty(createProductDTO.inventoryQty());
    product.get().setSku(createProductDTO.sku());
    product.get().setTitle(createProductDTO.title());
    productRepository.save(product.get());
  }

  public FullAdminProductDTO getAdminProductById(UUID id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isEmpty()) {
      return null;
    }
    List<String> urls = new ArrayList<>();
    List<ProductMedia> media = product.get().getMedia();
    media.forEach(m -> {urls.add(m.getUrl());});

    return new FullAdminProductDTO(urls,product.get().getId(),product.get().getPrice(), product.get().getTitle(),product.get().getSku(),product.get().getDescription(), product.get().getInventoryQty(), product.get().isActive());
  }

  public void deleteProduct(UUID id)
    throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
    NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
    InternalException {
    Optional<Product> product = productRepository.findById(id);
    if (product.isEmpty()) {
      return;
    }

    try {
      if (product.get().getMedia() != null && !product.get().getMedia().isEmpty()) {
        // needs changing if images change
        mediaService.deleteMediaByUrl(product.get().getMedia().getFirst().getUrl());
      }

      productRepository.deleteById(id);
    }
    catch (InsufficientDataException e) {
      throw e;
    }
  }
}
