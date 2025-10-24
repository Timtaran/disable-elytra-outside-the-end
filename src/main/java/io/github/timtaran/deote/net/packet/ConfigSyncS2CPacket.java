package io.github.timtaran.deote.net.packet;

import io.github.timtaran.deote.util.DeoteIdentifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ConfigSyncS2CPacket(String workingMode) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ConfigSyncS2CPacket> TYPE = new CustomPacketPayload.Type<>(DeoteIdentifier.get("config_sync"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ConfigSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ConfigSyncS2CPacket::workingMode,
            ConfigSyncS2CPacket::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
