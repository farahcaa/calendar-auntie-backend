package com.calendar_auntie.services;

import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MediaFinderService {

  private final MinioClient minioClient;

  @Value("${s3.media-bucket-name}")
  private String mediaBucketName;

  @Autowired
  public MediaFinderService(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  /**
   * Finds a post-draft media
   *
   * @param path the path
   * @return the post-draft media
   */
  public boolean findMedia(String path)
    throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
    NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
    InternalException {

    StatObjectResponse statObjectResponse = minioClient.statObject(
      StatObjectArgs.builder().bucket(mediaBucketName).object(path).build());

    return statObjectResponse != null;
  }


}
