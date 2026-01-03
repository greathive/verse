package net.mcreator.verse.procedures;

import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.client.model.ModelarmModelSlim;
import net.mcreator.verse.client.model.ModelarmModelLarge;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(value = {Dist.CLIENT})
public class KleidersHideArmProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onEventTriggered(RenderArmEvent event) {
		execute(event, event.getPlayer());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		RenderArmEvent _evt = (RenderArmEvent) event;
		Minecraft mc = Minecraft.getInstance();
		EntityRenderDispatcher dis = mc.getEntityRenderDispatcher();
		Entity _evtEntity = _evt.getPlayer();
		PlayerRenderer playerRenderer = (PlayerRenderer) dis.getRenderer(_evt.getPlayer());
		EntityRendererProvider.Context context = new EntityRendererProvider.Context(dis, mc.getItemRenderer(), mc.getBlockRenderer(), dis.getItemInHandRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.font);
		MultiBufferSource bufferSource = _evt.getMultiBufferSource();
		int packedLight = _evt.getPackedLight();
		PoseStack poseStack = _evt.getPoseStack();
		PlayerModel<AbstractClientPlayer> playerModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
		playerModel.attackTime = 0.0F;
		playerModel.crouching = false;
		playerModel.swimAmount = 0.0F;
		playerModel.setupAnim(_evt.getPlayer(), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		playerModel.leftArm.xRot = 0.0F;
		playerModel.rightArm.xRot = 0.0F;
		HumanoidArm arm = _evt.getArm();
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).outfitVisible == true) {
			if (!(entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("")) {
				{
					ResourceLocation _texture = ResourceLocation.parse("kleiders_custom_renderer:textures/entities/default.png");
					if (ResourceLocation.tryParse((_evtEntity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) _evtEntity).getSkin().texture().toString() : "kleiders_custom_renderer:textures/entities/default.png")) != null) {
						_texture = ResourceLocation.parse((_evtEntity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) _evtEntity).getSkin().texture().toString() : "kleiders_custom_renderer:textures/entities/default.png"));
					}
					PlayerModel<AbstractClientPlayer> newModel = new PlayerModel<>(
							context.bakeLayer((_evtEntity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) _evtEntity).getSkin().model() == PlayerSkin.Model.SLIM : false) ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER),
							(_evtEntity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) _evtEntity).getSkin().model() == PlayerSkin.Model.SLIM : false));
					newModel.leftArm.copyFrom(playerModel.leftArm);
					newModel.rightArm.copyFrom(playerModel.rightArm);
					if (arm == HumanoidArm.LEFT) {
						newModel.leftArm.render(_evt.getPoseStack(), bufferSource.getBuffer(RenderType.entityTranslucentCull(_texture)), packedLight, OverlayTexture.NO_OVERLAY);
					} else {
						newModel.rightArm.render(_evt.getPoseStack(), bufferSource.getBuffer(RenderType.entityTranslucentCull(_texture)), packedLight, OverlayTexture.NO_OVERLAY);
					}
				}
				if (_evtEntity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) _evtEntity).getSkin().model() == PlayerSkin.Model.SLIM : false) {
					{
						ResourceLocation _texture = ResourceLocation.parse("kleiders_custom_renderer:textures/entities/default.png");
						if (ResourceLocation.tryParse(("verse:textures/entities/@.png".replace("@", entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit))) != null) {
							_texture = ResourceLocation.parse(("verse:textures/entities/@.png".replace("@", entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit)));
						}
						ModelarmModelSlim newModel = new ModelarmModelSlim(context.bakeLayer(ModelarmModelSlim.LAYER_LOCATION));
						newModel.LeftArm.copyFrom(playerModel.leftArm);
						newModel.RightArm.copyFrom(playerModel.rightArm);
						if (arm == HumanoidArm.LEFT) {
							newModel.LeftArm.render(_evt.getPoseStack(), bufferSource.getBuffer(RenderType.entityTranslucentCull(_texture)), packedLight, OverlayTexture.NO_OVERLAY);
						} else {
							newModel.RightArm.render(_evt.getPoseStack(), bufferSource.getBuffer(RenderType.entityTranslucentCull(_texture)), packedLight, OverlayTexture.NO_OVERLAY);
						}
					}
				} else {
					{
						ResourceLocation _texture = ResourceLocation.parse("kleiders_custom_renderer:textures/entities/default.png");
						if (ResourceLocation.tryParse(("verse:textures/entities/@.png".replace("@", entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit))) != null) {
							_texture = ResourceLocation.parse(("verse:textures/entities/@.png".replace("@", entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit)));
						}
						ModelarmModelLarge newModel = new ModelarmModelLarge(context.bakeLayer(ModelarmModelLarge.LAYER_LOCATION));
						newModel.LeftArm.copyFrom(playerModel.leftArm);
						newModel.RightArm.copyFrom(playerModel.rightArm);
						if (arm == HumanoidArm.LEFT) {
							newModel.LeftArm.render(_evt.getPoseStack(), bufferSource.getBuffer(RenderType.entityTranslucentCull(_texture)), packedLight, OverlayTexture.NO_OVERLAY);
						} else {
							newModel.RightArm.render(_evt.getPoseStack(), bufferSource.getBuffer(RenderType.entityTranslucentCull(_texture)), packedLight, OverlayTexture.NO_OVERLAY);
						}
					}
				}
				_evt.setCanceled(true);
			}
		}
	}
}