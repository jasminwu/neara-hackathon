import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.example.RizzlerKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/dev.robocode.tankroyale/robocode-tankroyale-bot-api
    implementation("dev.robocode.tankroyale:robocode-tankroyale-bot-api:0.19.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("Rizzler.jar")

    doLast {
        copy {
            from("src/main/kotlin/org/example/Rizzler.json")
            from("$buildDir/libs/Rizzler.jar")
            into("Rizzler")
        }
    }

}

tasks.clean {
    delete ("Rizzler/Rizzler.json")
    delete ("Rizzler/Rizzler.jar")
}

kotlin {
    jvmToolchain(11)
}
