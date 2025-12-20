package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.InitializeMediaUploadDTO;
import com.calendar_auntie.model.entities.Product;
import com.calendar_auntie.model.entities.ProductMedia;
import com.calendar_auntie.model.repositories.ProductMediaRepository;
import com.calendar_auntie.model.repositories.ProductRepository;
import com.calendar_auntie.services.MediaFinderService;
import com.calendar_auntie.services.MinioMediaPresignedURLCreatorService;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products/{productId}/media")
@CrossOrigin
public class MediaController {

  private static final Logger LOGGER =
    org.slf4j.LoggerFactory.getLogger(MediaController.class);
  private final MinioMediaPresignedURLCreatorService minioMediaPresignedURLCreatorService;
  private final ProductRepository productRepository;
  private final MediaFinderService mediaFinderService;
  private final ProductMediaRepository productMediaRepository;

  public MediaController(
    ProductMediaRepository productMediaRepository,
    MediaFinderService mediaFinderService,
    MinioMediaPresignedURLCreatorService minioMediaPresignedURLCreatorService,
    ProductRepository productRepository
   ) {
    this.productMediaRepository = productMediaRepository;
    this.mediaFinderService = mediaFinderService;
    this.minioMediaPresignedURLCreatorService = minioMediaPresignedURLCreatorService;
    this.productRepository = productRepository;
  }

  /**
   * Initialize media upload
   * <p>
   * This endpoint is called to generate a presigned URL for media upload, direct
   * from the client to S3
   * </p>
   *
   * @param productId post draft id
   * @return response
   */
  @PostMapping
  public ResponseEntity<Object> initializeMediaUpload(
    @PathVariable UUID productId) {

    if (productId == null) {
      LOGGER.error("id is null");
      return ResponseEntity.badRequest().build();
    }

    Optional<Product> productOptional = productRepository.findById(productId);

    if (productOptional.isEmpty()) {
      LOGGER.error("product not found");
      return ResponseEntity.status(404).build();
    }

    // Change this to enable more media
    if(!productOptional.get().getMedia().isEmpty()){
      LOGGER.error("product has media");
      return ResponseEntity.badRequest().build();
    }
    UUID mediaId = UUID.randomUUID();

    String path =
      generatePathForProductMedia(productOptional.get().getId(),
        mediaId);

    String presignedURL;
    try {
      presignedURL = minioMediaPresignedURLCreatorService.createPresignedURL(path);
    } catch (ServerException | IOException | InsufficientDataException | ErrorResponseException |
             NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException |
             XmlParserException | InternalException e) {
      LOGGER.error("Error creating presigned URL", e);
      return ResponseEntity.status(500).build();
    }

    return ResponseEntity.ok(new InitializeMediaUploadDTO(presignedURL, mediaId));
  }

  /**
   * Finalize media upload
   * <p>This endpoint is called when the media upload is complete, from client direct to S3</p>
   *
   * @param mediaId     media id
   * @param productId  id
   * @return response
   */
  @PutMapping("/{mediaId}")
  public ResponseEntity<Object> finalizeMediaUpload(
    @PathVariable UUID mediaId,
    @PathVariable UUID productId) {


    if (productId == null) {
      LOGGER.error("postDraftId is null");
      return ResponseEntity.badRequest().build();
    }

    if (mediaId == null) {
      LOGGER.error("mediaId is null");
      return ResponseEntity.badRequest().build();
    }

    Optional<Product> productOptional = productRepository.findById(productId);

    if (productOptional.isEmpty()) {
      LOGGER.error("postDraft not found");
      return ResponseEntity.status(404).build();
    }

    // find the media in the bucket
    String path =
      generatePathForProductMedia(productId,
        mediaId);
    LOGGER.info(path);
    boolean exists;
    try {
      exists = mediaFinderService.findMedia(path);
    } catch (ServerException | NoSuchAlgorithmException | InvalidKeyException |
             InvalidResponseException | XmlParserException | IOException | ErrorResponseException |
             InternalException | InsufficientDataException e) {
      LOGGER.error("Error finding media", e);
      return ResponseEntity.status(500).build();
    }

    if (!exists) {
      LOGGER.error("media not found in bucket");
      return ResponseEntity.status(404).build();
    }
    Product product = productOptional.get();

    ProductMedia media = productMediaRepository.findById(mediaId)
      .orElseGet(() -> new ProductMedia().setId(mediaId));

    media.setProduct(product);
    media.setUrl(path).setCreatedAt(Instant.now());

    productMediaRepository.saveAndFlush(media);
    product.getMedia().add(media);

    productRepository.saveAndFlush(product);

    return ResponseEntity.ok("success");
  }

  @DeleteMapping("/delete/{mediaId}")
  public ResponseEntity<Object> deleteMedia(
    @PathVariable UUID mediaId,
    @PathVariable UUID productId) {

    if (productId == null) {
      LOGGER.error("productId is null");
      return ResponseEntity.badRequest().build();
    }

    if (mediaId == null) {
      LOGGER.error("mediaId is null");
      return ResponseEntity.badRequest().build();
    }

    Optional<Product> productOptional =productRepository.findById(productId);

    if (productOptional.isEmpty()) {
      LOGGER.error("product not found");
      return ResponseEntity.status(404).build();
    }

    Optional<ProductMedia> productMediaOptional = productMediaRepository.findById(mediaId);

    if (productMediaOptional.isEmpty()) {
      LOGGER.error("postDraftMedia not found");
      return ResponseEntity.status(404).build();
    }
    ProductMedia media =productMediaOptional.get();

// Optional: check if this media belongs to the postDraft
    if (!media.getProduct().getId().equals(productId)) {
      LOGGER.error("Media does not belong to the specified post draft");
      return ResponseEntity.status(403).build();
    }
    Product product =productOptional.get();

    product.getMedia().remove(media);

    // 👈 important
    productMediaRepository.delete(media);
    productRepository.save(product);
    LOGGER.info("Deleted media with ID {}", mediaId);
    return ResponseEntity.ok().build();


  }

  public String generatePathForProductMedia( UUID productId, UUID mediaId) {
    return String.format("products/%s/%s", productId, mediaId);
  }
}
