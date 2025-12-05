package io.github.timtaran.deote.commands;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public final class Messages {
    public static final Supplier<Component> HELP_COMMAND = () ->
            Component.literal("Available commands:\n")
                    .append(Component.literal(" /deote help").withStyle(ChatFormatting.ITALIC)
                            .withStyle(s -> TextUtils.styleCommandText(s, "/deote help", "/deote help")))
                    .append(" - shows this help message\n")
                    .append(Component.literal(" /deote reload").withStyle(ChatFormatting.ITALIC)
                            .withStyle(s -> TextUtils.styleCommandText(s, "/deote reload", "/deote reload")))
                    .append(" - reloads the config and syncs it to all clients\n")
                    .append(Component.literal(" /deote config").withStyle(ChatFormatting.ITALIC)
                            .withStyle(s -> TextUtils.styleCommandText(s, "/deote config", "/deote config")))
                    .append(" - config-related commands");

    public static final Supplier<Component> VALUE_CHANGE_SUCCESS =
            () -> Component.literal("Value successfully updated.");

    public static final Supplier<Component> CONFIG_RELOAD_SUCCESS =
            () -> Component.literal("Configuration successfully reloaded.");

    public static final Supplier<Component> WRONG_ARGUMENTS =
            () -> Component.literal("Invalid command arguments.").withStyle(ChatFormatting.RED);

    public static final Supplier<Component> CONFIG_MESSAGE = Messages::getConfigMessage;

    private static Component getConfigMessage() {
        Component message = Component.literal("Current configuration values:\n");

        for (String param : ConfigProvider.getAllParams()) {
            try {
                message = message.copy()
                        .append(
                                String.format(
                                        " %s - %s\n",
                                        param,
                                        ConfigProvider.getFieldByName(param)
                                                .get(ConfigProvider.getConfigInstance())
                                )
                        );
            } catch (IllegalAccessException e) {
                DisableElytraOutsideTheEnd.LOGGER.error(e.getMessage());
            }
        }

        message = message.copy()
                .append(String.format(
                        " dimensionList - %s\n",
                        TextUtils.listToString(
                                ConfigProvider.getConfigInstance().dimensionList
                        )
                ))
                .append("\nYou can change a config value with:\n")
                .append(Component.literal(" /deote config set <param> <value>\n")
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(s -> TextUtils.styleCommandText(s, "/deote config set ", "/deote config set")))
                .append("Or edit the dimension list with:\n")
                .append(Component.literal(" /deote config dimension_list <add/remove> <dimension_name>")
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(s -> TextUtils.styleCommandText(s, "/deote config dimension_list ", "/deote config dimension_list")));

        return message;
    }
}
