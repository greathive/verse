package net.mcreator.verse.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.EventBusSubscriber;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class VanillaOverlayHider {
    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        Entity entity = Minecraft.getInstance().player;
        if (entity == null)
            return;
        
        if (entity.getPersistentData().getDouble("mantraselectiontick") != 0 
            && entity.tickCount - entity.getPersistentData().getDouble("mantraselectiontick") <= 100) {
            
            if (event.getName().equals(VanillaGuiLayers.HOTBAR)) {
                event.setCanceled(true);
            }
            if (event.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR)) {
                event.setCanceled(true);
            }
        }
    }
}