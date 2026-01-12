package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.util.SwingDataLoader;
import net.mcreator.verse.procedures.ParrySystem;

@Mixin(MultiPlayerGameMode.class)
public class PreventMiningOnCooldownMixin {
    
    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void preventMining(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        
        if (player == null) return;
        
        // Prevent mining during active parry frames
        if (ParrySystem.isInActiveParryFrames(player)) {
            cir.setReturnValue(false);
            return;
        }
        
        ItemStack mainHand = player.getMainHandItem();
        
        // Check if holding a custom weapon
        if (SwingDataLoader.getSwingData(mainHand) != null) {
            CompoundTag data = player.getPersistentData();
            
            // Mark that mining was initiated (initial click was on a block)
            data.putBoolean("MiningInitiated", true);
            
            // Don't prevent if no cooldown
            if (!data.contains("CustomAttackCooldownUntil")) {
                return;
            }
            
            long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
            long currentTime = player.level().getGameTime();
            
            // Prevent mining if on cooldown
            if (currentTime < cooldownUntil) {
                cir.setReturnValue(false);
            }
        }
    }
    
    @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void preventContinueMining(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        
        if (player == null) return;
        
        // Prevent mining during active parry frames
        if (ParrySystem.isInActiveParryFrames(player)) {
            cir.setReturnValue(false);
            return;
        }
        
        ItemStack mainHand = player.getMainHandItem();
        
        // Check if holding a custom weapon
        if (SwingDataLoader.getSwingData(mainHand) != null) {
            CompoundTag data = player.getPersistentData();
            
            // Only allow continuing if mining was initiated
            if (!data.getBoolean("MiningInitiated")) {
                cir.setReturnValue(false);
                return;
            }
            
            // Don't prevent if no cooldown
            if (!data.contains("CustomAttackCooldownUntil")) {
                return;
            }
            
            long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
            long currentTime = player.level().getGameTime();
            
            // Prevent mining if on cooldown
            if (currentTime < cooldownUntil) {
                cir.setReturnValue(false);
            }
        }
    }
}