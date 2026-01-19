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
import net.minecraft.world.InteractionHand;
//? if 1.21.1 {
/*import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
*///?} elif >=1.21.4 {
import net.minecraft.world.InteractionResult;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


/**
 * Mixin to prevent firework boosting when disabled by the mod config.
 *
 * @author timtaran
 */
@Mixin(value = FireworkRocketItem.class)
public class PreventFireworkBoostingMixin {
    /**
     * Injects into the use method to prevent firework boosting
     * based on the mod configuration.
     *
     * @param level           the level
     * @param player          the player
     * @param interactionHand the interaction hand
     * @param callbackInfo    the callback info
     */
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void init(
            Level level,
            Player player,
            InteractionHand interactionHand,
            //? if 1.21.1 {
            /*CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfo)
             *///?} elif >= 1.21.4 {
            CallbackInfoReturnable<InteractionResult> callbackInfo)
    //?}
    {
        if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FIREWORKS) {
            try {
                if (FallFlyingPreventer.preventFallFlying(player)) {
                    if (player.isFallFlying()) {
                        //? if 1.21.1 {
                        /*callbackInfo.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(interactionHand)));
                         *///?} elif >= 1.21.4 {
                        callbackInfo.setReturnValue(InteractionResult.PASS);
                        //?}

                        callbackInfo.cancel();
                    }
                }
            } catch (Exception exception) {
                DisableElytraOutsideTheEnd.LOGGER.error(exception.getMessage());
            }
        }
    }
}