description = "This library is an extension to the great jOOλ library"


plugins {
    id("net.researchgate.release") version "2.8.1"
    id("com.bakdata.sonar") version "1.1.4"
    id("com.bakdata.sonatype") version "1.1.4"
    id("org.hildan.github.changelog") version "0.8.0"
    id("io.freefair.lombok") version "3.8.0"
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

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        "api"(group = "org.jooq", name = "jool", version = "0.9.14")

        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.5.1")
        "testImplementation"("org.junit.jupiter:junit-jupiter-params:5.5.1")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.5.1")
        "testImplementation"(group = "org.assertj", name = "assertj-core", version = "3.13.2")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
