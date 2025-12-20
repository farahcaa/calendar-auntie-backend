package com.calendar_auntie.model.dtos.payment;

import java.util.List;
import java.util.UUID;

public record CreateProductDTO(UUID id, String sku, String title, String description, double price, int inventoryQty, boolean isActive,
                               List<String> media) {

}
