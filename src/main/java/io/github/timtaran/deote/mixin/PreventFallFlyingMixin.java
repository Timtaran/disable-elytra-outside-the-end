/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2026 timtaran
 */
package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.WorkingMode;
import io.github.timtaran.deote.util.ConfigHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for Entity to prevent fall flying.
 *
 * @author timtaran
 */
@Mixin(Entity.class)
public abstract class PreventFallFlyingMixin {
    @Shadow
    protected abstract void setSharedFlag(int flag, boolean value);

    @Inject(method = "setSharedFlag", at = @At("HEAD"), cancellable = true)
    protected void deote$setSharedFlag(int flag, boolean value, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        try {
            if (
                    value &&
                            flag == 7 && // Fall flying flag
                            GlobalStorage.deoteConfig.workingMode == WorkingMode.FLYING &&
                            !ConfigHelper.isAllowedDimension(GlobalStorage.deoteConfig, self.level())

            ) {
                if (GlobalStorage.deoteConfig.warningMessageEnabled && self instanceof ServerPlayer player)
                    player.displayClientMessage(Component.literal(GlobalStorage.deoteConfig.flightDisabledMessage), true);
                setSharedFlag(flag, false);
                ci.cancel();
            }
        } catch (Exception exception) {
            DisableElytraOutsideTheEnd.LOGGER.error(exception.getMessage());
        }
    }
}
