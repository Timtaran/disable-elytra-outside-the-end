package io.github.timtaran.deote.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import io.github.timtaran.deote.util.TextUtils;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import io.github.timtaran.deote.util.DeoteIdentifier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
        //?} elif neoforge {
/*import net.neoforged.fml.loading.FMLPaths;
 *///?}

import java.nio.file.Path;

public class DeoteConfig {
    public static ConfigClassHandler<DeoteConfig> HANDLER = ConfigClassHandler.createBuilder(DeoteConfig.class)
            .id(DeoteIdentifier.get("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(getConfigDirectory().resolve("deote.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();


    @SerialEntry
    public WorkingMode workingMode = WorkingMode.FLYING;

    private static Path getConfigDirectory() {
        //? if fabric {
        return FabricLoader.getInstance().getConfigDir();
        //?} elif neoforge {
        /*return FMLPaths.CONFIGDIR.get();
         *///?}
    }

    public Screen getConfigScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Disable Elytra Outside The End"))
                .category(ConfigCategory.createBuilder()
                        .name(TextUtils.translatable("config.categories.main"))
                        .tooltip(TextUtils.translatable("config.categories.main.tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(TextUtils.translatable("config.groups.main"))
                                .option(Option.<WorkingMode>createBuilder()
                                        .name(TextUtils.translatable("config.groups.main.working_mode"))
                                        .description(OptionDescription.of(TextUtils.translatable("config.groups.main.working_mode.description")))
                                        .binding(
                                                WorkingMode.NONE,
                                                () -> this.workingMode,
                                                newVal -> this.workingMode = newVal
                                        )
                                        .controller(opt -> EnumControllerBuilder.create(opt)
                                                .enumClass(WorkingMode.class))
                                        .build()
                                )
                                .build()
                        )
                        .build())
                .save(HANDLER::save)
                .build().generateScreen(parentScreen);
    }
}
