package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;

@Mixin(Player.class)
public class PreventHotbarSwitchMixin {
    @Shadow
    public Inventory getInventory() {
        return null;
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void preventHotbarSwitchDuringCooldown(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        
        if (player.level().isClientSide()) {
            ItemStack mainHand = player.getMainHandItem();
            
            // If holding custom weapon and on cooldown, reset selected slot to prevent switching
            if (mainHand.is(ItemTags.create(ResourceLocation.parse("verse:tools")))) {
                CompoundTag data = player.getPersistentData();
                
                if (data.contains("CustomAttackCooldownUntil")) {
                    long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
                    long currentTime = player.level().getGameTime();
                    long ticksRemaining = cooldownUntil - currentTime;
                    
                    // Track the slot we were using
                    if (!data.contains("LockedHotbarSlot")) {
                        data.putInt("LockedHotbarSlot", player.getInventory().selected);
                    }
                    
                    int lockedSlot = data.getInt("LockedHotbarSlot");
                    
                    // Force back to locked slot if on cooldown (except last 5 ticks)
                    if (ticksRemaining > 5) {
                        if (player.getInventory().selected != lockedSlot) {
                            player.getInventory().selected = lockedSlot;
                        }
                    } else {
                        // Cooldown ending, clear lock
                        data.remove("LockedHotbarSlot");
                    }
                } else {
                    // No cooldown, clear lock
                    data.remove("LockedHotbarSlot");
                }
            } else {
                // Not holding custom weapon, clear lock
                player.getPersistentData().remove("LockedHotbarSlot");
            }
        }
    }
}