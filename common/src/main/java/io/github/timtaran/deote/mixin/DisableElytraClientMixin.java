package io.github.timtaran.deote.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class DisableElytraClientMixin {
  @Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
  private void tryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
    Player self = (Player) (Object) this;

    if (self.level().dimension() != Level.END) {
      cir.setReturnValue(false);
    }
  }
}
