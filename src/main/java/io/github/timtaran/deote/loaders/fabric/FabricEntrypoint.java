//? if fabric {
/*package io.github.timtaran.deote.loaders.fabric;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class FabricEntrypoint implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Hello from FabricEntrypoint!");
        DisableElytraOutsideTheEnd.initialize();
    }
}
*///?}
