package net.mcreator.verse.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.verse.entity.NpcEntity;
import net.mcreator.verse.client.model.Modelnpcmodel;

public class NpcRenderer extends MobRenderer<NpcEntity, Modelnpcmodel<NpcEntity>> {
	public NpcRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelnpcmodel<NpcEntity>(context.bakeLayer(Modelnpcmodel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(NpcEntity entity) {
		return ResourceLocation.parse("verse:textures/entities/375bba550ac47a0e.png");
	}
}