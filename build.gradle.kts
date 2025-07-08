val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.dan_bat"
version = "1.0-SNAPSHOT"

val koolVersion = "0.17.0"
val lwjglVersion = "3.3.6"
val physxJniVersion = "2.4.0"
val targetPlatforms = listOf("natives-windows", "natives-linux", "natives-macos", "natives-macos-arm64")

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

application {
    mainClass = "ru.dan_bat.MainKt"
}

dependencies {
    implementation("de.fabmax.kool:kool-core:$koolVersion")
    implementation("de.fabmax.kool:kool-physics:$koolVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    for (platform in targetPlatforms) {

        runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$platform")
        listOf("glfw", "opengl", "jemalloc", "nfd", "stb", "vma", "shaderc").forEach { lib ->
            runtimeOnly("org.lwjgl:lwjgl-$lib:$lwjglVersion:$platform")
        }

        runtimeOnly("de.fabmax:physx-jni:$physxJniVersion:$platform")
    }

}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("myapp-all")
    archiveClassifier.set("")
    archiveVersion.set("1.0.0")
}

kotlin {
    jvmToolchain(21)
}

task("runnableJar", Jar::class) {
    dependsOn("jvmJar")

    group = "app"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveAppendix.set("runnable")
    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    configurations
        .asSequence()
        .filter { it.name.startsWith("common") || it.name.startsWith("jvm") }
        .map { it.copyRecursive().fileCollection { true } }
        .flatten()
        .distinct()
        .filter { it.exists() }
        .map { if (it.isDirectory) it else zipTree(it) }
        .forEach { from(it) }
    from(layout.buildDirectory.files("classes/kotlin/jvm/main"))

    doLast {
        copy {
            from(layout.buildDirectory.file("libs/${archiveBaseName.get()}-runnable.jar"))
            into("${rootDir}/dist/jvm")
        }
    }
}

task("runApp", JavaExec::class) {
    group = "app"
    dependsOn("jvmMainClasses")

    classpath = layout.buildDirectory.files("classes/kotlin/jvm/main")
    configurations
        .filter { it.name.startsWith("common") || it.name.startsWith("jvm") }
        .map { it.copyRecursive().filter { true } }
        .forEach { classpath += it }

    mainClass.set("LauncherKt")
}

val build by tasks.getting(Task::class) {
    dependsOn("runnableJar")
}

val clean by tasks.getting(Task::class) {
    doLast {
        delete("${rootDir}/dist")
    }
}