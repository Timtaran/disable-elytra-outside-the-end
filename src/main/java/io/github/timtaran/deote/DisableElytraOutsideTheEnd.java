/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote;

import com.mojang.logging.LogUtils;
import io.github.timtaran.deote.config.DeoteConfig;
import org.slf4j.Logger;

/**
 * Loader-agnostic main class of mod.
 *
 * @author timtaran
 */
public class DisableElytraOutsideTheEnd {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "deote";

    public static void initialize() {
        DeoteConfig.load();
    }
}
