plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("com.google.cloud.tools.jib") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}
version = providers.gradleProperty("apiMigratorVersion").get()
description = "api-migrator"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql") // or newer
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
}


jib {
    from {
        image = "eclipse-temurin:21-jre"
    }
    to {
        image = "ghcr.io/java-backend/${project.name}:${version}"
        tags = setOf("$version", "latest")
    }
    container {
        jvmFlags = listOf("-XX:MaxRAMPercentage=75")
        user = "1000:1000"
        environment = mapOf("SPRING_PROFILES_ACTIVE" to "prod",
            "SPRING_MAIN_WEB_APPLICATION_TYPE" to "none",
            // optional but explicit:
            "SPRING_LIQUIBASE_ENABLED" to "true"
        )
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

/**
 * This may be needed.
 * Some unique bug.
 * Thanks gradle/IntelliJ
 * See: https://stackoverflow.com/questions/64290545/task-preparekotlinbuildscriptmodel-not-found-in-project-app
 */
tasks.register("prepareKotlinBuildScriptModel") {}

