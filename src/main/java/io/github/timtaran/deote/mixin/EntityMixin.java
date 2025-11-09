package io.github.timtaran.deote.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityMixin {
    @Invoker("setSharedFlag")
    void invokeSetSharedFlag(int index, boolean value);
}
