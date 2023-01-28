plugins {
    java
    kotlin("jvm")
//    id("com.github.johnrengelman.shadow") version libs.versions.shadow
    `maven-publish`
    `java-library`
}

version = "1.0.13"


//publishing {
//    publications {
//        create<MavenPublication>("shadow") {
//            project.extensions.configure<com.github.jengelman.gradle.plugins.shadow.ShadowExtension>() {
//                component(this@create)
//            }
//        }
//    }
//    repositories {
//        maven {
//            url = uri("https://gitlab.hyneo.ru/api/v4/projects/36/packages/maven")
//            credentials(HttpHeaderCredentials::class) {
//                name = "Private-Token"
//                value =  System.getenv("TOKEN")// the variable resides in $GRADLE_USER_HOME/gradle.properties
//            }
//            authentication {
//                create("header", HttpHeaderAuthentication::class)
//            }
//        }
//    }
//}

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

//    prepareSpigotPlugins{
//        dependsOn(shadowJar)
//    }
//
//    generateSpigotDescription{
//        enabled = false
//    }

    processResources {
        filesMatching("*.json") {
            expand(project.properties)
        }
    }

//    shadowJar {
//        archiveClassifier.set("")
////        minimize()
//        exclude("**/*.kotlin_metadata")
////        exclude("**/*.kotlin_module")
////        exclude("**/*.kotlin_builtins")
//    }
}

dependencies {

    implementation(kotlin("stdlib"))
    //ебал я в врот ваш netty пошел нахуй
    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.stub.kotlin)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.protobuf.kotlin)

    implementation(libs.configlib.yaml)

//    implementation(libs.jedis)
    implementation("org.redisson:redisson:3.19.1")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation(libs.trove4j)
// https://mvnrepository.com/artifact/org.yaml/snakeyaml
    compileOnly("org.yaml:snakeyaml:1.33")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    //caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.2")
    implementation("com.github.ben-manes.caffeine:guava:3.1.2")
//    implementation("com.github.ben-manes.caffeine:jcache:3.1.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
