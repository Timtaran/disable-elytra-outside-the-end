/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
package io.github.timtaran.deote.util;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
//? if >1.21.10 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
 *///?}

/**
 * A utility class for creating mod-specific Identifiers.
 *
 * @author timtaran
 */
public class DeoteIdentifier {
    //? if >1.21.10 {
    public static Identifier get(String path) {
        return Identifier.fromNamespaceAndPath(DisableElytraOutsideTheEnd.MOD_ID, path);
    }
    //?} else {
     /*public static ResourceLocation get(String path) {
         return ResourceLocation.fromNamespaceAndPath(DisableElytraOutsideTheEnd.MOD_ID, path);
     }
    *///?}
}
