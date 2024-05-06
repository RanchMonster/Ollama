plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("java")
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = "myjars"
            artifactId = "Ollama" // Set your artifactId
            version = "1.0.0" // Set your version
            artifact(tasks.shadowJar.get().archiveFile)
        }
    }
}
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/RanchMonster/Ollama.git")
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
