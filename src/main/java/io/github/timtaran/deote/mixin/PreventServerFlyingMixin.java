/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.WorkingMode;
import io.github.timtaran.deote.util.FallFlyingPreventer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Mixin to prevent server-side flying when disabled by the mod config.
 *
 * @author timtaran
 */
@Mixin(value = LivingEntity.class)
public abstract class PreventServerFlyingMixin {
    // In 1.21.1 `updateFallFlying` calls `setSharedFlag` every tick on server
    //? if >=1.21.4 {
    /**
     * Injects into the updateFallFlying method to prevent fall flying
     * based on the mod configuration.
     *
     * @param callbackInfo the callback info
     */
    @Inject(
            method = "updateFallFlying",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateFallFlying(CallbackInfo callbackInfo) {
        if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FLYING) {
            LivingEntity self = (LivingEntity) (Object) this;

            try {
                if (self.level().isClientSide()) return;

                if (FallFlyingPreventer.preventFallFlying(self)) {
                    ((EntityMixin) self).invokeSetSharedFlag(7, false);
                    callbackInfo.cancel();
                }

            } catch (Exception exception) {
                DisableElytraOutsideTheEnd.LOGGER.error(exception.getMessage());
            }
        }
    }
    //?}
}