package io.github.timtaran.deote;


import io.github.timtaran.deote.config.ConfigLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class DisableElytraOutsideTheEndFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    try {
      ConfigLoader.loadConfig(FabricLoader.getInstance().getConfigDir().resolve("deote.conf"));
    } catch (Exception e) {
      Constants.LOGGER.severe("Failed to load config file: " + e.getMessage());
      throw new RuntimeException("Cannot start DisableElytraOutsideTheEnd: config failed to load", e);
    }
  }
}