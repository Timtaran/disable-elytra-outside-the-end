package io.github.timtaran.deote.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import io.github.timtaran.deote.GlobalStorage;
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

    public DeoteConfig(WorkingMode workingMode, boolean warningMessageEnabled, String flightDisabledMessage, String fireworksDisabledMessage) {
        this.workingMode = workingMode;
        this.warningMessageEnabled = warningMessageEnabled;
        this.flightDisabledMessage = flightDisabledMessage;
        this.fireworksDisabledMessage = fireworksDisabledMessage;
    }

    public DeoteConfig() {}

    private static final WorkingMode DEFAULT_WORKING_MODE = WorkingMode.FLYING;
    private static final boolean DEFAULT_WARNING_MESSAGE_ENABLED = true;
    private static final String DEFAULT_FLIGHT_DISABLED_MESSAGE = "§cThe atmosphere here is too dense to allow the wings to open";
    private static final String DEFAULT_FIREWORKS_DISABLED_MESSAGE = "§cIt's hard to light a fuse in flight";



    @SerialEntry
    public WorkingMode workingMode = DEFAULT_WORKING_MODE;

    @SerialEntry
    public boolean warningMessageEnabled = DEFAULT_WARNING_MESSAGE_ENABLED;

    @SerialEntry
    public String flightDisabledMessage = DEFAULT_FLIGHT_DISABLED_MESSAGE;

    @SerialEntry
    public String fireworksDisabledMessage = DEFAULT_FIREWORKS_DISABLED_MESSAGE;

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
                                                DEFAULT_WORKING_MODE,
                                                () -> this.workingMode,
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
                                                .description(OptionDescription.of(TextUtils.translatable("config.groups.main.flight_disabled_message.description")))
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
                                                .description(OptionDescription.of(TextUtils.translatable("config.groups.main.fireworks_disabled_message.description")))
                                                .binding(
                                                        DEFAULT_FIREWORKS_DISABLED_MESSAGE,
                                                        () -> this.fireworksDisabledMessage,
                                                        newVal -> this.fireworksDisabledMessage = newVal
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

    public static DeoteConfig getInstance() {
        return HANDLER.instance();
    }

    public static void save() {
        HANDLER.save();
        updateStorage();
    }

    public static void load() {
        HANDLER.load();
        updateStorage();
    }

    public static void updateStorage() {
        GlobalStorage.deoteConfig = DeoteConfig.getInstance();
    }
}
