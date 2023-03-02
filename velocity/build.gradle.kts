 /*
блять просто помогает брать спокойно из воздуха spigot и bungee-cord,
но если блять автор не обновит базу данных и придется переписывать весь код,
то он пидарас
*/
plugins {
    java
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version libs.versions.shadow
    `maven-publish`
    `java-library`
}

version = "1.0.18"

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

    processResources {
        filesMatching("*.json") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        exclude("**/*.kotlin_metadata")
    }
}

dependencies {
    implementation(project(":api"))

    compileOnly(libs.velocity)
    implementation("eu.suro.command:command-velocity:1.0.9")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-api:2.9.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-core:2.9.0")

}
