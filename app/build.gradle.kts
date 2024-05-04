/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.0.2/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    // kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("org.json:json:20140107")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
}

application {
    // Define the main class for the application.
    mainClass.set("ollama.Ollama")
}

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

tasks.getByName<ShadowJar>("shadowJar") {
    // Configure the shadow jar task as needed
    // For example, change the name of the output jar:
    archiveFileName.set("your-app-all.jar")
}
tasks.withType<Jar> {
    archiveBaseName.set("App")
}
shadowJar {
}
tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
    
}

