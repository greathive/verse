package net.mcreator.verse.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

@Mixin(KeyMapping.class)
public abstract class DisableHotbarDuringMantraSelectionMixin {
    
    @Shadow
    public abstract String getName();
    
    @Shadow
    private int clickCount;
    
    @Inject(method = "consumeClick", at = @At("HEAD"), cancellable = true)
    public void inject1(CallbackInfoReturnable<Boolean> cir) {
        // Only process if there's actually a click to consume
        if (this.clickCount <= 0) {
            return;
        }
        
        String keyName = this.getName();
        
        if (keyName.startsWith("key.hotbar.")) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                double mantraSelectionTick = player.getPersistentData().getDouble("mantraselectiontick");
                
                if (mantraSelectionTick != 0 && player.tickCount - mantraSelectionTick <= 100) {
                    try {
                        // Extract the number from "key.hotbar.X"
                        int slotNumber = Integer.parseInt(keyName.substring(11));
                        
                        // Set the selected mantra slot
                        player.getPersistentData().putDouble("selected", slotNumber);
                        
                        
                        // Manually decrement clickCount to consume the click
                        this.clickCount--;
                        
                        // Return false to indicate no click should be processed
                        cir.setReturnValue(false);
                    } catch (NumberFormatException e) {
                        System.err.println("Failed to parse hotbar key: " + keyName);
                    }
                }
            }
        }
    }
}