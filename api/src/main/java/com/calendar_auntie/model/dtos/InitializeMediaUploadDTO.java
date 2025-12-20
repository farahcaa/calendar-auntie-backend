package com.calendar_auntie.model.dtos;

import java.util.UUID;

public class InitializeMediaUploadDTO {

  private String uploadUrl;

  private UUID mediaId;

  public InitializeMediaUploadDTO(String uploadUrl, UUID mediaId) {
    this.uploadUrl = uploadUrl;
    this.mediaId = mediaId;
  }

  public String getUploadUrl() {
    return uploadUrl;
  }

  public InitializeMediaUploadDTO setUploadUrl(String uploadUrl) {
    this.uploadUrl = uploadUrl;
    return this;
  }

  public UUID getMediaId() {
    return mediaId;
  }

  public InitializeMediaUploadDTO setMediaId(UUID mediaId) {
    this.mediaId = mediaId;
    return this;
  }
}
