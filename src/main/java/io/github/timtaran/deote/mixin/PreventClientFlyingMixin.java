package io.github.timtaran.deote.mixin;

import io.github.timtaran.deote.GlobalStorage;
import io.github.timtaran.deote.config.WorkingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Player.class)
public abstract class PreventClientFlyingMixin {
    @Shadow
    protected abstract boolean canGlide();

    /**
     * @author timtaran
     * @reason Replacing return value doesn't work, so I guess it would be easier to replace function than rewrite original function and call it twice.
     */
    @Overwrite
    public boolean tryToStartFallFlying() {
        Player self = (Player) (Object) this;

        if (!self.isFallFlying() && canGlide() && !self.isInWater()) {
            if (GlobalStorage.deoteConfig.workingMode == WorkingMode.FLYING) {
                if (self.level().dimension() != Level.END) {
                    if (GlobalStorage.deoteConfig.warningMessageEnabled) {
                        Minecraft.getInstance().gui.setOverlayMessage(Component.literal(GlobalStorage.deoteConfig.flightDisabledMessage), false);
                    }
                    return false;
                } else {
                    self.startFallFlying();
                    return true;
                }
            } else {
                self.stopFallFlying();
                return true;
            }
        } else {
            return false;
        }
    }
}
