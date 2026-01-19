/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2026 timtaran
 */
package io.github.timtaran.deote.util;

import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.DeoteConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Utility class for config-related operations.
 *
 * @author timtaran
 */
public class ConfigHelper {
    /**
     * Checks if the given dimension is allowed based on the config.
     *
     * @param deoteConfig the mod configuration
     * @param level       the level to check
     * @return true if the dimension is allowed, false otherwise
     */
    public static boolean isAllowedDimension(DeoteConfig deoteConfig, Level level) {
        return deoteConfig.dimensionList.contains(
                //? if <= 1.21.10 {
                level.dimension().location().toString()
                //?} else {
                /*level.dimension().identifier().toString()
                 *///?}
        );
    }

    /**
     * Checks if the given dimension is allowed based on the global config.
     *
     * @param level the level to check
     * @return true if the dimension is allowed, false otherwise
     */
    public static boolean isAllowedDimension(Level level) {
        return isAllowedDimension(GlobalStorage.deoteConfig, level);
    }

    /**
     * Checks if the given item is allowed based on the config.
     *
     * @param deoteConfig the mod configuration
     * @param itemStack   the item stack to check
     * @return true if the item is allowed, false otherwise
     */
    public static boolean isAllowedItem(DeoteConfig deoteConfig, ItemStack itemStack) {
        if (!itemStack.isEmpty()) return deoteConfig.itemList.contains(
                itemStack.getItem().toString()
        );
        else return true;
    }

    /**
     * Checks if the given dimension is allowed based on the global config.
     *
     * @return true if the dimension is allowed, false otherwise
     */
    public static boolean isAllowedItem(ItemStack itemStack) {
        return isAllowedItem(DeoteConfig.getInstance(), itemStack);
    }
}
