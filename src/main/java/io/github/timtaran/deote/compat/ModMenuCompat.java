//? if fabric {
package io.github.timtaran.deote.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.timtaran.deote.config.DeoteConfig;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> DeoteConfig.HANDLER.instance().getConfigScreen(parentScreen);
    }
}
//?}