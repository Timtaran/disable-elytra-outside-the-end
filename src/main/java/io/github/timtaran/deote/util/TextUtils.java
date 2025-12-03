package io.github.timtaran.deote.util;


import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import net.minecraft.network.chat.Component;

public class TextUtils {
    public static Component translatable(String key) {
        return Component.translatable(DisableElytraOutsideTheEnd.MOD_ID + "." + key);
    }

    public static String formatColors(String input) {
        return input.replaceAll("&([0-9a-fk-or])", "§$1");
    }

}
