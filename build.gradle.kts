plugins {
    // You can put conventions here if all modules need them
    id("org.springframework.boot") version "3.5.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.google.cloud.tools.jib") version "3.4.3" apply false
}

allprojects {
    group = "org.jaga"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    // Common Java toolchain for all modules
    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
