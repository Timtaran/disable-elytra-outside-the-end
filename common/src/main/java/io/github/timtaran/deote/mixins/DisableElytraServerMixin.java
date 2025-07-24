package io.github.timtaran.deote.mixins;

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

@Mixin(LivingEntity.class)
public class DisableElytraServerMixin {
  @Shadow @Final private static Logger LOGGER;

  @Inject(
          method = "updateFallFlying",
          at = @At("HEAD"),
          cancellable = true
  )
  private void updateFallFlying(CallbackInfo callbackInfo) {
    LivingEntity self = (LivingEntity) (Object) this;
    if (self.level().dimension() != Level.END) {
      try {
        Method m = Entity.class.getDeclaredMethod("setSharedFlag", int.class, boolean.class);  // not a good solution, but probably will add more versions compatibility
        m.setAccessible(true);
        m.invoke(this, 7, false);
      } catch (Exception e) {
        LOGGER.error(e.getMessage());
      }
      callbackInfo.cancel();
    }
  }
}
