/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
//? if fabric {
package io.github.timtaran.deote.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.timtaran.deote.config.DeoteConfig;

/**
 * ModMenu compatibility class.
 *
 * @author timtaran
 */
public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> DeoteConfig.getInstance().getConfigScreen(parentScreen);
    }
}
//?}