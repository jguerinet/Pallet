object Version {
    const val teamCityDsl = "2019.2"
    const val teamCityPlugins = "1.0-SNAPSHOT"
}

plugins {
    java
    kotlin("jvm") version "1.3.61"
    maven
    id("com.github.ben-manes.versions") version "0.28.0"
}

apply(from = "https://raw.githubusercontent.com/jguerinet/Gradle-Artifact-Scripts/master/spotless.gradle")

group = "com.guerinet"
version = "5.2.0"

buildscript {

    repositories {
        jcenter()
    }

    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:3.27.1")
    }
}

repositories {
    mavenCentral()
    maven("http://download.jetbrains.com/teamcity-repository")
    maven("https://teamcity.jetbrains.com/app/dsl-plugins-repository")
    maven("https://teamcity.internal.company.com/app/dsl-plugins-repository")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin:${Version.teamCityDsl}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-bundled:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-commandLineRunner:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-commit-status-publisher:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-gradle:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-jetbrains.git:${Version.teamCityPlugins}")
    implementation("org.jetbrains.teamcity:configs-dsl-kotlin-pull-requests:${Version.teamCityPlugins}")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

apply(from = "https://raw.githubusercontent.com/jguerinet/Gradle-Artifact-Scripts/master/kotlin-artifacts.gradle")
