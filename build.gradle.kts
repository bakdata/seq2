description = "This library is an extension to the great jOOλ library"


plugins {
    `java-library`
    id("net.researchgate.release") version "3.0.2"
    id("com.bakdata.sonar") version "1.1.17"
    id("com.bakdata.sonatype") version "1.1.14"
    id("org.hildan.github.changelog") version "2.2.0"
    id("io.freefair.lombok") version "8.4"
}

allprojects {
    group = "com.bakdata.${rootProject.name}"

    tasks.withType<Test> {
        maxParallelForks = 4
    }

    repositories {
        mavenCentral()
    }
}

configure<com.bakdata.gradle.SonatypeSettings> {
    developers {
        developer {
            name.set("Philipp Schirmer")
            id.set("philipp94831")
        }
    }
}

configure<org.hildan.github.changelog.plugin.GitHubChangelogExtension> {
    githubUser = "bakdata"
    futureVersionTag = findProperty("changelog.releaseVersion")?.toString()
    sinceTag = findProperty("changelog.sinceTag")?.toString()
}

allprojects {
    apply(plugin = "java-library")

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(11)
        }
    }

    dependencies {
        api(group = "org.jooq", name = "jool", version = "0.9.14")

        val junitVersion = "5.9.2"
        testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = junitVersion)
        testImplementation(group = "org.assertj", name = "assertj-core", version = "3.24.2")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

release {
    git {
        requireBranch.set("master")
    }
}
