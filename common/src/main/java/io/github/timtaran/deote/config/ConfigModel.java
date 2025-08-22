package io.github.timtaran.deote.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class ConfigModel {
  private boolean warningMessageEnabled = true;
  private String flightDisabledMessage = "§cThe air here is too light to allow the wings to open";

  public String getFlightDisabledMessage() {
    return flightDisabledMessage;
  }
}