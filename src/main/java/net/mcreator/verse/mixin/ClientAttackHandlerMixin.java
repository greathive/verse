package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.util.SwingDataLoader;
import net.mcreator.verse.init.VerseModMobEffects;

@Mixin(Minecraft.class)
public class ClientAttackHandlerMixin {
    @Shadow private LocalPlayer player;
    @Shadow private HitResult hitResult;
    
    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player == null) return;
        
        ItemStack mainHand = this.player.getMainHandItem();
        
        // Get swing data - this will return null if not a valid weapon
        SwingDataLoader.SwingData swingData = SwingDataLoader.getSwingData(mainHand);
        if (swingData == null) return;
        
        CompoundTag data = this.player.getPersistentData();
        
        // If clicking on a block, mark mining as initiated and allow it
        if (this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
            data.putBoolean("MiningInitiated", true);
            return; // Don't cancel, allow vanilla block breaking
        }
        
        // Check if can attack (no effect blocking it)
        if (this.player.hasEffect(VerseModMobEffects.NO_ATTACK)) {
            cir.setReturnValue(false);
            return;
        }
        
        // Get persistent data for cooldown tracking
        long currentTime = this.player.level().getGameTime();
        
        // Calculate how many ticks before end we allow next attack (buffer for smooth combos)
        int comboBuffer = -2;
        
        // Check custom cooldown - allow attack in last few ticks
        if (data.contains("CustomAttackCooldownUntil")) {
            long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
            long ticksRemaining = cooldownUntil - currentTime;
            
            // Only block if we're still on cooldown AND not in the combo buffer window
            if (ticksRemaining > comboBuffer) {
                cir.setReturnValue(false);
                return;
            }
        }
        
        // Calculate cooldown based on attack speed
        double attackSpeed = this.player.getAttributeValue(Attributes.ATTACK_SPEED);
        long cooldownTicks = Math.round(20.0 / attackSpeed);
        
        // Set when this attack started and when it ends ON CLIENT (needed for trail renderer)
        data.putLong("AttackStartTime", currentTime);
        data.putLong("CustomAttackCooldownUntil", currentTime + cooldownTicks);
        
        // Cycle swing counter based on swing count from JSON
        int swing = data.getInt("SwingCounter");
        swing = swing >= swingData.swingCount ? 1 : swing + 1;
        data.putInt("SwingCounter", swing);
        
        // Play animation using anim type from JSON
        String animName = "verse:swing_" + swingData.animType + swing;
        data.putString("PlayerCurrentAnimation", animName);
        data.putBoolean("OverrideCurrentAnimation", true);
        data.putBoolean("FirstPersonAnimation", true);
        
        // Clear hit entities for new swing
        data.remove("HitEntitiesThisSwing");
        data.remove("LastDamageTick");
        
        // Trigger swing on client for immediate feedback
        this.player.swing(this.player.getUsedItemHand());
        
        // Cancel vanilla attack
        cir.setReturnValue(false);
    }
}