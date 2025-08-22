package io.github.timtaran.deote.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public class ConfigLoader {
  private static ConfigModel config;

  public static ConfigModel getConfig() {
    return config;
  }

  public static void loadConfig(Path configPath) throws ConfigurateException {
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