plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("java")
    id("maven-publish")
}
group = "my.jars.AI"
version = "1.0"
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
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/RanchMonster/Ollama")
            credentials {
                username = project.findProperty("gpr.user") as String? ?:System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?:System.getenv("TOKEN")
            }
        }
    }
    publications {
        gpr<MavenPublication>("Ollama") {
            from(components["java"])
        }
    }
}

task.writeNewPom {
    doLast {
        pom {
            project {
                groupId ="org.example"
                artifactId= "test"
                version ='1.0.0'
                inceptionYear '2008'
                licenses {
                    license {
                        name 'The Apache Software License, Version 2.0'
                        url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                        distribution 'repo'
                    }
                }
            }
        }.writeTo("pom.xml")
    }
}
application {
    mainClass.set("ollama.Ollama")
}

tasks.shadowJar {
    // archiveBaseName.set("App")
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
