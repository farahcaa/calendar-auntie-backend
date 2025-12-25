package com.calendar_auntie.model.dtos;

import java.util.UUID;

public record ProductDTO(UUID id, String thumbnail, String title, double price, String description) {
}
