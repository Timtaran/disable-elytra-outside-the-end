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
        maven("https://maven.isxander.dev/releases/")
    }
}