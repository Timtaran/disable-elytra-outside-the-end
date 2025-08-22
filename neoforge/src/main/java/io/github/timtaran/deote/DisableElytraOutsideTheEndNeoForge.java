package io.github.timtaran.deote;


import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class DisableElytraOutsideTheEndNeoForge {
  public DisableElytraOutsideTheEndNeoForge() {
    DisableElytraOutsideTheEnd.init(FMLPaths.CONFIGDIR.get().resolve("deote.conf"));
  }
}