package io.github.timtaran.deote.config;

import io.github.timtaran.deote.Constants;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {
  private static ConfigModel config;

  public static ConfigModel getConfig() {
    return config;
  }

  public static void loadConfig(Path configPath) throws ConfigurateException {
    if (!Files.exists(configPath)) {
      Constants.LOGGER.info("Config file not found, creating a new one");
      config = new ConfigModel();
      saveConfig(configPath);
      return;
    }

    final HoconConfigurationLoader loader =
            HoconConfigurationLoader.builder().path(configPath).build();

    CommentedConfigurationNode node = loader.load();
    config = node.get(ConfigModel.class);

    saveConfig(configPath);
  }

  public static void saveConfig(Path configPath) throws ConfigurateException {
    HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .path(configPath)
            .build();

    CommentedConfigurationNode node = loader.createNode();
    node.set(ConfigModel.class, config);

    loader.save(node);
  }
}