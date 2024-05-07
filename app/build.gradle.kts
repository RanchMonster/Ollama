plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("java")
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("org.json:json:20140107")
    shadow("junit:junit:3.8.2")
    implementation("com.google.guava:guava:31.1-jre")
}

plugins {
}
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/RanchMonster/Ollama")
        credentials {
            username = "RanchMonster"
            password = "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"
        }
    }
}

application {
    mainClass.set("ollama.Ollama")
}

tasks.shadowJar {
    archiveBaseName.set("Ollama")
    archiveClassifier.set("")
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.named<Test>("test") {
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
