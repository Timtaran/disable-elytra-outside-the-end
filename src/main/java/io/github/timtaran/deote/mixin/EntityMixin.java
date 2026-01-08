/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Mixin interface for Entity to access setSharedFlag method.
 *
 * @author timtaran
 */
@Mixin(Entity.class)
public interface EntityMixin {
    @Invoker("setSharedFlag")
    void invokeSetSharedFlag(int index, boolean value);
}
