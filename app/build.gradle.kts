plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    // kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("java")
    id("maven-publish")
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
    // maven("maven-publish")
    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
}
publishing {
    publications {
        create<MavenPublication>("gpr") {
            run {
                groupId = "com.enefce.libraries"
                artifactId = getArtificatId()
                version = getVersionName()
                artifact("$buildDir/outputs/aar/${getArtificatId()}-release.aar")
            }
        }
    }
}
repositories{
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/RanchMonster/Ollama")
      credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
            }
    }
}
application {
    // Define the main class for the application.
    mainClass.set("ollama.Ollama")
}

tasks.shadowJar {
    // Configure the shadow jar task as needed
    // For example, change the name of the output jar:
    archiveBaseName.set("Ollama")
    archiveClassifier.set("")
    // If you want to exclude any dependencies, you can do it like this:
    // dependencies {
    //     exclude("group:name")
    // }
}

tasks.named<Jar>("jar") {
    // Change the name of the regular JAR file
    enabled= false
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
tasks.named("distZip") {
    dependsOn(":App:shadowJar")
}

tasks.named("distTar") {
    dependsOn(":App:shadowJar")
}

tasks.named("startScripts") {
    dependsOn(":App:shadowJar")
}