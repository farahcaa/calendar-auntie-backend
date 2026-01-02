package com.calendar_auntie.model.dtos;

import com.calendar_auntie.model.dtos.payment.AddressDTO;
import java.util.List;

public record IndividualOrderDTO(String Name, String email, double total, double tax, double shipping, AddressDTO address, List<OrderItemDTO> orderItems) {
}
