package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.WorkingMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
//? if 1.21.1 {
/*import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
*///?} elif >=1.21.4 {
import net.minecraft.world.InteractionResult;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = FireworkRocketItem.class)
public class PreventFireworkBoostingMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void init(
            Level level,
            Player player,
            InteractionHand interactionHand,
            //? if 1.21.1 {
            /*CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfo)
             *///?} elif >= 1.21.4 {
            CallbackInfoReturnable<InteractionResult> callbackInfo)
    //?}
    {
        if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FIREWORKS) {
            try {
                if (!GlobalStorage.deoteConfig.dimensionList.contains(level.dimension().location().toString())) {
                    if (player.isFallFlying()) {
                        //? if 1.21.1 {
                        /*callbackInfo.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(interactionHand)));
                         *///?} elif >= 1.21.4 {
                        callbackInfo.setReturnValue(InteractionResult.PASS);
                        //?}

                        if (GlobalStorage.deoteConfig.warningMessageEnabled) {
                            if (!level.isClientSide()) {
                                player.displayClientMessage(Component.literal(GlobalStorage.deoteConfig.fireworksDisabledMessage), true);
                            }
                        }

                        callbackInfo.cancel();
                    }
                }
            } catch (Exception exception) {
                DisableElytraOutsideTheEnd.LOGGER.error(exception.getMessage());
            }
        }
    }
}