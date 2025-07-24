package io.github.timtaran.deote.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class DisableElytraServerMixin {
  @Redirect(
          method = "updateFallFlying",
          at = @At(
                  value = "INVOKE",
                  target = "Lnet/minecraft/world/item/ElytraItem;isFlyEnabled(Lnet/minecraft/world/item/ItemStack;)Z"
          )
  )
  private boolean onCheckFlyEnabled(ItemStack itemStack) {
    LivingEntity self = (LivingEntity) (Object) this;
    if (self.level().dimension() != Level.END) {
      return false;
    }

    return ElytraItem.isFlyEnabled(itemStack);
  }
}
