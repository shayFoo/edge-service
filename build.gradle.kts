import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.cloud.tools.jib") version "3.4.5"
}

group = "com.polarbookshop"
version = "0.0.1-SNAPSHOT"
description = "API gateway adn cross-cutting-concerns."

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"
extra["testcontainers.version"] = "1.21.3"
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux:4.3.1")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.micrometer:micrometer-tracing-bridge-otel")
    runtimeOnly("io.opentelemetry:opentelemetry-exporter-otlp")
    runtimeOnly("io.github.resilience4j:resilience4j-micrometer")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainers.version")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
    environment = mapOf(
        "BP_JVM_VERSION" to "25",
        "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
        "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-Duser.timezone=Asia/Tokyo",
        "LANG" to "ja_JP.UTF-8",
        "LANGUAGE" to "ja_JP:ja",
        "LC_ALL" to "ja_JP.UTF-8",
    )
    imageName = project.name + ":" + project.version
    docker {
        publishRegistry {
            username = project.findProperty("registryUsername")?.toString()
            password = project.findProperty("registryToken")?.toString()
            url = project.findProject("registryUrl")?.toString()
        }
    }
}

// Jib settings for local development.
jib {
    from {
        image = "amazoncorretto:25"
    }
    to {
        image = project.name + ":" + project.version
    }
    container {
        jvmFlags = listOf("-Duser.timezone=Asia/Tokyo")
        user = "1000"
        environment = mapOf(
            "LANG" to "ja_JP.UTF-8",
            "LANGUAGE" to "ja_JP:ja",
            "LC_ALL" to "ja_JP.UTF-8",
        )
        workingDirectory = "/workspace"
        mainClass = "com.polarbookshop.edge_service.EdgeServiceApplication"
        ports = listOf("9001")
    }
}

