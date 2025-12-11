package org.jaga.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jaga.model.enums.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(generator = "uuid")
  @Column(name = "id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer; // can be null per liquibase

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 32)
  private OrderStatus status = OrderStatus.PENDING;

  @Column(name = "subtotal_price", nullable = false)
  private long subtotalPrice = 0;

  @Column(name = "shipping_price", nullable = false)
  private long shippingPrice = 0;

  @Column(name = "tax_price", nullable = false)
  private long taxPrice = 0;

  @Column(name = "total_price", nullable = false)
  private long totalPrice = 0;

  @Column(name = "billing_address_id")
  private UUID billingAddressId;

  @Column(name = "shipping_address_id")
  private UUID shippingAddressId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @OneToMany(
    mappedBy = "order",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  private List<OrderItem> items = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public Order() {}

  public UUID getId() {
    return id;
  }
  public Order setId(UUID id) {
    this.id = id;
    return this;
  }
  public Customer getCustomer() {
    return customer;
  }
  public Order setCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }
  public OrderStatus getStatus() {
    return status;
  }
  public Order setStatus(OrderStatus status) {
    this.status = status;
    return this;
  }
  public long getSubtotalPrice() {
    return subtotalPrice;
  }
  public Order setSubtotalPrice(long subtotalPrice) {
    this.subtotalPrice = subtotalPrice;
    return this;
  }
  public long getShippingPrice() {
    return shippingPrice;
  }
  public Order setShippingPrice(long shippingPrice) {
    this.shippingPrice = shippingPrice;
    return this;
  }
  public long getTaxPrice() {
    return taxPrice;
  }
  public Order setTaxPrice(long taxPrice) {
    this.taxPrice = taxPrice;
    return this;
  }
  public long getTotalPrice() {
    return totalPrice;
  }
  public Order setTotalPrice(long totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }
  public UUID getBillingAddressId() {
    return billingAddressId;
  }
  public Order setBillingAddressId(UUID billingAddressId) {
    this.billingAddressId = billingAddressId;
    return this;
  }
  public UUID getShippingAddressId() {
    return shippingAddressId;
  }
  public Order setShippingAddressId(UUID shippingAddressId) {
    this.shippingAddressId = shippingAddressId;
    return this;
  }
  public Instant getCreatedAt() {
    return createdAt;
  }
  public Order setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }
  public Instant getUpdatedAt() {
    return updatedAt;
  }
  public Order setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
  public List<OrderItem> getItems() {
    return items;
  }
  public Order setItems(List<OrderItem> items) {
    this.items = items;
    return this;
  }

}
