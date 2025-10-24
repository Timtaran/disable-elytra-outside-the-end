package io.github.timtaran.deote.platform;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
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
        DisableElytraOutsideTheEnd.LOGGER.info("Fabric Entry Point");
        DisableElytraOutsideTheEnd.initialize();

        PayloadTypeRegistry.playS2C().register(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ServerPlayNetworking.send(handler.player, new ConfigSyncS2CPacket(DeoteConfig.HANDLER.instance().workingMode.toString())));
    }
}
//?} elif neoforge {
/*import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@Mod(DisableElytraOutsideTheEnd.MOD_ID)
@EventBusSubscriber()
public class PlatformEntrypoint {
    public PlatformEntrypoint(IEventBus modEventBus) {
        DisableElytraOutsideTheEnd.LOGGER.info("NeoForge Entry Point");
        DisableElytraOutsideTheEnd.initialize();
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        DisableElytraOutsideTheEnd.LOGGER.info("new logged in");
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            DisableElytraOutsideTheEnd.LOGGER.info("sending packet");
            try {
                PacketDistributor.sendToPlayer(serverPlayer, new ConfigSyncS2CPacket(DeoteConfig.HANDLER.instance().workingMode.toString()));
            } catch (UnsupportedOperationException ignored) {}
        }
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void register(RegisterPayloadHandlersEvent event) {
        DisableElytraOutsideTheEnd.LOGGER.info("registering packet");
        event.registrar("1").optional().commonToClient(
                ConfigSyncS2CPacket.TYPE,
                ConfigSyncS2CPacket.STREAM_CODEC,
                PlatformEntrypoint::syncConfigs
        ).optional();
    }

    private static void syncConfigs(final ConfigSyncS2CPacket payload, final IPayloadContext context) {
        DisableElytraOutsideTheEnd.LOGGER.info(payload.workingMode());
    }

//    @SubscribeEvent // on the mod event bus
//    public static void register(RegisterPayloadHandlersEvent event) {
//        final PayloadRegistrar registrar = event.registrar("1");
//        registrar.playBidirectional(
//                ConfigSyncS2CPacket.TYPE,
//                ConfigSyncS2CPacket.STREAM_CODEC,
//                ServerPayloadHandler::handleDataOnMain
//        );
//    }

}
*///?}