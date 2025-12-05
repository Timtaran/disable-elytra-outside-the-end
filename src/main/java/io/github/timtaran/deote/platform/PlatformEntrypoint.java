package io.github.timtaran.deote.platform;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.commands.DeoteCommands;
import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.net.packet.ConfigSyncS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;


//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//?} elif neoforge {
/*import io.github.timtaran.deote.GlobalStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
*///?}

//? if fabric {
public class PlatformEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        DisableElytraOutsideTheEnd.initialize();

        PayloadTypeRegistry.playS2C().register(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                // Fabric server doesn't care if client doesn't have channel so we're not catching any exceptions here
                sendConfigSyncPacket(handler.player, DeoteConfig.getInstance()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> DeoteCommands.registerCommands(dispatcher));
    }

    public static void sendConfigSyncPacket(ServerPlayer player, DeoteConfig config) {
        ServerPlayNetworking.send(player, new ConfigSyncS2CPacket(config));
    }
//?} elif neoforge {
/*@Mod(DisableElytraOutsideTheEnd.MOD_ID)
@EventBusSubscriber(modid = DisableElytraOutsideTheEnd.MOD_ID)
public class PlatformEntrypoint {
    public PlatformEntrypoint() {
        DisableElytraOutsideTheEnd.initialize();
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            try {
                sendConfigSyncPacket(serverPlayer, DeoteConfig.getInstance());
            } catch (UnsupportedOperationException ignored) {}
        }
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").optional().commonToClient(
                ConfigSyncS2CPacket.TYPE,
                ConfigSyncS2CPacket.STREAM_CODEC,
                PlatformEntrypoint::syncConfigs
        ).optional();
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        DeoteCommands.registerCommands(event.getDispatcher());
    }

    private static void syncConfigs(final ConfigSyncS2CPacket payload, final IPayloadContext context) {
        GlobalStorage.deoteConfig = payload.config();
        GlobalStorage.isGotSyncPacket = true;
    }

    public static void sendConfigSyncPacket(ServerPlayer player, DeoteConfig config) {
        PacketDistributor.sendToPlayer(player, new ConfigSyncS2CPacket(config));
    }

*///?}

    public static void resendConfig(MinecraftServer server) {

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!server.isSingleplayerOwner(player.getGameProfile())) {
                sendConfigSyncPacket(player, DeoteConfig.getInstance());
            }
        }
    }
}