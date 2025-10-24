package io.github.timtaran.deote.platform;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
//? if fabric {
import net.fabricmc.api.ModInitializer;

public class PlatformEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        DisableElytraOutsideTheEnd.LOGGER.info("Fabric Entry Point");
        DisableElytraOutsideTheEnd.initialize();
    }
}
//?} elif neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(DisableElytraOutsideTheEnd.MOD_ID)
public class PlatformEntrypoint {
    public PlatformEntrypoint(IEventBus modEventBus) {
        DisableElytraOutsideTheEnd.LOGGER.info("NeoForge Entry Point");
        DisableElytraOutsideTheEnd.initialize();
    }
}
*///?}