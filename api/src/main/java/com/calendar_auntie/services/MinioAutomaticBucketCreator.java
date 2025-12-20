package com.calendar_auntie.services;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinioAutomaticBucketCreator {

  private static final Logger LOGGER = LoggerFactory.getLogger(MinioAutomaticBucketCreator.class);
  private final MinioClient minioClient;

  @Value("${s3.media-bucket-name}")
  private String mediaBucketName;


  @Autowired
  public MinioAutomaticBucketCreator(MinioClient minioClient ) {
    this.minioClient = minioClient;
  }

  @PostConstruct
  public void init() {
    LOGGER.info("Initializing MinIO buckets + public-read policy");


    ensureBucketWithPublicRead(mediaBucketName);
    LOGGER.info("MinIO bucket init complete");
  }

  private void ensureBucketWithPublicRead(String bucket) {
    try {
      if (bucket == null || bucket.isBlank()) {
        LOGGER.warn("Skipping blank bucket name");
        return;
      }

      boolean exists = minioClient.bucketExists(
        BucketExistsArgs.builder().bucket(bucket).build());

      if (!exists) {
        LOGGER.info("Creating bucket: {}", bucket);
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
      } else {
        LOGGER.debug("Bucket {} already exists", bucket);
      }

      // Apply/ensure public-read (anonymous GET) policy
      setPublicReadPolicy(bucket);

    } catch (Exception e) {
      // Don’t take the whole app down—log clearly so you can fix creds/policy later
      LOGGER.error("Failed to ensure bucket '{}' with public-read: {}", bucket, e.toString(), e);
    }
  }

  private void setPublicReadPolicy(String bucket)
    throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
    NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
    XmlParserException, InternalException {

    // Public GET on all objects in this bucket.
    // If you also allow listing, add "s3:ListBucket" on arn:aws:s3:::bucket (see note below)
    String policyJson = """
      {
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Principal": {"AWS": ["*"]},
            "Action": ["s3:GetObject"],
            "Resource": ["arn:aws:s3:::%s/*"]
          }
        ]
      }
      """.formatted(bucket);

    LOGGER.info("Applying public-read policy to bucket {}", bucket);
    minioClient.setBucketPolicy(
      SetBucketPolicyArgs.builder().bucket(bucket).config(policyJson).build());
  }
}