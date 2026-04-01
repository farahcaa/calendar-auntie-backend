package com.calendar_auntie.model.dtos.blog;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BlogPostDTO(
  UUID id,
  UUID categoryId,
  String categoryName,
  String categorySlug,
  String title,
  String slug,
  String excerpt,
  String bodyMarkdown,
  OffsetDateTime createdAt,
  OffsetDateTime updatedAt
) {}