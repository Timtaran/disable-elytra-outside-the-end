package io.github.timtaran.deote;


import io.github.timtaran.deote.config.ConfigLoader;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class DisableElytraOutsideTheEndNeoForge {
  public DisableElytraOutsideTheEndNeoForge() {
    try {
      Constants.LOGGER.info(FMLPaths.CONFIGDIR.get().resolve("deote.conf").toAbsolutePath().toString());
      ConfigLoader.loadConfig(FMLPaths.CONFIGDIR.get().resolve("deote.conf"));
    } catch (Exception e) {
      Constants.LOGGER.severe("Failed to load config file: " + e.getMessage());
      throw new RuntimeException("Cannot start DisableElytraOutsideTheEnd: config failed to load", e);
    }
  }
}