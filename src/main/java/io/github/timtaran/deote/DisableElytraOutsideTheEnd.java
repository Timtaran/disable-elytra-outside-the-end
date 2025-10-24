package io.github.timtaran.deote;

import com.mojang.logging.LogUtils;
import io.github.timtaran.deote.config.DeoteConfig;
import org.slf4j.Logger;

public class DisableElytraOutsideTheEnd {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "deote";

    public static void initialize() {
        DeoteConfig.load();
    }
}
