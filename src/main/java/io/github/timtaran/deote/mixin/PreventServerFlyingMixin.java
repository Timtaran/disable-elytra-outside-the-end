package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.config.WorkingMode;
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
public class PreventServerFlyingMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(
            method = "updateFallFlying",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateFallFlying(CallbackInfo callbackInfo) {
        if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FLYING) {
            LivingEntity self = (LivingEntity) (Object) this;
            try {
                if (self.level().dimension() != Level.END) {
                    if (GlobalStorage.deoteConfig.warningMessageEnabled && self instanceof ServerPlayer player && player.isFallFlying() && !self.level().isClientSide()) {
                        player.displayClientMessage(Component.literal(GlobalStorage.deoteConfig.flightDisabledMessage), true);
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
}
