description = "This library is an extension to the great jOOλ library"


plugins {
    `java-library`
    id("com.bakdata.release") version "1.10.0"
    id("com.bakdata.sonar") version "1.10.0"
    id("com.bakdata.sonatype") version "1.10.0"
    id("io.freefair.lombok") version "8.14"
}

group = "com.bakdata.${rootProject.name}"

tasks.withType<Test> {
    maxParallelForks = 4
}

repositories {
    mavenCentral()
}

publication {
    developers {
        developer {
            name.set("Philipp Schirmer")
            id.set("philipp94831")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

dependencies {
    api(group = "org.jooq", name = "jool", version = "0.9.15")

    val junitVersion = "5.13.3"
    testRuntimeOnly(group = "org.junit.platform", name = "junit-platform-launcher")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = junitVersion)
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.27.3")
}
