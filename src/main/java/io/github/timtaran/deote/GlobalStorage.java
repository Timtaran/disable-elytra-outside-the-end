package io.github.timtaran.deote;


import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.config.WorkingMode;

public class GlobalStorage {
    public static DeoteConfig deoteConfig;
    public static boolean gotSyncPacket;

    public static void setNullConfig() {
        deoteConfig = new DeoteConfig(
                WorkingMode.NONE,
                false,
                "",
                ""
        );
    }
}
