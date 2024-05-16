plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("java")
    id("maven-publish")
    id("org.moditect.gradleplugin") version "1.0.0.RC1"
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
        create<MavenPublication>("maven") {
            groupId = "my.jars"
            artifactId = "ollama"
            version = "1.3"
            from(components["java"])
            artifact(tasks.named("shadowJar")) {
                classifier = null
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/RanchMonster/Ollama")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: "RanchMonster"
                password = project.findProperty("gpr.key") as String? ?: "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"
            }
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

tasks.register<Copy>("copyLibs") {
    from(configurations.runtimeClasspath)
    into("libs")
}

moditect {
    addModuleInfo {
        module {
            moduleInfoFile.set(file("src/main/module-info.java"))
            targetJar.set(file("libs/Ollama-1.3.jar"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn("copyLibs")
}
