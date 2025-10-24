plugins {
    id("dev.isxander.modstitch.base") version "0.6.4-unstable"
}

val loader = when {
    modstitch.isLoom -> "fabric"
    modstitch.isModDevGradle -> "neoforge"
    else -> error("Unknown loader")
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val mcVersion = property("deps.minecraft") as String

modstitch {
    minecraftVersion = mcVersion

    // Alternatively use stonecutter.eval if you have a lot of versions to target.
    // https://stonecutter.kikugie.dev/stonecutter/guide/setup#checking-versions
    javaVersion = if (stonecutter.eval(mcVersion, ">1.20.4")) 21 else 17

    // If parchment doesn't exist for a version, yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = "deote"
        modName = "Disable Elytra Outside The End"
        modVersion = "2.0.0"
        modGroup = "io.github.timtaran.deote"
        modAuthor = "timtaran"
        modLicense = "LGPL-3.0-or-later"

        fun <K, V> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", "https://github.com/Timtaran/disable-elytra-outside-the-end")
            put("pack_format", when (property("deps.minecraft")) {
                "1.20.1" -> 15
                "1.20.4" -> 22
                "1.21.1" -> 34
                "1.21.4" -> 46
                "1.21.8" -> 64
                "1.21.10" -> 69
                else -> throw IllegalArgumentException("Please store the resource pack version for ${property("deps.minecraft")} in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
            }.toString())
            put("yacl_version", "${property("deps.yacl")}-${loader}")
        }
    }

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure it's up to date.
        fabricLoaderVersion = "0.17.3"

        // Configure loom like normal in this block.
        configureLoom {

        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.neoforge") { neoForgeVersion = it }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()
    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

        configs.register("deote")

        // Most of the time you won't ever need loader specific mixins.
        // If you do, simply make the mixin file and add it like so for the respective loader:
        // if (isLoom) configs.register("examplemod-fabric")
        // if (isModDevGradleRegular) configs.register("examplemod-neoforge")
        // if (isModDevGradleLegacy) configs.register("examplemod-forge")
    }
}

stonecutter {
    constants {
        put("fabric", modstitch.isLoom)
        put("neoforge", modstitch.isModDevGradleRegular)
        put("forge", modstitch.isModDevGradleLegacy)
        put("forgelike", modstitch.isModDevGradle)
    }
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    modstitch.loom {
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    }

    modstitchModImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}-${loader}") {
        exclude(group = "thedarkcolour")
    }
}