plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active file("versions/current")

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")

        // YACL
        maven("https://maven.isxander.dev/releases/")

        // Mod Menu
        maven("https://maven.terraformersmc.com/")
        maven("https://maven.nucleoid.xyz/") // Text Placeholder API
    }
}