package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Camera;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Accessor
    void setDetached(boolean value);
}