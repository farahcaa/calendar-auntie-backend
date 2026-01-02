package com.calendar_auntie.model.dtos;

public record OrderItemDTO(String id, String title, String thumbnail, int quantity, double unit_price, double total_price) {
}
