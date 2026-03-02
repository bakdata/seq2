description = "This library is an extension to the great jOOλ library"


plugins {
    `java-library`
    alias(libs.plugins.release)
    alias(libs.plugins.sonar)
    alias(libs.plugins.sonatype)
    alias(libs.plugins.lombok)
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
    api(libs.jool)

    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
}
