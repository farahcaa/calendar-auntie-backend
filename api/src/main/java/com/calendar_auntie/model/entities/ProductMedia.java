package com.calendar_auntie.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "media")
public class ProductMedia {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "url")
  private String url;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public ProductMedia() {}

  public ProductMedia(String url, Instant createdAt) {
    this.url = url;
    this.createdAt = createdAt;
  }
  public UUID getId() {
    return id;
  }
  public ProductMedia setId(UUID id) {
    this.id = id;
    return this;
  }
  public String getUrl() {
    return url;
  }
  public ProductMedia setUrl(String url) {
    this.url = url;
    return this;
  }
  public Instant getCreatedAt() {
    return createdAt;
  }
  public ProductMedia setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Product getProduct() {
    return product;
  }

  public ProductMedia setProduct(Product product) {
    this.product = product;
    return this;
  }
}
