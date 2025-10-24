package io.github.timtaran.deote.config;

import dev.isxander.yacl3.api.NameableEnum;
import io.github.timtaran.deote.util.TextUtils;
import net.minecraft.network.chat.Component;

public enum WorkingMode implements NameableEnum {
    NONE,
    FIREWORKS,
    FLYING;

    @Override
    public Component getDisplayName() {
        return TextUtils.translatable("working_mode." + name().toLowerCase());
    }
}
