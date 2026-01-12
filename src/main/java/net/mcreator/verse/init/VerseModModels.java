/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.verse.client.model.Modelnpcmodel;
import net.mcreator.verse.client.model.ModelarmModelSlim;
import net.mcreator.verse.client.model.ModelarmModelLarge;
import net.mcreator.verse.client.model.ModelMegalodaunt;
import net.mcreator.verse.client.model.ModelKCRPlayerModelBaseJava;

@EventBusSubscriber(Dist.CLIENT)
public class VerseModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelKCRPlayerModelBaseJava.LAYER_LOCATION, ModelKCRPlayerModelBaseJava::createBodyLayer);
		event.registerLayerDefinition(ModelMegalodaunt.LAYER_LOCATION, ModelMegalodaunt::createBodyLayer);
		event.registerLayerDefinition(ModelarmModelLarge.LAYER_LOCATION, ModelarmModelLarge::createBodyLayer);
		event.registerLayerDefinition(Modelnpcmodel.LAYER_LOCATION, Modelnpcmodel::createBodyLayer);
		event.registerLayerDefinition(ModelarmModelSlim.LAYER_LOCATION, ModelarmModelSlim::createBodyLayer);
	}
}