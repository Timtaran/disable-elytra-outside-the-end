import java.util.Locale

fun String.capitalized() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

plugins {
    id("dev.isxander.modstitch.base") version "0.8.2"

    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
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
val modMetaVersion = property("mod.metadata.version") as String

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
        modId = property("mod.metadata.id") as String
        modName = property("mod.metadata.name") as String
        modVersion = "$modMetaVersion-$loader+mc$mcVersion"
        modGroup = property("mod.metadata.group") as String
        modAuthor = property("mod.metadata.author") as String
        modLicense = property("mod.metadata.license") as String

        fun <K : Any, V : Any> MapProperty<K, V>.populate(
            block: MapProperty<K, V>.() -> Unit
        ) {
            block()
        }


        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", property("mod.metadata.issue_tracker") as String)
            put("pack_format", when (property("deps.minecraft")) {
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
        modstitchModImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    }

    modstitchModImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}-${loader}") {
        exclude(group = "thedarkcolour")
    }
}

val finalJarTasks = listOf(modstitch.finalJarTask)

val buildAndCollect by tasks.registering(Copy::class) {
    group = "publishing"
    description = "Collect final mod JAR(s) into build/finalJars"
    finalJarTasks.forEach { jar ->
        dependsOn(jar)
        from(jar.flatMap { it.archiveFile })
    }
    into(rootProject.layout.buildDirectory.dir("finalJars"))
}

val releaseModVersion by tasks.registering {
    group = "publishing"
    description = "Runs publishMods (Modrinth)."
    dependsOn("publishMods")
}

/* publishMods (mod-publish-plugin) — Modrinth-only configuration */
publishMods {
    // Control dry run with -PpublishDryRun=false (default = true)
    dryRun = findProperty("publishDryRun")?.toString()?.toBoolean() ?: true

    // Use the final mod JAR produced by modstitch
    file = modstitch.finalJarTask.flatMap { it.archiveFile }

    // friendly name shown in logs / UI
    displayName = "[${loader.capitalized()}] ${modstitch.metadata.modName.get()} for $mcVersion"

    // add loader tag (fabric/neoforge/etc)
    modLoaders.add(loader)

    fun getChangelogForVersion(changelogText: String, version: String): String? {
        // looking for "# v2.1.0" or "# 2.1.0"
        val escapedVersion = Regex.escape(version)
        val regex = Regex("(?m)(?s)^#\\s*v?$escapedVersion\\s*\\r?\\n(.*?)(?=^#\\s*v?\\d|\\z)")
        return regex.find(changelogText)?.groups?.get(1)?.value?.trim()
    }

    type = BETA
    changelog = getChangelogForVersion(rootProject.file("CHANGELOG.md").readText(), modMetaVersion)

    logger.lifecycle(changelog.toString())

    fun versionList(prop: String) = findProperty(prop)?.toString()
        ?.split(',')
        ?.map { it.trim() }
        ?: emptyList()

    val stableMCVersions = versionList("pub.stableMC")

    // ---- MODRINTH ----
    val modrinthId: String? = findProperty("modrinth.id")?.toString()
    if (!modrinthId.isNullOrBlank() && hasProperty("modrinth.token")) {
        modrinth {
            // project id or slug — set in gradle.properties as modrinthId
            projectId.set(modrinthId)

            // access token — set in gradle.properties as modrinth.token (or pass -Pmodrinth.token=...)
            accessToken.set(findProperty("modrinth.token")?.toString())

            // tags for which Minecraft versions the file will be listed under
            minecraftVersions.addAll(stableMCVersions)
            minecraftVersions.addAll(versionList("pub.modrinthMC"))

            // optional text used in announcements
            announcementTitle = "Download $mcVersion for ${loader.replaceFirstChar { it.uppercase() }} from Modrinth"

            // example of declaring dependencies (change/remove as needed)
            requires { slug.set("yacl") }
            if (modstitch.isLoom) {
                requires { slug.set("fabric-api") }
                optional { slug.set("modmenu") }
            }
        }
    } else {
        // helpful log when disabled
        logger.lifecycle("Modrinth publishing disabled — set 'modrinth.id' and 'modrinth.token' to enable.")
    }
}