package io.github.timtaran.deote.commands;

import com.mojang.brigadier.Command;
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

import java.util.ArrayList;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;


public class DeoteCommands {
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_PARAM_NAME =
            (CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) -> {
                Collection<String> params = ConfigProvider.getAllParams();
                for (String p : params) {
                    if (startsWithIgnoreCase(p, builder.getRemaining())) builder.suggest(p);
                }
                return builder.buildFuture();
            };

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_PARAM_VALUE =
            (CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) -> {
                try {
                    String param = StringArgumentType.getString(ctx, "param");
                    Collection<String> values = ConfigProvider.getParamValues(param);
                    for (String v : values) {
                        if (startsWithIgnoreCase(v, builder.getRemaining())) builder.suggest(v);
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
                        if (startsWithIgnoreCase(v, builder.getRemaining()) && !ConfigProvider.getConfigInstance().dimensionList.contains(v))
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
                        if (startsWithIgnoreCase(v, builder.getRemaining())) builder.suggest(v);
                    }
                } catch (IllegalArgumentException ex) {
                    DisableElytraOutsideTheEnd.LOGGER.debug(ex.getMessage());
                }
                return builder.buildFuture();
            };

    private static Collection<String> getAllDimensions(MinecraftServer minecraftServer) {
        Collection<String> dimensions = new ArrayList<>();
        for (ServerLevel level : minecraftServer.getAllLevels()) {
            dimensions.add(level.dimension().location().toString());
        }
        return dimensions;
    }

    private static int setValue(CommandContext<CommandSourceStack> context) {
        try {
            ConfigProvider.setParamValue(
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

    private static int addDimension(CommandContext<CommandSourceStack> context) {
        ConfigProvider.getConfigInstance().dimensionList.add(StringArgumentType.getString(context, "dimension"));
        context.getSource().sendSuccess(Messages.VALUE_CHANGE_SUCCESS, false);
        ConfigProvider.save();
        return 1;
    }

    private static int removeDimension(CommandContext<CommandSourceStack> context) {
        String dimension = StringArgumentType.getString(context, "dimension");

        if (!ConfigProvider.getConfigInstance().dimensionList.contains(dimension)) {
            context.getSource().sendSuccess(Messages.WRONG_ARGUMENTS, false);
            return 0;
        }

        ConfigProvider.getConfigInstance().dimensionList.remove(dimension);
        context.getSource().sendSuccess(Messages.VALUE_CHANGE_SUCCESS, false);
        ConfigProvider.save();
        return 1;
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deote")
                        .requires(source -> source.hasPermission(2))

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
                                .executes(ctx -> {
                                    // TODO reload and re-send config for every player on server
                                    return Command.SINGLE_SUCCESS;
                                })
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
