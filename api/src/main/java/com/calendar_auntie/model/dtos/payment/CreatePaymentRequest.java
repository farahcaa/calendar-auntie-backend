package com.calendar_auntie.model.dtos.payment;

import java.util.List;

public class CreatePaymentRequest {
 private List<CartItemDTO> items;

 private CustomerInfoDTO customer;

 public CreatePaymentRequest(List<CartItemDTO> items, CustomerInfoDTO customer) {
     this.items = items;
     this.customer = customer;
 }

 public List<CartItemDTO> getItems() {
     return items;
 }
 public CustomerInfoDTO getCustomer() {
     return customer;
 }

 public CreatePaymentRequest setCustomer(CustomerInfoDTO customer) {
     this.customer = customer;
     return this;
 }

public CreatePaymentRequest setItems(List<CartItemDTO> items) {
     this.items = items;
     return this;
}

}
