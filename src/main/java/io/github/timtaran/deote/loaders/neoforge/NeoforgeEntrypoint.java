//? if neoforge {
package io.github.timtaran.deote.loaders.neoforge;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(DisableElytraOutsideTheEnd.MOD_ID)
public class NeoforgeEntrypoint {
    private static final Logger LOGGER = LogUtils.getLogger();

    public NeoforgeEntrypoint() {
        LOGGER.info("Hello from NeoforgeEntrypoint!");
        DisableElytraOutsideTheEnd.initialize();
    }
}
//?}
