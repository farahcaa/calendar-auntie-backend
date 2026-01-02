package com.calendar_auntie.model.entities;

import com.calendar_auntie.model.enums.AddressType;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.calendar_auntie.model.enums.OrderStatus;

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
  private double subtotalPrice = 0;

  @Column(name = "shipping_price", nullable = false)
  private double shippingPrice = 0;

  @Column(name = "tax_price", nullable = false)
  private double taxPrice = 0;

  @Column(name = "total_price", nullable = false)
  private double totalPrice = 0;

  @Column(name = "billing_address_id")
  private UUID billingAddressId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
  private Address shippingAddress;

  @Column(name = "stripe_payment_intent_id")
  private String stripePaymentIntentId;

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
  public double getSubtotalPrice() {
    return subtotalPrice;
  }
  public Order setSubtotalPrice(double subtotalPrice) {
    this.subtotalPrice = subtotalPrice;
    return this;
  }
  public double getShippingPrice() {
    return shippingPrice;
  }
  public Order setShippingPrice(double shippingPrice) {
    this.shippingPrice = shippingPrice;
    return this;
  }
  public double getTaxPrice() {
    return taxPrice;
  }
  public Order setTaxPrice(double taxPrice) {
    this.taxPrice = taxPrice;
    return this;
  }
  public double getTotalPrice() {
    return totalPrice;
  }
  public Order setTotalPrice(double totalPrice) {
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
  public Address getShippingAddress() {
    return shippingAddress;
  }
  
  public Order setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
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

  public Order setStripePaymentIntentID(String stripePaymentIntentId) {
    this.stripePaymentIntentId = stripePaymentIntentId;
    return this;
  }

  public String getStripePaymentIntentID() {
    return stripePaymentIntentId;
  }
}
