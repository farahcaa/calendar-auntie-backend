package org.jaga.model.dtos.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jaga.model.enums.AddressType;

public class CustomerInfoDTO {

  // --- Customer (person-level info) ---
  @NotBlank
  @Size(max = 100)
  private String firstName;

  @NotBlank
  @Size(max = 100)
  private String lastName;

  @NotBlank
  @Email
  @Size(max = 255)
  private String email;

  @Size(max = 30)
  private String phone;

  private AddressDTO shippingAddress;

  private AddressDTO billingAddress;

  public CustomerInfoDTO() {}

  // --- Getters & Setters ---
  public String getFirstName() {
    return firstName;
  }

  public CustomerInfoDTO setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public CustomerInfoDTO setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public CustomerInfoDTO setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public CustomerInfoDTO setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public AddressDTO getShippingAddress() {
    return shippingAddress;
  }

  public CustomerInfoDTO setShippingAddress(AddressDTO shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

  public AddressDTO getBillingAddress() {
    return billingAddress;
  }

  public CustomerInfoDTO setBillingAddress(AddressDTO billingAddress) {
    this.billingAddress = billingAddress;
    return this;
  }

}
