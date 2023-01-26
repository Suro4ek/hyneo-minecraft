/*
блять просто помогает брать спокойно из воздуха spigot,
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

version = "1.0.12"


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
    name = "hncapi"
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

    shadowJar {
        archiveClassifier.set("")
        exclude("**/*.kotlin_metadata")
    }
}

dependencies {

    implementation(libs.inventory.framework)

    compileOnly(spigot("1.15.2"))
    implementation(project(":api"))


    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.9.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.9.0")

}
