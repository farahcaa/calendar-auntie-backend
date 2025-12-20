package com.calendar_auntie.model.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record AdminProductDTO(String thumbnail, UUID id, double price, String name) {
}
