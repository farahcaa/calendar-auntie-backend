package com.calendar_auntie.model.dtos;

import java.util.List;
import java.util.UUID;

public record FullAdminProductDTO(List<String> thumbnails, UUID id, double price, String title, String sku, String description, int inventoryQty, boolean isActive ) {
}
