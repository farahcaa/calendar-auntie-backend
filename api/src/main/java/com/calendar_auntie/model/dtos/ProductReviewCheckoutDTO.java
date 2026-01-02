package com.calendar_auntie.model.dtos;

import java.util.UUID;

public record ProductReviewCheckoutDTO(String title, double price, UUID id, String thumbnail) {
}
