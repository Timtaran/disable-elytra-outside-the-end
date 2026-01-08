/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.platform;

import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.DeoteConfig;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import io.github.timtaran.deote.net.packet.ConfigSyncS2CPacket;


/**
 * Fabric client entrypoint.
 *
 * @author timtaran
 */
public class PlatformClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncS2CPacket.TYPE, (payload, context) ->
                {
                    GlobalStorage.deoteConfig = payload.config();
                    GlobalStorage.isGotSyncPacket = true;
                }
        );

        ClientPlayConnectionEvents.INIT.register((phase, listener) -> {
            if (!listener.hasSingleplayerServer()) {
                if (!GlobalStorage.isGotSyncPacket) {
                    GlobalStorage.setNullConfig();
                }
                GlobalStorage.isConnectedToServer = true;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register(
                (phase, listener) -> {
                    DeoteConfig.updateStorage();
                    GlobalStorage.isGotSyncPacket = false;
                    GlobalStorage.isConnectedToServer = false;
                }

        );
    }
}
//?} elif neoforge {
/*import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import io.github.timtaran.deote.DisableElytraOutsideTheEnd;


/^*
 * NeoForge client entrypoint.
 *
 * @author timtaran
 ^/
@Mod(value = DisableElytraOutsideTheEnd.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = DisableElytraOutsideTheEnd.MOD_ID, value = Dist.CLIENT)
public class PlatformClientEntrypoint {
    public PlatformClientEntrypoint() {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parentScreen) -> DeoteConfig.getInstance().getConfigScreen(parentScreen)
        );
    }

    @SubscribeEvent
    public static void onClientConnect(ClientPlayerNetworkEvent.LoggingIn event) {
        if (!Minecraft.getInstance().hasSingleplayerServer()) {
            if (!GlobalStorage.isGotSyncPacket) {
                GlobalStorage.setNullConfig();
            }
            GlobalStorage.isConnectedToServer = true;
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        DeoteConfig.updateStorage();
        GlobalStorage.isGotSyncPacket = false;
        GlobalStorage.isConnectedToServer = false;
    }
}

*///?}