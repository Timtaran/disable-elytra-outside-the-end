package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.WorkingMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;

@Mixin(value = FireworkRocketItem.class)
public class PreventFireworkBoostingMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void init(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FIREWORKS) {
            try {
                if (level.dimension() != Level.END) {
                    Method m = Entity.class.getDeclaredMethod("getSharedFlag", int.class);  // not a good solution, but I forgot how I have done this before
                    m.setAccessible(true);
                    boolean isGliding = (boolean) m.invoke(player, 7);

                    if (isGliding) {
                        callbackInfo.setReturnValue(InteractionResult.PASS);

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