package io.github.timtaran.deote.platform;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.net.packet.ConfigSyncS2CPacket;
//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PlatformEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        DisableElytraOutsideTheEnd.initialize();

        PayloadTypeRegistry.playS2C().register(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                // Fabric server doesn't care if client doesn't have channel so we're not catching any exceptions here
                ServerPlayNetworking.send(handler.player, new ConfigSyncS2CPacket(DeoteConfig.getInstance())));
    }
}
//?} elif neoforge {
/*import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@Mod(DisableElytraOutsideTheEnd.MOD_ID)
@EventBusSubscriber(modid = DisableElytraOutsideTheEnd.MOD_ID)
public class PlatformEntrypoint {
    public PlatformEntrypoint() {
        DisableElytraOutsideTheEnd.initialize();
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            try {
                PacketDistributor.sendToPlayer(serverPlayer, new ConfigSyncS2CPacket(DeoteConfig.getInstance()));
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

    private static void syncConfigs(final ConfigSyncS2CPacket payload, final IPayloadContext context) {
        DisableElytraOutsideTheEnd.LOGGER.debug("got packet sync packet from server");
        GlobalStorage.deoteConfig = payload.config();
        GlobalStorage.gotSyncPacket = true;
    }
}
*///?}