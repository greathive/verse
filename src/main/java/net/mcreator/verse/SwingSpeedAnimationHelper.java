package net.mcreator.verse;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.mcreator.verse.entity.NpcEntity;

public class SwingSpeedAnimationHelper {
    
    /**
     * Calculates the animation speed multiplier for an entity based on its type and animation name.
     * 
     * @param entity The entity playing the animation
     * @param animationName The name of the animation being played
     * @return The speed multiplier (1.0 = normal speed, 2.0 = double speed, 0.5 = half speed)
     */
    public static float getAnimationSpeedMultiplier(LivingEntity entity, String animationName) {
        // Only modify speed for animations containing "swing"
        if (!animationName.toLowerCase().contains("swing")) {
            return 1.0f;
        }
        
        // Player: Use vanilla attack speed attribute
        if (entity instanceof Player player) {
            return getPlayerSwingSpeed(player);
        }
        
        // NPC: Use custom verse:swing_speed attribute
        if (entity instanceof NpcEntity npc) {
            return getNpcSwingSpeed(npc);
        }
        
        // Other mobs: No speed modification
        return 1.0f;
    }
    
    /**
     * Gets the swing speed multiplier for a player based on their attack speed attribute.
     * Base attack speed is 4.0, which we normalize to 1.0 animation speed.
     */
    private static float getPlayerSwingSpeed(Player player) {
        double attackSpeed = player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED);
        // Base attack speed is 4.0, normalize to that
        return (float) (attackSpeed / 1.6);
    }
    
    /**
     * Gets the swing speed multiplier for an NPC based on the verse:swing_speed attribute.
     * Returns 1.0 if the attribute doesn't exist.
     */
    private static float getNpcSwingSpeed(NpcEntity npc) {
        try {
            // Try to get the custom attribute holder
            net.minecraft.core.Holder<Attribute> swingSpeedAttr = BuiltInRegistries.ATTRIBUTE
                .getHolder(ResourceLocation.fromNamespaceAndPath("verse", "swing_speed"))
                .orElse(null);
            
            if (swingSpeedAttr != null) {
                AttributeInstance attrInstance = npc.getAttribute(swingSpeedAttr);
                if (attrInstance != null) {
                    return (float) attrInstance.getValue();
                }
            }
        } catch (Exception e) {
            VerseMod.LOGGER.warn("Failed to get swing_speed attribute for NPC: " + e.getMessage());
        }
        
        // Default to normal speed if attribute doesn't exist
        return 1.0f;
    }
    
    /**
     * Checks if an animation name should have speed modification applied.
     */
    public static boolean shouldModifySpeed(String animationName) {
        return animationName != null && animationName.toLowerCase().contains("swing");
    }
}