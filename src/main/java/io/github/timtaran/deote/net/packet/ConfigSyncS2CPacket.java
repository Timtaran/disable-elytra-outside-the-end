/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.net.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.util.DeoteIdentifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

/**
 * Server-to-client packet for syncing config.
 *
 * @author timtaran
 */
public record ConfigSyncS2CPacket(DeoteConfig config) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ConfigSyncS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(DeoteIdentifier.get("config_sync"));

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final StreamCodec<ByteBuf, ConfigSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            packet -> GSON.toJson(packet.config()),
            json -> new ConfigSyncS2CPacket(GSON.fromJson(json, DeoteConfig.class))
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
