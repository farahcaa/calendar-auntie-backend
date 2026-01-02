package com.calendar_auntie.model.dtos;

import java.util.List;

public record CreateOrderRequest(List<CheckoutOrderDTO> checkoutOrderDTO, CheckoutAddressDTO checkoutAddressDTO) {
}
