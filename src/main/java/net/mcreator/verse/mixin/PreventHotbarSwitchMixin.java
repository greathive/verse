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

import net.mcreator.verse.init.VerseModMobEffects;

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
            CompoundTag data = player.getPersistentData();
            
            boolean shouldLockHotbar = false;
            
            // Check if player has guardbroken effect - if your effect exists, uncomment this:
           if (player.hasEffect(VerseModMobEffects.GUARDBROKEN)) {
                shouldLockHotbar = true;
             }
            

            
            if (shouldLockHotbar) {
                // Track the slot we were using
                if (!data.contains("LockedHotbarSlot")) {
                    data.putInt("LockedHotbarSlot", player.getInventory().selected);
                }
                
                int lockedSlot = data.getInt("LockedHotbarSlot");
                
                // Force back to locked slot
                if (player.getInventory().selected != lockedSlot) {
                    player.getInventory().selected = lockedSlot;
                }
            } else {
                // No restrictions, clear lock
                data.remove("LockedHotbarSlot");
            }
        }
    }
}