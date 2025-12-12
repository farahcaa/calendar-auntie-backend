package com.calendar_auntie.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue(generator = "uuid")
  @Column(name = "id")
  private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant UpdatedAt;

  public Customer() {}

  public UUID getId() {
    return id;
  }
  public Customer setId(UUID id) {
    this.id = id;
    return this;
  }
  public String getFirstName() {
    return firstName;
  }
  public Customer setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }
  public String getLastName() {
    return lastName;
  }
  public Customer setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }
  public String getEmail() {
    return email;
  }
  public Customer setEmail(String email) {
    this.email = email;
    return this;
  }
  public String getPhone() {
    return phone;
  }
  public Customer setPhone(String phone) {
    this.phone = phone;
    return this;
  }
  public Instant getCreatedAt() {
    return createdAt;
  }
  public Customer setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }
  public Instant getUpdatedAt() {
    return UpdatedAt;
  }
  public Customer setUpdatedAt(Instant updatedAt) {

    UpdatedAt = updatedAt;
    return this;
  }
}
