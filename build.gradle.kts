import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val revision: String? = System.getenv("GITHUB_RUN_NUMBER")

group = "org.rescado"
version = "1.0.0-${if (revision.isNullOrBlank()) "SNAPSHOT" else "r$revision"}"
java.sourceCompatibility = JavaVersion.VERSION_11

// Sources
repositories {
    mavenCentral()
}

// Plugins
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
}

// Dependencies
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Hibernate
    implementation("org.hibernate:hibernate-core:5.5.7.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
    implementation("org.hibernate.validator:hibernate-validator-annotation-processor:6.2.0.Final")
    runtimeOnly("org.postgresql:postgresql")
    // Liquibase
    implementation("org.liquibase:liquibase-core:4.5.0")
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // YAUAA
    implementation("nl.basjes.parse.useragent:yauaa:5.19")
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
}

// Spring Boot config
springBoot {
    buildInfo() // make available at runtime
}

// Simple task that will copy project Git hooks to the .git directory
tasks.register<Copy>("installGitHooks") {
    from(".github/hooks")
    into(".git/hooks")
}

// Alternative command to run app with "prod" as active Spring profile
tasks.register("bootRunProd") {
    group = "application"
    description = "Runs this project as a Spring Boot application with the prod profile"
    // Running this via IntelliJ is bugged:
    // - You cannot debug this: breakpoints are ignored (maybe related https://youtrack.jetbrains.com/issue/IDEA-119494)
    // - Stopping process outputs process stopped but that's a lie: process just keeps running and you'll need to kill it
    // TODO find another way to define a "bootRunDev" task, so "bootRun" can be used for prod config.
    doFirst {
        tasks.bootRun.configure {
            systemProperty("spring.profiles.active", "prod")
        }
    }
    finalizedBy("bootRun")
}

// Kotlin compiler arguments and chaining installGitHooks task
tasks.withType<KotlinCompile> {
    kotlinOptions {
        apiVersion = "1.5" // Kotlin version
        languageVersion = "1.5" // Kotlin version
        jvmTarget = "11" // JVM version
        freeCompilerArgs = listOf("-Xjsr305=strict") // strict null-safety
    }
    finalizedBy("installGitHooks") // install most recent Git hooks
}

// Use JUnit for tests
tasks.withType<Test> {
    useJUnitPlatform()
}
