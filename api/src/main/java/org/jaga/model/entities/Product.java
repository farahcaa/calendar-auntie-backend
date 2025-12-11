package org.jaga.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(generator = "uuid")
  @Column(name = "id")
  private UUID id;

  @Column(name = "sku")
  private String sku;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private double price;

  @Column(name = "inventory_qty")
  private int inventoryQty;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductMedia> media = new ArrayList<>();
  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "created_at")
  @CreationTimestamp
  private Instant createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Instant updatedAt;

  public UUID getId() {
    return id;
  }
  public Product setId(UUID id) {
    this.id = id;
    return this;
  }
  public String getSku() {
    return sku;
  }
  public Product setSku(String sku) {
    this.sku = sku;
    return this;
  }
  public String getTitle() {
    return title;
  }
  public Product setTitle(String title) {
    this.title = title;
    return this;
  }
  public String getDescription() {
    return description;
  }
  public Product setDescription(String description) {
    this.description = description;
    return this;
  }
  public double getPrice() {
    return price;
  }
  public Product setPrice(double price) {
    this.price = price;
    return this;
  }
  public int getInventoryQty() {
    return inventoryQty;
  }
  public Product setInventoryQty(int inventoryQty) {
    this.inventoryQty = inventoryQty;
    return this;
  }
  public boolean isActive() {
    return isActive;
  }
  public Product setActive(boolean active) {
    isActive = active;
    return this;
  }
  public Instant getCreatedAt() {
    return createdAt;
  }
  public Product setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }
  public Instant getUpdatedAt() {
    return updatedAt;
  }
  public Product setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
