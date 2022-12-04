rootProject.name = "hyneo-minecraft"
include("api")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement{
    versionCatalogs{
        create("libs"){
            //plugin
            version("kotlin", "1.8.0-Beta")
            version("shadow", "7.1.2")
            version("spigradle", "2.4.2")
            //minecraft
            version("spigot", "1.15.2")
            version("velocity_api", "3.0.1")
            version("litecommands", "2.5.0")
            version("configlib_yaml", "v4.2.0")
            version("inventory_framework", "2.5.4-rc.1")
            //grpc
            version("grpcVersion", "1.47.0-SNAPSHOT")
            version("grpc", "1.49.0")
            version("grpc-lite", "1.51.0")
            version("grpc-kotlin", "1.3.0")
            version("protobuf_java_util", "3.21.5")
            version("protobufPluginVersion", "0.8.18")
            version("protobufVersion", "3.19.2")
            //etc
            version("trove4j", "3.0.3")
            version("guava", "31.1-jre")
            version("lombok", "1.18.24")
            version("jedis", "4.2.3")

            alias("grpc-okhttp").to("io.grpc", "grpc-okhttp").versionRef("grpc")
            alias("grpc-stub").to("io.grpc", "grpc-stub").versionRef("grpc")
            alias("grpc-stub-kotlin").to("io.grpc", "grpc-kotlin-stub").versionRef("grpc-kotlin")
            alias("grpc-protobuf").to("io.grpc", "grpc-protobuf-lite").versionRef("grpc-lite")
            alias("grpc-protobuf-kotlin").to("com.google.protobuf", "protobuf-kotlin").versionRef("protobuf_java_util")

            alias("configlib-yaml").to("com.github.Exlll.ConfigLib", "configlib-yaml").versionRef("configlib-yaml")
            alias("jedis").to("redis.clients", "jedis").versionRef("jedis")
            alias("trove4j").to("net.sf.trove4j", "trove4j").versionRef("trove4j")

            alias("litecommands-core").to("dev.rollczi.litecommands", "core").versionRef("litecommands")
            alias("litecommands-velocity").to("dev.rollczi.litecommands", "velocity").versionRef("litecommands")
            alias("litecommands-bukkit").to("dev.rollczi.litecommands", "bukkit").versionRef("litecommands")

            alias("guava").to("com.google.guava", "guava").versionRef("guava")

            alias("inventory-framework").to("com.github.DevNatan.inventory-framework", "inventory-framework").versionRef("inventory_framework")

            alias("lombok").to("org.projectlombok", "lombok").versionRef("lombok")

            alias("velocity").to("com.velocitypowered", "velocity-api").versionRef("velocity_api")
        }
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.screamingsandals.org/public/")
    }
}


