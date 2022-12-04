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
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
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
    shadowJar {
        archiveClassifier.set("")
//        minimize()
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_builtins")
    }
}



dependencies {
    //ебал я в врот ваш netty пошел нахуй
    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.stub.kotlin)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.protobuf.kotlin)

    implementation(libs.configlib.yaml)

    implementation(libs.jedis)

    implementation(libs.trove4j)

    implementation(libs.litecommands.core)
    implementation(libs.litecommands.velocity)
    implementation(libs.litecommands.bukkit)

    implementation(libs.guava)

    implementation(libs.inventory.framework)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(spigot("1.15.2"))

    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-api:2.7.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-core:2.7.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.7.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.7.0")

}
