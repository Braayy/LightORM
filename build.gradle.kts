plugins {
    java
    `maven-publish`

    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.jpereirax"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.zaxxer:HikariCP:3.4.5")

    implementation("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact("$buildDir/libs/${rootProject.name}-$version-all.jar")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}