package com.calendar_auntie.model.dtos.blog;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BlogCategoryDTO(
  UUID id,
  String name,
  String slug,
  String description,
  OffsetDateTime createdAt,
  OffsetDateTime updatedAt
) {}