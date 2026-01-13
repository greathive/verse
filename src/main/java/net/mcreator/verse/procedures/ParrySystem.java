package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.verse.network.PlayPlayerAnimationMessage;
import net.mcreator.verse.VerseMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class ParrySystem {
    
    /**
     * Handles parry timing on player tick.
     * Manages active parry frames and cooldown.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        
        CompoundTag data = player.getPersistentData();
        long currentTime = player.level().getGameTime();
        
        // Handle active parry frames
        if (data.contains("ParryActiveUntil")) {
            long parryActiveUntil = data.getLong("ParryActiveUntil");
            
            if (currentTime >= parryActiveUntil) {
                // Check if these were bonus frames
                boolean isBonusFrames = data.getBoolean("ParryBonusFrames");
                
                // Remove active parry state
                data.remove("ParryActiveUntil");
                data.remove("ParryBonusFrames");
                
                // Only set cooldown if NOT bonus frames
                if (!isBonusFrames) {
                    // Active parry frames ended without a successful parry
                    // Set parry cooldown
                    data.putLong("ParryCooldownUntil", currentTime + 25);
                    VerseMod.LOGGER.info("Parry window expired, setting 25 tick cooldown");
                } else {
                    VerseMod.LOGGER.info("Bonus parry frames expired, no cooldown");
                }
            }
        }
        
        // Clear cooldown when it expires
        if (data.contains("ParryCooldownUntil")) {
            long cooldownUntil = data.getLong("ParryCooldownUntil");
            if (currentTime >= cooldownUntil) {
                data.remove("ParryCooldownUntil");
                VerseMod.LOGGER.info("Parry cooldown expired");
            }
        }
        
    }
    
    /**
     * Handles attack cancellation on successful parry.
     * Priority HIGHEST to run before other damage handlers.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        Level level = player.level();
        if (level.isClientSide()) return;
        
        CompoundTag data = player.getPersistentData();
        long currentTime = level.getGameTime();
        
        // Check if player is in active parry frames
        if (!data.contains("ParryActiveUntil")) return;
        
        long parryActiveUntil = data.getLong("ParryActiveUntil");
        if (currentTime >= parryActiveUntil) return;
        
        VerseMod.LOGGER.info("SUCCESSFUL PARRY! Damage: " + event.getAmount() + ", Time remaining: " + (parryActiveUntil - currentTime));
        
        // Successful parry!
        // Cancel the entire attack event
        if (event instanceof ICancellableEvent cancellable) {
            cancellable.setCanceled(true);
        }
        
        // Grant bonus frames (6 more ticks)
        data.putLong("ParryActiveUntil", currentTime + 6);
        data.putBoolean("ParryBonusFrames", true);
        VerseMod.LOGGER.info("Granted 6 bonus parry frames!");
        
        // Play parry sound with random pitch on server
        float randomPitch = 0.75f + (player.getRandom().nextFloat() * 0.5f); // 0.75 to 1.25
        level.playSound(
            null,
            player.getX(), 
            player.getY(), 
            player.getZ(),
            BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:parry")),
            SoundSource.PLAYERS,
            1.0F,
            randomPitch
        );
        
        // Play success animation
        data.putString("PlayerCurrentAnimation", "verse:parrysuccess_fist1");
        data.putBoolean("OverrideCurrentAnimation", true);
        data.putBoolean("FirstPersonAnimation", true);
        
        // Broadcast success animation to all nearby players
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
            player,
            new PlayPlayerAnimationMessage(
                player.getId(),
                "verse:parrysuccess_fist1",
                true,
                true
            )
        );
    }
    
    /**
     * Checks if the player can currently parry.
     * Returns false if:
     * - On parry cooldown
     * - In damage immunity frames
     * - In custom weapon swing (unless in buffer time)
     */
    public static boolean canParry(Player player) {
        CompoundTag data = player.getPersistentData();
        long currentTime = player.level().getGameTime();
        
        // Check if on parry cooldown
        if (data.contains("ParryCooldownUntil")) {
            long cooldownUntil = data.getLong("ParryCooldownUntil");
            if (currentTime < cooldownUntil) {
                return false;
            }
        }
        
        // Check if in damage immunity frames
        if (player.invulnerableTime > 0) {
            // Set short cooldown and prevent parry
            data.putLong("ParryCooldownUntil", currentTime + 10);
            return false;
        }
        
        
        return true;
    }
    
    /**
     * Checks if the player can currently swing a weapon.
     * Returns false if they're in the no-swing period after a parry.
     */
    
    /**
     * Initiates a parry attempt.
     * Should be called when the player presses the parry key.
     */
    public static void initiateParry(Player player) {
        if (!canParry(player)) {
            return;
        }
        
        CompoundTag data = player.getPersistentData();
        long currentTime = player.level().getGameTime();
        
        // Set active parry frames (6 ticks)
        data.putLong("ParryActiveUntil", currentTime + 6);
        data.remove("ParryBonusFrames"); // This is NOT a bonus frame
        
        

        
        // Play parry animation
        data.putString("PlayerCurrentAnimation", "verse:parry_fist");
        data.putBoolean("OverrideCurrentAnimation", true);
        data.putBoolean("FirstPersonAnimation", true);
    }
    
    /**
     * Checks if the player is currently in active parry frames.
     * Used by other systems to prevent actions during parry.
     */
    public static boolean isInActiveParryFrames(Player player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains("ParryActiveUntil")) return false;
        
        long currentTime = player.level().getGameTime();
        long parryActiveUntil = data.getLong("ParryActiveUntil");
        
        return currentTime < parryActiveUntil;
    }
}