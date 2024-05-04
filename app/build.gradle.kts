plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    // kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("java")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("org.json:json:20140107")
    shadow("junit:junit:3.8.2")
    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
}

application {
    // Define the main class for the application.
    mainClass.set("ollama.Ollama")
}

tasks.shadowJar {
    // Configure the shadow jar task as needed
    // For example, change the name of the output jar:
    archiveBaseName.set("App")
    archiveClassifier.set("")
    useJUnitPlatform
    // If you want to exclude any dependencies, you can do it like this:
    // dependencies {
    //     exclude("group:name")
    // }
}

// tasks.named<Jar>("jar") {
//     // Change the name of the regular JAR file
//     archiveBaseName.set("App")
// }

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
