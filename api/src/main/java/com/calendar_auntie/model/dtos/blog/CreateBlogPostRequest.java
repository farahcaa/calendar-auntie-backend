package com.calendar_auntie.model.dtos.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateBlogPostRequest(
  @NotNull
  UUID categoryId,

  @NotBlank
  @Size(max = 200)
  String title,

  @NotBlank
  @Size(max = 220)
  String slug,

  String excerpt,

  @NotBlank
  String bodyMarkdown
) {}