package org.jaga.model.dtos.payment;

import java.util.UUID;

public class CartItemDTO {

  private UUID productId;

  private int quantity;

  private String size;

  private String currency;

  public CartItemDTO(UUID productId, int quantity, String size, String currency) {
    this.productId = productId;
    this.quantity = quantity;
    this.size = size;
    this.currency = currency;
  }

  public UUID getProductId() {
    return productId;
  }

  public CartItemDTO setProductId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public int getQuantity() {
    return quantity;
  }

  public CartItemDTO setQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public String getSize() {
    return size;
  }

  public CartItemDTO setSize(String size) {
    this.size = size;
    return this;
  }

  public String getCurrency() {
    return currency;
  }
  public CartItemDTO setCurrency(String currency) {
    this.currency = currency;
    return this;
  }
}
