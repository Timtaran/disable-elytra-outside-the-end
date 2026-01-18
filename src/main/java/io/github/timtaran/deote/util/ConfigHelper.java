/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2026 timtaran
 */
package io.github.timtaran.deote.util;

import io.github.timtaran.deote.config.DeoteConfig;
import net.minecraft.world.level.Level;

/**
 * Utility class for config-related operations.
 *
 * @author timtaran
 */
public class ConfigHelper {
    public static boolean isAllowedDimension(DeoteConfig deoteConfig, Level level) {
        return deoteConfig.dimensionList.contains(
        //? if <= 1.21.10 {
        level.dimension().location().toString()
        //?} else {
        /*level.dimension().identifier().toString()
         *///?}
        );
    }
}
