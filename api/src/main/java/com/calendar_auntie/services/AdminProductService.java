package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.AdminProductDTO;
import com.calendar_auntie.model.dtos.FullAdminProductDTO;
import com.calendar_auntie.model.dtos.payment.CreateProductDTO;
import com.calendar_auntie.model.entities.Product;
import com.calendar_auntie.model.entities.ProductMedia;
import com.calendar_auntie.model.repositories.ProductRepository;


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

  ProductRepository productRepository;

  @Autowired
  public AdminProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<AdminProductDTO> getAdminProducts(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);

    System.out.println(products);
    return products.map(p -> {
      String thumbnail = (p.getMedia() != null && !p.getMedia().isEmpty())
        ? p.getMedia().get(0).getUrl()
        : null; // or "" or a placeholder URL

      return new AdminProductDTO(thumbnail, p.getId(), p.getPrice(), p.getTitle());
    });
  }


  public UUID createProduct() {
    Product product = new Product().setTitle("").setPrice(0).setActive(false).setSku("").setDescription("");
    productRepository.save(product);
    return product.getId();
  }

  public boolean updateProduct(CreateProductDTO createProductDTO) {
    Optional<Product> product = productRepository.findById(createProductDTO.id());
    if (!product.isPresent()) {
      return false;
    }
    product.get().setActive(createProductDTO.isActive());
    product.get().setDescription(createProductDTO.description());
    product.get().setPrice(createProductDTO.price());
    product.get().setInventoryQty(createProductDTO.inventoryQty());
    productRepository.save(product.get());
    return true;
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
}
