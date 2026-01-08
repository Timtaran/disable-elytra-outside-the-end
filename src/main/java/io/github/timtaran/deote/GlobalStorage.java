/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote;


import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.config.WorkingMode;

/**
 * A global storage for the mod.
 * Used to store current config and connection status.
 *
 * @author timtaran
 */
public class GlobalStorage {
    public static DeoteConfig deoteConfig;
    public static boolean isGotSyncPacket = false;
    public static boolean isConnectedToServer = false;

    public static void setNullConfig() {
        deoteConfig = new DeoteConfig(
                WorkingMode.NONE,
                false,
                "",
                ""
        );
    }
}
