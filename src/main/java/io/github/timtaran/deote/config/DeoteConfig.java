/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.util.TextUtils;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import io.github.timtaran.deote.commands.ConfigProvider;
import io.github.timtaran.deote.util.DeoteIdentifier;
// import net.minecraft.client.Minecraft;  // causes java.lang.RuntimeException: Cannot load class net.minecraft.client.server.IntegratedServer in environment type SERVER
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
// import net.minecraft.server.MinecraftServer;
//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} elif neoforge {
/*import net.neoforged.fml.loading.FMLPaths;
 *///?}

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The main config class for the mod.
 *
 * @author timtaran
 */
public class DeoteConfig {
    private static final WorkingMode DEFAULT_WORKING_MODE = WorkingMode.FLYING;
    private static final ArrayList<String> DEFAULT_DIMENSION_LIST = new ArrayList<>(List.of(new String[]{"minecraft:the_end"}));
    private static final boolean DEFAULT_WARNING_MESSAGE_ENABLED = true;
    private static final String DEFAULT_FLIGHT_DISABLED_MESSAGE = "§cThe atmosphere here is too dense to allow the wings to open";
    private static final String DEFAULT_FIREWORKS_DISABLED_MESSAGE = "§cIt's hard to light a fuse in flight";

    public static ConfigClassHandler<DeoteConfig> HANDLER = ConfigClassHandler.createBuilder(DeoteConfig.class)
            .id(DeoteIdentifier.get("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(getConfigDirectory().resolve("deote.json"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(false)
                    .build())
            .build();
    @SerialEntry
    public WorkingMode workingMode = DEFAULT_WORKING_MODE;
    @SerialEntry
    public ArrayList<String> dimensionList = DEFAULT_DIMENSION_LIST;  // currently - allowed dimensions, maybe I'll add blacklist in the future
    @SerialEntry
    public boolean warningMessageEnabled = DEFAULT_WARNING_MESSAGE_ENABLED;
    @SerialEntry
    public String flightDisabledMessage = DEFAULT_FLIGHT_DISABLED_MESSAGE;
    @SerialEntry
    public String fireworksDisabledMessage = DEFAULT_FIREWORKS_DISABLED_MESSAGE;

    public DeoteConfig(WorkingMode workingMode, boolean warningMessageEnabled, String flightDisabledMessage, String fireworksDisabledMessage) {
        this.workingMode = workingMode;
        this.warningMessageEnabled = warningMessageEnabled;
        this.flightDisabledMessage = flightDisabledMessage;
        this.fireworksDisabledMessage = fireworksDisabledMessage;
    }

    public DeoteConfig() {
    }

    private static Path getConfigDirectory() {
        //? if fabric {
        return FabricLoader.getInstance().getConfigDir();
        //?} elif neoforge {
        /*return FMLPaths.CONFIGDIR.get();
         *///?}
    }

    /**
     * Gets the singleton instance of the config.
     * @return the config instance
     */
    public static DeoteConfig getInstance() {
        return HANDLER.instance();
    }

    /**
     * Saves the config and updates the global storage.
     */
    public static void save() {
        /*MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) {
            PlatformEntrypoint.resendConfig(server);
        }
        */

        HANDLER.save();
        updateStorage();
    }

    /**
     * Loads the config and updates the global storage.
     */
    public static void load() {
        HANDLER.load();
        ConfigProvider.setConfigInstance(DeoteConfig.getInstance());
        updateStorage();
    }

    /**
     * Updates the global storage with the current config
     * if not connected to a server.
     */
    public static void updateStorage() {
        if (!GlobalStorage.isConnectedToServer) {
            GlobalStorage.deoteConfig = DeoteConfig.getInstance();
        }
    }

    /**
     * Generates the config screen using YetAnotherConfigLib.
     *
     * @param parentScreen the parent screen
     * @return the config screen
     */
    public Screen getConfigScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Disable Elytra Outside The End"))
                .category(ConfigCategory.createBuilder()
                        .name(TextUtils.translatable("config.categories.main"))
                        .group(OptionGroup.createBuilder()
                                .name(TextUtils.translatable("config.groups.main"))
                                .option(Option.<WorkingMode>createBuilder()
                                        .name(TextUtils.translatable("config.groups.main.working_mode"))
                                        .description(OptionDescription.of(TextUtils.translatable("config.groups.main.working_mode.description")))
                                        .binding(
                                                DEFAULT_WORKING_MODE,
                                                () -> this.workingMode != null ? this.workingMode : DEFAULT_WORKING_MODE,
                                                newVal -> this.workingMode = newVal
                                        )
                                        .controller(opt -> EnumControllerBuilder.create(opt)
                                                .enumClass(WorkingMode.class))
                                        .build()
                                )
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(TextUtils.translatable("config.groups.main.warning_message"))
                                                .description(OptionDescription.of(TextUtils.translatable("config.groups.main.warning_message.description")))
                                                .binding(
                                                        DEFAULT_WARNING_MESSAGE_ENABLED,
                                                        () -> this.warningMessageEnabled,
                                                        newVal -> this.warningMessageEnabled = newVal
                                                )
                                                .controller(BooleanControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        Option.<String>createBuilder()
                                                .name(TextUtils.translatable("config.groups.main.flight_disabled_message"))
                                                .binding(
                                                        DEFAULT_FLIGHT_DISABLED_MESSAGE,
                                                        () -> this.flightDisabledMessage,
                                                        newVal -> this.flightDisabledMessage = newVal
                                                )
                                                .controller(StringControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        Option.<String>createBuilder()
                                                .name(TextUtils.translatable("config.groups.main.fireworks_disabled_message"))
                                                .binding(
                                                        DEFAULT_FIREWORKS_DISABLED_MESSAGE,
                                                        () -> this.fireworksDisabledMessage,
                                                        newVal -> this.fireworksDisabledMessage = newVal
                                                )
                                                .controller(StringControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        Option.<String>createBuilder()
                                                .name(TextUtils.translatable("config.groups.main.dimensions.list"))
                                                .description(OptionDescription.of(TextUtils.translatable("config.groups.main.dimensions.list.description")))
                                                .binding(
                                                        TextUtils.listToString(DEFAULT_DIMENSION_LIST),
                                                        () -> TextUtils.listToString(this.dimensionList),
                                                        newVal -> this.dimensionList = TextUtils.stringToList(newVal)
                                                )
                                                .controller(StringControllerBuilder::create)
                                                .build()
                                )

                                .build()
                        )
                        .build())
                .save(DeoteConfig::save)
                .build().generateScreen(parentScreen);
    }
}
