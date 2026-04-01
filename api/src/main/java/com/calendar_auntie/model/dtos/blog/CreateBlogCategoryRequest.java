package com.calendar_auntie.model.dtos.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateBlogCategoryRequest(
  @NotBlank
  @Size(max = 100)
  String name,

  @NotBlank
  @Size(max = 120)
  String slug,

  @Size(max = 5000)
  String description
) {}