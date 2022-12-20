import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version libs.versions.kotlin
}

subprojects {

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.panda-lang.org/releases")
        maven {
            name="papermc"
            url = uri("https://repo.papermc.io/repository/maven-public")
        }
    }

//    tasks.withType<ProcessResources> {
//        filesMatching(listOf("*.yml", "*.json")) {
//            expand(project.properties)
//        }
//    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
}


