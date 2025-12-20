package com.calendar_auntie.services;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinioMediaPresignedURLCreatorService {

  private final String bucketName;
  private final MinioClient minioClient;

  @Autowired
  public MinioMediaPresignedURLCreatorService(MinioClient minioClient,
                                              @Value("${s3.media-bucket-name}") String bucketName) {
    this.minioClient = minioClient;
    this.bucketName = bucketName;
  }

  /**
   * Creates a presigned URL for a given object path
   *
   * @param objectPath the object path
   * @return the presigned URL
   */
  public String createPresignedURL(String objectPath)
    throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
    NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
    InternalException {
    return minioClient.getPresignedObjectUrl(
      // TODO: configurable expiry
      GetPresignedObjectUrlArgs.builder().expiry(15, TimeUnit.MINUTES).bucket(bucketName)
        .object(objectPath).method(Method.PUT).build());
  }
}
