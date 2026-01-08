/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.config;

import dev.isxander.yacl3.api.NameableEnum;
import io.github.timtaran.deote.util.TextUtils;
import net.minecraft.network.chat.Component;

/**
 * Enum representing the working modes of the mod.
 *
 * @author timtaran
 */
public enum WorkingMode implements NameableEnum {
    NONE,
    FIREWORKS,
    FLYING;

    @Override
    public Component getDisplayName() {
        return TextUtils.translatable("working_mode." + name().toLowerCase());
    }
}
