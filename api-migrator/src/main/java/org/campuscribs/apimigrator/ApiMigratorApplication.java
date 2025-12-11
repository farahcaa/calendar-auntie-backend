package org.campuscribs.apimigrator;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiMigratorApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder(ApiMigratorApplication.class)
      .web(WebApplicationType.NONE)
      .run(args);
    // Do NOT System.exit here. The runner below will exit
  }

  /**
   * Runs after the context is ready (Liquibase has already executed).
   * If Liquibase failed, the app would not reach this runner.
   */
  @Bean
  CommandLineRunner exitWhenDone(ConfigurableApplicationContext ctx) {
    return args -> {
      System.out.println("Liquibase completed successfully. Exiting…");
      int code = SpringApplication.exit(ctx, () -> 0);
      System.exit(code);
    };
  }
}
