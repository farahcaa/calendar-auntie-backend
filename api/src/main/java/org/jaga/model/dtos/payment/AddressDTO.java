package org.jaga.model.dtos.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jaga.model.enums.AddressType;

public class AddressDTO {
  // --- Address (shipping/billing info) ---

  @Size(max = 200)
  private AddressType addressType;

  @NotBlank
  @Size(max = 255)
  private String line1;

  @Size(max = 255)
  private String line2;

  @NotBlank
  @Size(max = 100)
  private String city;

  @NotBlank
  @Size(max = 100)
  private String state;

  @NotBlank
  @Size(max = 20)
  private String postalCode;

  @NotBlank
  @Size(max = 2)
  private String countryCode;

  public AddressDTO() {
  }

  public AddressDTO setAddressType(AddressType addressType) {
    this.addressType = addressType;
    return this;
  }
  public AddressDTO setLine1(String line1) {
    this.line1 = line1;
    return this;
  }
  public AddressDTO setLine2(String line2) {
    this.line2 = line2;
    return this;
  }
  public AddressDTO setCity(String city) {
    this.city = city;
    return this;
  }
  public AddressDTO setState(String state) {
    this.state = state;
    return this;
  }
  public AddressDTO setPostalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }
  public AddressDTO setCountryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }
  public AddressType getAddressType() {
    return this.addressType;
  }
  public String getLine1() {
    return this.line1;
  }
  public String getLine2() {
    return this.line2;
  }
  public String getCity() {
    return this.city;
  }
  public String getState() {
    return this.state;
  }
  public String getPostalCode() {
    return this.postalCode;
  }
  public String getCountryCode() {
    return this.countryCode;
  }

}
