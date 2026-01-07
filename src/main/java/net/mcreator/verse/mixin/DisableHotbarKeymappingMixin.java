package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

@Mixin(KeyMapping.class)
public abstract class DisableHotbarKeymappingMixin {
    
    @Inject(method = "consumeClick", at = @At("HEAD"), cancellable = true)
    public void injectConsumeClick(CallbackInfoReturnable<Boolean> cir) {
        KeyMapping keyMapping = (KeyMapping) (Object) this;
        
        if (keyMapping.getName().contains("key.hotbar")) {
            LocalPlayer player = Minecraft.getInstance().player;
            
            if (player != null) {
                // Check if player has guardbroken effect
                if (player.hasEffect(net.mcreator.verse.init.VerseModMobEffects.GUARDBROKEN)) {
                    cir.setReturnValue(false);
                    return;
                }
                
                // Check if currently in a swing
                CompoundTag data = player.getPersistentData();
                
                if (data.contains("CustomAttackCooldownUntil")) {
                    long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
                    long currentTime = player.level().getGameTime();
                    long ticksRemaining = cooldownUntil - currentTime;
                    
                    // Prevent key press if on cooldown (except last 5 ticks)
                    if (ticksRemaining > 5) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}