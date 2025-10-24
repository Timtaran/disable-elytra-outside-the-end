package io.github.timtaran.deote.util;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import net.minecraft.resources.ResourceLocation;

public class DeoteIdentifier {
    public static ResourceLocation get(String path) {
        return ResourceLocation.fromNamespaceAndPath(DisableElytraOutsideTheEnd.MOD_ID, path);
    }
}
