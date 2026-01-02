package com.calendar_auntie.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import com.calendar_auntie.model.enums.AddressType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "addresses")
public class Address {

  @Id
  @GeneratedValue(generator = "uuid")
  @Column(name = "id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_order_customer"))
  private Customer customer;

  @Column(name = "name")
  private String name;

  @Column(name = "line1")
  private String line1;

  @Column(name = "line2")
  private String line2;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "country_code")
  private String countryCode;

  @Column(name = "address_type")
  @Enumerated(EnumType.STRING)
  private AddressType addressType;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  public Address() {}

  public UUID getId() {
    return id;
  }
  public Address setId(UUID id) {
    this.id = id;
    return this;
  }
  public Customer getCustomer() {
    return customer;
  }
  public Address setCustomer(Customer customer) {
    this.customer = customer;
    return this;
  }
  public String getName() {
    return name;
  }
  public Address setName(String name) {
    this.name = name;
    return this;
  }
  public String getLine1() {
    return line1;
  }
  public Address setLine1(String line1) {
    this.line1 = line1;
    return this;
  }
  public String getLine2() {
    return line2;
  }
  public Address setLine2(String line2) {
    this.line2 = line2;
    return this;
  }
  public String getCity() {
    return city;
  }
  public Address setCity(String city) {
    this.city = city;
    return this;
  }
  public String getState() {
    return state;
  }
  public Address setState(String state) {
    this.state = state;
    return this;
  }
  public AddressType getAddressType() {
    return this.addressType;
  }
  public  Address setAddressType(AddressType addressType) {
    this.addressType = addressType;
    return this;
  }
  public String getPostalCode() {
    return postalCode;
  }
  public Address setPostalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }
  public String getCountryCode() {
    return countryCode;
  }
  public Address setCountryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }
  public Instant getCreatedAt() {
    return createdAt;
  }
  public Address setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }
  public Instant getUpdatedAt() {
    return updatedAt;
  }
  public Address setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
