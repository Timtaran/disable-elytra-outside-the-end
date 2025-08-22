package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.config.ConfigLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Player.class)
public abstract class DisableElytraClientMixin {
  @Inject(method = "startFallFlying", at = @At("HEAD"), cancellable = true)
  private void startFallFlying(CallbackInfo cir) {
    Player self = (Player) (Object) this;

    if (self.level().dimension() != Level.END) {
      Minecraft.getInstance().gui.setOverlayMessage(Component.literal(ConfigLoader.getConfig().getFlightDisabledMessage()), false);
      cir.cancel();
    }
  }
}
