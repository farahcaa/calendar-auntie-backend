plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("com.google.cloud.tools.jib") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

version = providers.gradleProperty("apiVersion").get()

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")

    implementation("org.hibernate.orm:hibernate-core:6.6.2.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.2.Final")

    testImplementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("com.stripe:stripe-java:30.0.0")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("io.minio:minio:8.6.0")

}

jib {
    from {
        image = "eclipse-temurin:21-jre"
    }
    to {
        image = "ghcr.io/jaga/api:${project.version}"
        tags = setOf("${project.version}", "latest")
    }
    container {
        ports = listOf("8081")
        jvmFlags = listOf("-XX:MaxRAMPercentage=75")
        user = "1000:1000"
        environment = mapOf("SPRING_PROFILES_ACTIVE" to "prod")
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