/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

//? if <=1.21.10 {
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
//?} else {
/*import net.minecraft.server.permissions.Permissions;
import org.apache.commons.lang3.Strings;
*///?}
import java.util.ArrayList;
import java.util.Collection;


/**
 * Class responsible for registering and handling Deote mod commands.
 *
 * @author timtaran
 */
public class DeoteCommands {
    private static boolean startsWith(String str, String prefix) {
        //? if <=1.21.10 {
        return startsWithIgnoreCase(str, prefix);
        //?} else {
        /*return Strings.CI.startsWith(str, prefix);
        *///?}
    }
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_PARAM_NAME =
            (CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) -> {
                Collection<String> params = ConfigProvider.getAllParams();
                for (String p : params) {
                    if (startsWith(p, builder.getRemaining())) builder.suggest(p);
                }
                return builder.buildFuture();
            };

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_PARAM_VALUE =
            (CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) -> {
                try {
                    String param = StringArgumentType.getString(ctx, "param");
                    Collection<String> values = ConfigProvider.getParamValues(param);
                    for (String v : values) {
                        if (startsWith(v, builder.getRemaining())) builder.suggest(v);
                    }
                } catch (IllegalArgumentException ex) {
                    DisableElytraOutsideTheEnd.LOGGER.debug(ex.getMessage());
                }
                return builder.buildFuture();
            };

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_ADD_DIMENSION =
            (CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) -> {
                try {
                    Collection<String> values = getAllDimensions(ctx.getSource().getServer());
                    for (String v : values) {
                        if (startsWith(v, builder.getRemaining()) && !ConfigProvider.getConfigInstance().dimensionList.contains(v))
                            builder.suggest(v);
                    }
                } catch (IllegalArgumentException ex) {
                    DisableElytraOutsideTheEnd.LOGGER.debug(ex.getMessage());
                }
                return builder.buildFuture();
            };

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_REMOVE_DIMENSION =
            (CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) -> {
                try {
                    Collection<String> values = ConfigProvider.getConfigInstance().dimensionList;
                    for (String v : values) {
                        if (startsWith(v, builder.getRemaining())) builder.suggest(v);
                    }
                } catch (IllegalArgumentException ex) {
                    DisableElytraOutsideTheEnd.LOGGER.debug(ex.getMessage());
                }
                return builder.buildFuture();
            };

    /**
     * Retrieves all dimension identifiers from the Minecraft server.
     *
     * @param minecraftServer the Minecraft server instance
     * @return a collection of dimension identifiers
     */
    private static Collection<String> getAllDimensions(MinecraftServer minecraftServer) {
        Collection<String> dimensions = new ArrayList<>();
        for (ServerLevel level : minecraftServer.getAllLevels()) {
            //? if <=1.21.10 {
             dimensions.add(level.dimension().location().toString());
            //?} else {
            /*dimensions.add(level.dimension().identifier().toString());
            *///?}
        }
        return dimensions;
    }

    /**
     * Sets the value of a configuration parameter.
     *
     * @param context the command context
     * @return command result code
     */
    private static int setValue(CommandContext<CommandSourceStack> context) {
        try {
            ConfigProvider.setParamValue(
                    context.getSource().getServer(),
                    StringArgumentType.getString(context, "param"),
                    StringArgumentType.getString(context, "value")
            );
            context.getSource().sendSuccess(Messages.VALUE_CHANGE_SUCCESS, false);
        } catch (IllegalAccessException e) {
            DisableElytraOutsideTheEnd.LOGGER.error(e.getMessage());
            context.getSource().sendSuccess(Messages.WRONG_ARGUMENTS, false);
        }
        return 1;
    }

    /**
     * Adds a dimension to the configuration's dimension list.
     *
     * @param context the command context
     * @return command result code
     */
    private static int addDimension(CommandContext<CommandSourceStack> context) {
        ConfigProvider.getConfigInstance().dimensionList.add(StringArgumentType.getString(context, "dimension"));
        context.getSource().sendSuccess(Messages.VALUE_CHANGE_SUCCESS, false);
        ConfigProvider.save();
        ConfigProvider.reload(context.getSource().getServer());
        return 1;
    }

    /**
     * Removes a dimension from the configuration's dimension list.
     *
     * @param context the command context
     * @return command result code
     */
    private static int removeDimension(CommandContext<CommandSourceStack> context) {
        String dimension = StringArgumentType.getString(context, "dimension");

        if (!ConfigProvider.getConfigInstance().dimensionList.contains(dimension)) {
            context.getSource().sendSuccess(Messages.WRONG_ARGUMENTS, false);
            return 0;
        }

        ConfigProvider.getConfigInstance().dimensionList.remove(dimension);
        context.getSource().sendSuccess(Messages.VALUE_CHANGE_SUCCESS, false);
        ConfigProvider.save();
        ConfigProvider.reload(context.getSource().getServer());
        return 1;
    }

    /**
     * Reloads the mod configuration.
     *
     * @param context the command context
     * @return command result code
     */
    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        ConfigProvider.reload(context.getSource().getServer());
        context.getSource().sendSuccess(Messages.CONFIG_RELOAD_SUCCESS, false);

        return 1;
    }

    /**
     * Registers the Deote mod commands with the command dispatcher.
     *
     * @param dispatcher the command dispatcher
     */
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deote")
                        //? if <=1.21.10 {
                         .requires(source -> source.hasPermission(3))
                        //?} else {
                        /*.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                        *///?}

                        .executes(context -> {
                            context.getSource().sendSuccess(Messages.HELP_COMMAND, false);
                            return 1;
                        })

                        .then(Commands.literal("help")
                                .executes(
                                        context -> {
                                            context.getSource().sendSuccess(Messages.HELP_COMMAND, false);
                                            return 1;
                                        }
                                ))

                        .then(Commands.literal("reload")
                                .executes(DeoteCommands::reloadConfig)
                        )

                        .then(Commands.literal("config")
                                .executes(context -> {
                                    context.getSource().sendSuccess(Messages.CONFIG_MESSAGE, false);
                                    return 1;
                                })

                                .then(Commands.literal("set")
                                        .then(Commands.argument("param", StringArgumentType.word())
                                                .suggests(SUGGEST_PARAM_NAME)
                                                .then(Commands.argument("value", StringArgumentType.greedyString())
                                                        .suggests(SUGGEST_PARAM_VALUE)
                                                        .executes(DeoteCommands::setValue)
                                                )
                                        )
                                )

                                .then(
                                        Commands.literal("dimension_list")
                                                .then(Commands.literal("add")
                                                        .then(Commands.argument("dimension", StringArgumentType.greedyString())
                                                                .suggests(SUGGEST_ADD_DIMENSION)
                                                                .executes(DeoteCommands::addDimension)
                                                        )

                                                )
                                                .then(Commands.literal("remove")
                                                        .then(Commands.argument("dimension", StringArgumentType.greedyString())
                                                                .suggests(SUGGEST_REMOVE_DIMENSION)
                                                                .executes(DeoteCommands::removeDimension)
                                                        )

                                                )
                                )
                        )
        );
    }
}
