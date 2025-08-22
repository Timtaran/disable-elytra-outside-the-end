package io.github.timtaran.deote;


import io.github.timtaran.deote.config.ConfigLoader;

import java.nio.file.Path;

public class DisableElytraOutsideTheEnd {
  public static void init(Path configPath) {
    try {
      ConfigLoader.loadConfig(configPath);
    } catch (Exception e) {
      Constants.LOGGER.error("Failed to load config file: {}", e.getMessage());
      throw new RuntimeException("Cannot start DisableElytraOutsideTheEnd: config failed to load", e);
    }
  }
}
