package net.mcreator.verse.client;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class DisableScrollForMantra {
    @SubscribeEvent
    public static void Scroll(InputEvent.MouseScrollingEvent event) {
        Entity entity = Minecraft.getInstance().player;
        if (entity == null)
            return;
        
        if (entity.getPersistentData().getDouble("mantraselectiontick") != 0 
            && entity.tickCount - entity.getPersistentData().getDouble("mantraselectiontick") <= 100) {
            
            double currentSelected = entity.getPersistentData().getDouble("selected");
            double scrollDelta = event.getScrollDeltaY();
            
            if (scrollDelta > 0) {
                // Scrolling down - subtract 1 (wrap to 9 if at 0)
                if (currentSelected > 1) {
                    entity.getPersistentData().putDouble("selected", currentSelected - 1);
                } else {
                    entity.getPersistentData().putDouble("selected", 9);
                }
            } else if (scrollDelta < 0) {
                // Scrolling up - add 1 (wrap to 0 if at 9)
                if (currentSelected < 9) {
                    entity.getPersistentData().putDouble("selected", currentSelected + 1);
                } else {
                    entity.getPersistentData().putDouble("selected", 1);
                }
            }
            
            event.setCanceled(true);
        }
    }
}