package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.WorkingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
//? if 1.21.1 {
/*import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
*///?} elif >=1.21.4 {
import org.spongepowered.asm.mixin.Shadow;
//?}
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class PreventClientFlyingMixin {
    //? if >=1.21.4 {
    @Shadow
    protected abstract boolean canGlide();
    //?}


    @Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
    private void onTryToStartFallFlying(CallbackInfoReturnable<Boolean> callbackInfo) {
        Player self = (Player) (Object) this;

        if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FLYING && canFallFlying(self) && self.level().dimension() != Level.END) {
            if (GlobalStorage.deoteConfig.warningMessageEnabled) {
                Minecraft.getInstance().gui.setOverlayMessage(Component.literal(GlobalStorage.deoteConfig.flightDisabledMessage), false);
            }

            callbackInfo.setReturnValue(false);
            callbackInfo.cancel();
        }
    }

    @Unique
    private boolean canFallFlying(Player self) {
        //? if 1.21.1 {
        /*ItemStack itemStack = self.getItemBySlot(EquipmentSlot.CHEST);
        return !self.onGround() && !self.isFallFlying() && !self.isInWater() && !self.hasEffect(MobEffects.LEVITATION) &&
                itemStack.is(Items.ELYTRA) &&
                //? if fabric {
                ElytraItem.isFlyEnabled(itemStack);
                //?} elif neoforge {
                /^itemStack.canElytraFly(self);
                ^///?}
        *///?} elif >= 1.21.4 {
        return (!self.isFallFlying() && canGlide() && !self.isInWater());
         //?}
    }
}
