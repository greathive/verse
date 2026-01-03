package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Options;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.util.SwingDataLoader;
import net.mcreator.verse.network.CustomAttackPacket;
import net.mcreator.verse.init.VerseModMobEffects;

@Mixin(Minecraft.class)
public class ContinuousAttackMixin {
    @Shadow private LocalPlayer player;
    @Shadow private Options options;
    @Shadow private HitResult hitResult;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void handleContinuousAttack(CallbackInfo ci) {
        // Early exit - only run if left-click is held
        if (!this.options.keyAttack.isDown()) {
            // Clear mining flag when not attacking
            if (this.player != null) {
                this.player.getPersistentData().remove("MiningInitiated");
            }
            return;
        }
        
        if (this.player == null) return;
        
        ItemStack mainHand = this.player.getMainHandItem();
        
        // Get swing data - this will return null if not a valid weapon
        SwingDataLoader.SwingData swingData = SwingDataLoader.getSwingData(mainHand);
        if (swingData == null) return;
        
        CompoundTag data = this.player.getPersistentData();
        
        // If mining was initiated (initial click was on a block), don't do custom attacks
        if (data.getBoolean("MiningInitiated")) {
            return;
        }
        
        // If currently looking at a block, don't start custom attack
        if (this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
            return;
        }
        
        // Check if can attack (no effect blocking it)
        if (this.player.hasEffect(VerseModMobEffects.NO_ATTACK)) return;
        
        long currentTime = this.player.level().getGameTime();
        int comboBuffer = 5;
        
        // Check if cooldown allows attack
        boolean canAttack = false;
        if (data.contains("CustomAttackCooldownUntil")) {
            long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
            long ticksRemaining = cooldownUntil - currentTime;
            
            // Allow attack if in combo buffer window
            if (ticksRemaining <= comboBuffer) {
                canAttack = true;
            }
        } else {
            // No cooldown active
            canAttack = true;
        }
        
        if (!canAttack) return;
        
        // Calculate cooldown based on attack speed
        double attackSpeed = this.player.getAttributeValue(Attributes.ATTACK_SPEED);
        long cooldownTicks = Math.round(20.0 / attackSpeed);
        
        // Set when this attack started and when it ends ON CLIENT (needed for trail renderer)
        data.putLong("AttackStartTime", currentTime);
        data.putLong("CustomAttackCooldownUntil", currentTime + cooldownTicks);
        
        // Send packet to SERVER
        PacketDistributor.sendToServer(new CustomAttackPacket());
        
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
    }
}