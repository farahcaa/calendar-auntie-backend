package com.calendar_auntie.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(generator = "uuid")
  @Column(name = "id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "title")
  private String title;

  @Column(name = "sku")
  private String sku;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "unit_price", nullable = false)
  private double unitPrice;

  @Column(name = "total_price", nullable = false)
  private double totalPrice;

  public OrderItem() {}

  public UUID getId() {
    return id;
  }
  public OrderItem setId(UUID id) {
    this.id = id;
    return this;
  }
  public Order getOrder() {
    return order;
  }
  public OrderItem setOrder(Order order) {
    this.order = order;
    return this;
  }
  public Product getProduct() {
    return product;
  }
  public OrderItem setProduct(Product product) {
    this.product = product;
    return this;
  }
  public String getTitle() {
    return title;
  }
  public OrderItem setTitle(String title) {
    this.title = title;
    return this;
  }
  public String getSku() {
    return sku;
  }
  public OrderItem setSku(String sku) {
    this.sku = sku;
    return this;
  }
  public Integer getQuantity() {
    return quantity;
  }
  public OrderItem setQuantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }
  public double getUnitPrice() {
    return unitPrice;
  }
  public OrderItem setUnitPrice(double unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }
  public double getTotalPrice() {
    return totalPrice;
  }
  public OrderItem setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }
}
