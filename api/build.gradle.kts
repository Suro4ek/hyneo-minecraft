/*
блять просто помогает брать спокойно из воздуха spigot и bungee-cord,
но если блять автор не обновит базу данных и придется переписывать весь код,
то он пидарас
*/
import kr.entree.spigradle.kotlin.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version libs.versions.shadow
    id("kr.entree.spigradle") version libs.versions.spigradle
    `maven-publish`
    `java-library`
}

version = "1.0.8"


publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.extensions.configure<com.github.jengelman.gradle.plugins.shadow.ShadowExtension>() {
                component(this@create)
            }
        }
    }
    repositories {
        maven {
            url = uri("https://gitlab.hyneo.ru/api/v4/projects/36/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value =  System.getenv("TOKEN")// the variable resides in $GRADLE_USER_HOME/gradle.properties
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }
        }
    }
}

//plugin.yml
spigot {
    val authors1 = ArrayList<String>()
    authors1.add("Suro")
    authors = authors1
    load = kr.entree.spigradle.data.Load.STARTUP
}

val gitlabToken = if (System.getenv("CI_TOKEN") != null) {
    System.getenv("CI_TOKEN")
} else {
    findProperty("gitLabPrivateToken")
}

repositories{
    maven {
        url = uri( "https://gitlab.hyneo.ru/api/v4/groups/39/-/packages/maven")
        credentials(HttpHeaderCredentials::class) {
            name = "Private-Token"
            value = "$gitlabToken"
        }
        authentication {
            create("header", HttpHeaderAuthentication::class)
        }
    }
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.javaParameters = true
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileJava{
        options.encoding =  "UTF-8"
    }

    prepareSpigotPlugins{
        dependsOn(shadowJar)
    }

    generateSpigotDescription{
        enabled = false
    }

    processResources {
        filesMatching("*.json") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveClassifier.set("")
//        minimize()
        exclude("**/*.kotlin_metadata")
//        exclude("**/*.kotlin_module")
//        exclude("**/*.kotlin_builtins")
    }
}

dependencies {

    implementation(kotlin("stdlib"))
    //ебал я в врот ваш netty пошел нахуй
    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.stub.kotlin)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.protobuf.kotlin)

//    implementation("eu.suro.command-velocity:command-velocity:1.0.0")
    implementation(libs.configlib.yaml)

    implementation(libs.jedis)

    implementation(libs.trove4j)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    implementation(libs.guava)

    implementation(libs.inventory.framework)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.velocity)

    implementation("eu.suro.command:command-velocity:1.0.4")

    compileOnly(spigot("1.15.2"))

//    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.3.3")
//    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-hocon", "1.3.3")

    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-api:2.9.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-core:2.9.0")

}
