package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(modid = "verse", value = Dist.CLIENT)
public class PreventActionsOnCooldown {

    // Prevent hotbar scrolling
    @SubscribeEvent
    public static void onScroll(InputEvent.MouseScrollingEvent event) {
        Entity entity = Minecraft.getInstance().player;
        if (entity == null) return;

        // Check if holding custom weapon
        if (entity instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.is(ItemTags.create(ResourceLocation.parse("verse:tools")))) {
                // Prevent scrolling during cooldown (except last 5 ticks for combo window)
                if (isOnCooldown(entity, 5)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    // Prevent item swapping (F key)
    @SubscribeEvent
    public static void onItemSwap(LivingSwapItemsEvent.Hands event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.is(ItemTags.create(ResourceLocation.parse("verse:tools")))) {
                // Prevent swapping during cooldown (except last 5 ticks for combo window)
                if (isOnCooldown(player, 5)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    private static boolean isOnCooldown(Entity entity, int ticksBeforeEnd) {
        if (entity == null || entity.level() == null) return false;

        CompoundTag data = entity.getPersistentData();
        if (!data.contains("CustomAttackCooldownUntil")) {
            return false;
        }

        long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
        long currentTime = entity.level().getGameTime();

        // Return true if still on cooldown AND not in the last X ticks
        return currentTime < cooldownUntil && (cooldownUntil - currentTime) > ticksBeforeEnd;
    }
}