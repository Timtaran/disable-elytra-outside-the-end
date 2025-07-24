package io.github.timtaran.deote.mixins;

import net.minecraft.world.entity.LivingEntity;
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
                  target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"
          )
  )
  private boolean onCheckCanElytraFly(ItemStack stack, LivingEntity entity) {
    if (entity.level().dimension() != Level.END) {
      return false;
    }
    return stack.canElytraFly(entity);
  }
}