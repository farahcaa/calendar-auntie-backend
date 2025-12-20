package com.calendar_auntie.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioClientProvider {

  @Value("${s3.secret-access-key}")
  private String secretAccessKey;

  @Value("${s3.access-key-id}")
  private String accessKeyId;

  @Value("${s3.endpoint}")
  private String endpoint;

  @Bean
  @Primary
  public MinioClient getMinioClient() {
    return MinioClient.builder().endpoint(endpoint)
      .credentials(accessKeyId, secretAccessKey).build();
  }


}