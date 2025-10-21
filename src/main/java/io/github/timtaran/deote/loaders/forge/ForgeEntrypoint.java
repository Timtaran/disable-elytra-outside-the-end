//? if forge {
/*package io.github.timtaran.deote.loaders.forge;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(DisableElytraOutsideTheEnd.MOD_ID)
public class ForgeEntrypoint {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ForgeEntrypoint() {
        LOGGER.info("Hello from ForgeEntrypoint!");
        DisableElytraOutsideTheEnd.initialize();
    }
}
*///?}
