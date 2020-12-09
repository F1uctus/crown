plugins {
    java
    `maven-publish`
}

group = "com.github.f1uctus"
version = "0.10.0"
description = "crown"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://repo.minestom.com/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("org.springframework:spring-core:5.2.4.RELEASE")
    implementation("io.github.kostaskougios:cloning:1.10.3")
    implementation("de.articdive:jnoise:2.0.0-SNAPSHOT")
    implementation("org.mini2Dx:gdx-math:1.9.11")
    implementation("org.jetbrains:annotations:20.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/f1uctus/crown")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}