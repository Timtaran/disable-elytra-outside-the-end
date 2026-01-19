/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2026 timtaran
 */
package io.github.timtaran.deote.util;

import io.github.timtaran.deote.GlobalStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

//? if neoforge && <= 1.21.4 {
/*import net.neoforged.neoforge.items.IItemHandler;
import io.github.timtaran.deote.compat.CuriosCompat;
*///?}

public class FallFlyingPreventer {
    /**
     * Determines whether fall flying should be prevented for the given living entity
     * based on the global configuration.
     *
     * @param entity the entity to check
     * @param warningMessage the warning message to display
     * @return true if fall flying have been prevented, false otherwise
     */
    public static boolean preventFallFlying(Entity entity, String warningMessage) {
        if (ConfigHelper.isAllowedDimension(entity.level()))
            return false;

        if (entity instanceof LivingEntity livingEntity) {
            //? if 1.21.1 {
                    /*ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
                    if (!itemStack.isEmpty()) {
                        if (ConfigHelper.isAllowedItem(itemStack))
                            return;
                    }
                    *///?} else {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                if (LivingEntity.canGlideUsing(livingEntity.getItemBySlot(equipmentSlot), equipmentSlot)) {
                    ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
                    if (ConfigHelper.isAllowedItem(itemStack))
                        return false;
                }
            }
            //?}

            //? if neoforge && <= 1.21.4 {
            /*IItemHandler capability = livingEntity.getCapability(CuriosCompat.CURIOS_INVENTORY);
            if (capability != null) {
                if (ConfigHelper.isAllowedItem(capability.getStackInSlot(1))) // chest
                    return false;
            }
            *///?}
        }

        if (GlobalStorage.deoteConfig.warningMessageEnabled && entity instanceof ServerPlayer player)
            player.displayClientMessage(Component.literal(warningMessage), true);

        return true;
    }

    /**
     * Determines whether fall flying should be prevented for the given living entity
     * based on the global configuration.
     *
     * @param entity the entity to check
     * @return true if fall flying have been prevented, false otherwise
     */
    public static boolean preventFallFlying(Entity entity) {
        return preventFallFlying(entity, GlobalStorage.deoteConfig.getWarningMessage());
    }
}
