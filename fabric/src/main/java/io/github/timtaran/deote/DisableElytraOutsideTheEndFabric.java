package io.github.timtaran.deote;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class DisableElytraOutsideTheEndFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    DisableElytraOutsideTheEnd.init(FabricLoader.getInstance().getConfigDir().resolve("deote.conf"));
  }
}