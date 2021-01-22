plugins {
    id("java")
    id("maven-publish")

    id("com.github.johnrengelman.shadow") version "6.1.0"
}

val groupPrefix = "com.jpereirax.lightorm"
val pVersion = "0.1.0-BETA"

group = groupPrefix
version = pVersion

subprojects {
    plugins.apply("java")
    plugins.apply("maven-publish")
    plugins.apply("com.github.johnrengelman.shadow")

    group = groupPrefix
    version = pVersion

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.projectlombok:lombok:1.18.16")
        annotationProcessor("org.projectlombok:lombok:1.18.16")
    }

    tasks {
        shadowJar {
            archiveFileName.set("lightorm-${project.name}-$pVersion.jar")
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifact("$buildDir/libs/lightorm-${project.name}-$pVersion.jar")
            }
        }
    }
}