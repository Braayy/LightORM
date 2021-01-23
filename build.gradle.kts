plugins {
    id("java")
    id("maven-publish")

    id("com.github.johnrengelman.shadow") version "6.1.0"
}

val groupPrefix = "com.jpereirax.lightorm"
val pVersion = "0.1.2-BETA"

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
        val lombokVersion = "1.18.16"
        val junitVersion = "5.7.0"
        val junitPlatformVersion = "1.7.0"

        implementation("org.projectlombok:lombok:$lombokVersion")
        annotationProcessor("org.projectlombok:lombok:$lombokVersion")

        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        testImplementation("org.junit.vintage:junit-vintage-engine:$junitVersion")
        testImplementation("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
    }

    tasks {
        test {
            useJUnitPlatform()
        }

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