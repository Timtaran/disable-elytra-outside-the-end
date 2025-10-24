package io.github.timtaran.deote.platform;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.net.packet.ConfigSyncS2CPacket;


//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PlatformClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncS2CPacket.TYPE, (payload, context) ->
                DisableElytraOutsideTheEnd.LOGGER.info(payload.workingMode()));
    }
}
//?}