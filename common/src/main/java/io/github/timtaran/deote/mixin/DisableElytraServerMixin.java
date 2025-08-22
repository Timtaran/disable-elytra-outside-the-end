package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.config.ConfigLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(value = LivingEntity.class)
public class DisableElytraServerMixin {
  @Shadow @Final private static Logger LOGGER;

  @Inject(
          method = "updateFallFlying",
          at = @At("HEAD"),
          cancellable = true
  )
  private void updateFallFlying(CallbackInfo callbackInfo) {
    LivingEntity self = (LivingEntity) (Object) this;
    try {
      if (self.level().dimension() != Level.END) {
        if (ConfigLoader.getConfig().isWarningMessageEnabled() && self instanceof ServerPlayer player && player.isFallFlying()) {
          player.displayClientMessage(Component.literal(ConfigLoader.getConfig().getFlightDisabledMessage()), true);
        }

        Method m = Entity.class.getDeclaredMethod("setSharedFlag", int.class, boolean.class);  // not a good solution, but probably will add more versions compatibility
        m.setAccessible(true);
        m.invoke(self, 7, false);
        callbackInfo.cancel();
      }
    } catch (Exception e) {
        LOGGER.error(e.getMessage());
    }
  }
}
