package net.mcreator.verse.procedures;

import org.joml.Vector3f;

import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.network.VerseModVariables;

import javax.annotation.Nullable;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Collection;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(Dist.CLIENT)
public class RenderOutfitProcedure {
	@SubscribeEvent
	public static void onPlayerRendered(RenderPlayerEvent.Pre event) {
		execute(event, event.getEntity(), (EntityModel) event.getRenderer().getModel(), event);
	}

	public static void offsetScale(PlayerModel model, Vector3f offset) {
		model.head.offsetScale(offset);
		model.head.y += offset.x() > 0 ? 0.05 : -0.05;
		model.body.offsetScale(offset);
		model.leftArm.offsetScale(offset);
		model.rightArm.offsetScale(offset);
		model.leftLeg.offsetScale(offset);
		model.rightLeg.offsetScale(offset);
		model.hat.offsetScale(offset);
		model.hat.y += offset.x() > 0 ? 0.05 : -0.05;
		model.jacket.offsetScale(offset);
		model.leftSleeve.offsetScale(offset);
		model.rightSleeve.offsetScale(offset);
		model.leftPants.offsetScale(offset);
		model.rightPants.offsetScale(offset);
	}

	public static Collection<Runnable> capes = new ConcurrentLinkedQueue<>();

	public static void renderHumanoid(RenderPlayerEvent playerRenderEvent, PlayerModel model, VertexConsumer vertexConsumer, boolean isFirstPerson) {
		PoseStack poseStack = playerRenderEvent.getPoseStack();
		((HumanoidModel) playerRenderEvent.getRenderer().getModel()).copyPropertiesTo(model);
		AbstractClientPlayer eventEntity_ = (AbstractClientPlayer) playerRenderEvent.getEntity();
		float partialTick = playerRenderEvent.getPartialTick();
		model.attackTime = eventEntity_.getAttackAnim(partialTick);
		float limbSwing = eventEntity_.walkAnimation.position(partialTick);
		float limbSwingAmount = eventEntity_.walkAnimation.speed(partialTick);
		float ageInTicks = eventEntity_.tickCount + partialTick;
		float interpolatedBodyYaw = Mth.rotLerp(partialTick, eventEntity_.yBodyRotO, eventEntity_.yBodyRot);
		float interpolatedHeadYaw = Mth.rotLerp(partialTick, eventEntity_.yHeadRotO, eventEntity_.yHeadRot);
		float netHeadYaw = interpolatedHeadYaw - interpolatedBodyYaw;
		float headPitch = Mth.lerp(partialTick, eventEntity_.xRotO, eventEntity_.getXRot());
		poseStack.pushPose();
		model.prepareMobModel(eventEntity_, limbSwing, limbSwingAmount, partialTick);
		CompoundTag playerData = eventEntity_.getPersistentData();
		float oldAnimationProgress = 0;
		float oldAgeInTicks = 0;
		if (playerData.contains("PlayerAnimationProgress")) {
			oldAnimationProgress = playerData.getFloat("PlayerAnimationProgress");
			oldAgeInTicks = playerData.getFloat("LastTickTime");
		}
		model.setupAnim(eventEntity_, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (playerData.contains("PlayerAnimationProgress") && playerData.getFloat("PlayerAnimationProgress") > 0) {
			playerData.putFloat("PlayerAnimationProgress", oldAnimationProgress);
			playerData.putFloat("LastTickTime", oldAgeInTicks);
		} else if (oldAnimationProgress > 0) {
			model.setupAnim(eventEntity_, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
		
		// Don't save/restore visibility - just set what we need for first person
		// Let the animation system control everything else
		if (isFirstPerson) {
			model.head.visible = false;
			model.hat.visible = false;
			model.body.visible = false;
			model.jacket.visible = false;
			model.leftLeg.visible = false;
			model.rightLeg.visible = false;
			model.leftPants.visible = false;
			model.rightPants.visible = false;
			model.leftSleeve.visible = false;
			model.rightSleeve.visible = false;
		}
		
		playerRenderEvent.getRenderer().setupRotations(eventEntity_, poseStack, ageInTicks, interpolatedBodyYaw, partialTick, 0);
		poseStack.scale(-0.938f, -0.938f, 0.938f);
		poseStack.translate(0.0D, -1.501, 0.0D);
		Vector3f offset = new Vector3f(0.015f);
		offsetScale(model, offset);
		if (!capes.isEmpty()) {
			capes.forEach(cape -> cape.run());
			capes.clear();
		}
		model.renderToBuffer(poseStack, vertexConsumer, playerRenderEvent.getPackedLight(), LivingEntityRenderer.getOverlayCoords(eventEntity_, 0));
		offset.negate();
		offsetScale(model, offset);
		
		// Don't restore anything - leave visibility as-is
		// The next frame will set it correctly anyway
		
		poseStack.popPose();
	}

	public static void renderEntity(RenderPlayerEvent playerRenderEvent, EntityModel model, VertexConsumer vertexConsumer) {
		PoseStack poseStack = playerRenderEvent.getPoseStack();
		playerRenderEvent.getRenderer().getModel().copyPropertiesTo(model);
		AbstractClientPlayer eventEntity_ = (AbstractClientPlayer) playerRenderEvent.getEntity();
		float partialTick = playerRenderEvent.getPartialTick();
		float limbSwing = eventEntity_.walkAnimation.position(partialTick);
		float limbSwingAmount = eventEntity_.walkAnimation.speed(partialTick);
		float ageInTicks = eventEntity_.tickCount + partialTick;
		float interpolatedBodyYaw = Mth.rotLerp(partialTick, eventEntity_.yBodyRotO, eventEntity_.yBodyRot);
		float interpolatedHeadYaw = Mth.rotLerp(partialTick, eventEntity_.yHeadRotO, eventEntity_.yHeadRot);
		float netHeadYaw = interpolatedHeadYaw - interpolatedBodyYaw;
		float headPitch = Mth.lerp(partialTick, eventEntity_.xRotO, eventEntity_.getXRot());
		poseStack.pushPose();
		playerRenderEvent.getRenderer().setupRotations(eventEntity_, poseStack, ageInTicks, interpolatedBodyYaw, partialTick, 0);
		poseStack.scale(-0.938f, -0.938f, 0.938f);
		poseStack.translate(0.0D, -1.501, 0.0D);
		model.prepareMobModel(eventEntity_, limbSwing, limbSwingAmount, partialTick);
		model.setupAnim(eventEntity_, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(poseStack, vertexConsumer, playerRenderEvent.getPackedLight(), LivingEntityRenderer.getOverlayCoords(eventEntity_, 0));
		poseStack.popPose();
	}

	public static void execute(Entity entity, EntityModel entityModel, RenderPlayerEvent playerRenderEvent) {
		execute(null, entity, entityModel, playerRenderEvent);
	}

	private static void execute(@Nullable Event event, Entity entity, EntityModel entityModel, RenderPlayerEvent playerRenderEvent) {
		if (entity == null || entityModel == null || playerRenderEvent == null)
			return;
		boolean slim = false;
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).outfitVisible == true) {
			if (!(entity.getData(VerseModVariables.PLAYER_VARIABLES).outfit).equals("")) {
				// Check if this is the local player in first person
				Minecraft mc = Minecraft.getInstance();
				// If ANY screen is open (inventory, custom GUI, etc.), treat as third person
				boolean isScreenOpen = mc.screen != null;
				boolean isFirstPerson = mc.player != null && mc.player.equals(entity) && mc.options.getCameraType().isFirstPerson() && !isScreenOpen;
				
				{
					ResourceLocation texture = GetTextureProcedure.execute(entity);
					renderHumanoid(playerRenderEvent, (PlayerModel) entityModel, playerRenderEvent.getMultiBufferSource().getBuffer(RenderType.armorCutoutNoCull(texture)), isFirstPerson);
				}
				
				// Hide vanilla clothing layers always
				((PlayerModel) entityModel).jacket.visible = false;
				((PlayerModel) entityModel).leftSleeve.visible = false;
				((PlayerModel) entityModel).rightSleeve.visible = false;
				((PlayerModel) entityModel).leftPants.visible = false;
				((PlayerModel) entityModel).rightPants.visible = false;
			}
		}
	}
}