/*
блять просто помогает брать спокойно из воздуха spigot и bungee-cord,
но если блять автор не обновит базу данных и придется переписывать весь код,
то он пидарас
*/
import kr.entree.spigradle.kotlin.*

plugins {
    java
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version libs.versions.shadow
    id("kr.entree.spigradle") version libs.versions.spigradle
    `maven-publish`
    `java-library`
}

version = System.getenv("BUILD_NUMBER")

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
            credentials {
                username=System.getenv("NEXUS_USR")
                password=System.getenv("NEXUS_PSW")
            }
            url=uri("https://registry.hyneo.ru/repository/maven-releases/")
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

//compileJava.options.encoding = 'UTF-8'
//compileTestJava.options.encoding = 'UTF-8'
//targetCompatibility = '17'
//sourceCompatibility = '17'


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_builtins")
//        dependencies{
//            exclude(libs.grpc.protobuf)
//        }
    }
}


val cloudVersion = "1.8.0"
dependencies {

    implementation(kotlin("stdlib"))
    //ебал я в врот ваш netty пошел нахуй
    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.stub.kotlin)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.protobuf.kotlin)

    implementation(libs.configlib.yaml)

    implementation(libs.jedis)

    implementation(libs.trove4j)

    implementation("cloud.commandframework", "cloud-core", cloudVersion)
    implementation("cloud.commandframework", "cloud-annotations", cloudVersion)
//    kapt("cloud.commandframework", "cloud-annotations", cloudVersion)
    implementation("cloud.commandframework", "cloud-velocity", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-extensions", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-coroutines-annotations", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-coroutines", cloudVersion)

//    implementation(libs.litecommands.core)
//    implementation(libs.litecommands.velocity)
//    implementation(libs.litecommands.bukkit)

    implementation(libs.guava)

    implementation(libs.inventory.framework)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.velocity)

    compileOnly(spigot("1.15.2"))




    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-api:2.8.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-core:2.8.0")
//    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.8.0")
//    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.8.0")

}
