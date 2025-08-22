package io.github.timtaran.deote.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class ConfigModel {
  @Comment("Should player be warned when their wings are disabled?")
  private boolean warningMessageEnabled = true;

  @Comment("Message to display when player's wings are disabled. You can use `§` to format text.")
  private String flightDisabledMessage = "§cThe atmosphere here is too dense to allow the wings to open";

  public String getFlightDisabledMessage() {
    return flightDisabledMessage;
  }

  public boolean isWarningMessageEnabled() {
    return warningMessageEnabled;
  }
}