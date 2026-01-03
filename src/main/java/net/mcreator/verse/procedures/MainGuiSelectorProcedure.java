package net.mcreator.verse.procedures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.glfw.GLFW;

import org.joml.Matrix4fStack;
import org.joml.Matrix4f;

import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.world.inventory.MainGuiMenu;
import net.mcreator.verse.network.VerseModVariables;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.Lighting;

@EventBusSubscriber(value = Dist.CLIENT)
public class MainGuiSelectorProcedure {
	private static Map<EntityType, Entity> data = new HashMap<>();
	private static GuiGraphics guiGraphics = null;
	private static float partialTick = 0.0F;
	private static int currentStage = 0;
	private static int targetStage = 0; // NONE: 0, ALWAYS: 1, GAME: 2, GUI: 3

	private static boolean target(int targetStage) {
		if (targetStage == currentStage) {
			MainGuiSelectorProcedure.targetStage = targetStage;
			return true;
		} else if (targetStage == 1) {
			if (currentStage != 0) {
				MainGuiSelectorProcedure.targetStage = currentStage;
				return true;
			}
		}
		return false;
	}

	private static void release() {
		targetStage = 0;
	}

	public static void renderEntity(LevelAccessor levelAccessor, EntityType type, double x, double y, double depth, float yaw, float pitch, float roll, float scale, boolean modelOnly) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		if (type == null)
			return;
		if (levelAccessor instanceof ClientLevel level && Minecraft.getInstance().gameRenderer.getMainCamera() != null) {
			Entity entity = null;
			if (data.containsKey(type)) {
				entity = data.get(type);
			} else {
				entity = type.create(level);
				data.put(type, entity);
			}
			renderEntity(entity, 0.0F, x, y, depth, yaw, pitch, roll, scale, modelOnly);
		}
	}

	public static void renderEntity(LevelAccessor levelAccessor, Entity entity, double x, double y, double depth, float yaw, float pitch, float roll, float scale, boolean modelOnly) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		Minecraft minecraft = Minecraft.getInstance();
		if (levelAccessor instanceof ClientLevel && minecraft.gameRenderer.getMainCamera() != null)
			renderEntity(entity, partialTick, x, y, depth, yaw, pitch, roll, scale, modelOnly);
	}

	private static void renderEntity(Entity entity, float partialTick, double x, double y, double depth, float yaw, float pitch, float roll, float scale, boolean modelOnly) {
		if (entity == null)
			return;
		float offset = entity.getBbHeight() / 2.0F;
		float yRotO = entity.yRotO;
		float yRot = entity.getYRot();
		float xRotO = entity.xRotO;
		float xRot = entity.getXRot();
		entity.yRotO = 180.0F;
		entity.setYRot(180.0F);
		entity.xRotO = 0.0F;
		entity.setXRot(0.0F);
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(x, y, -depth);
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll + 180.0F));
		poseStack.mulPose(new Matrix4f().scaling(scale, scale, -scale));
		poseStack.translate(0.0F, -offset, 0.0F);
		Lighting.setupForEntityInInventory();
		if (entity instanceof LivingEntity livingEntity) {
			float yBodyRotO = livingEntity.yBodyRotO;
			float yBodyRot = livingEntity.yBodyRot;
			float yHeadRotO = livingEntity.yHeadRotO;
			float yHeadRot = livingEntity.yHeadRot;
			livingEntity.yBodyRotO = 180.0F;
			livingEntity.yBodyRot = 180.0F;
			livingEntity.yHeadRotO = 180.0F;
			livingEntity.yHeadRot = 180.0F;
			renderEntity(livingEntity, partialTick, poseStack, modelOnly);
			livingEntity.yBodyRotO = yBodyRotO;
			livingEntity.yBodyRot = yBodyRot;
			livingEntity.yHeadRotO = yHeadRotO;
			livingEntity.yHeadRot = yHeadRot;
		} else {
			renderEntity(entity, partialTick, poseStack, modelOnly);
		}
		Lighting.setupFor3DItems();
		poseStack.popPose();
		entity.yRotO = yRotO;
		entity.setYRot(yRot);
		entity.xRotO = xRotO;
		entity.setXRot(xRot);
	}

	private static void renderEntity(Entity entity, float partialTick, PoseStack poseStack, boolean modelOnly) {
		Minecraft minecraft = Minecraft.getInstance();
		if (modelOnly) {
			boolean customNameVisible = entity.isCustomNameVisible();
			entity.setCustomNameVisible(false);
			minecraft.getEntityRenderDispatcher().getRenderer(entity).render(entity, 0.0F, partialTick, poseStack, guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
			guiGraphics.flush();
			entity.setCustomNameVisible(customNameVisible);
		} else {
			EntityRenderDispatcher renderer = minecraft.getEntityRenderDispatcher();
			renderer.setRenderShadow(false);
			renderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, poseStack, guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
			guiGraphics.flush();
			renderer.setRenderShadow(true);
		}
	}

	public static void renderItem(LevelAccessor levelAccessor, ItemStack itemStack, double x, double y, double depth, float yaw, float pitch, float roll, float scale) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		if (itemStack.isEmpty())
			return;
		Minecraft minecraft = Minecraft.getInstance();
		if (levelAccessor instanceof ClientLevel level && minecraft.gameRenderer.getMainCamera() != null) {
			ItemRenderer renderer = minecraft.getItemRenderer();
			BakedModel bakedModel = renderer.getModel(itemStack, level, null, 0);
			PoseStack poseStack = guiGraphics.pose();
			poseStack.pushPose();
			poseStack.translate(x, y, -depth);
			poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
			poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
			poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
			poseStack.mulPose(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
			poseStack.scale(scale, scale, scale);
			if (!bakedModel.usesBlockLight())
				Lighting.setupForFlatItems();
			renderer.render(itemStack, ItemDisplayContext.GUI, false, poseStack, guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedModel);
			guiGraphics.flush();
			Lighting.setupFor3DItems();
			poseStack.popPose();
		}
	}

	public static void renderRectangle(float x1, float y1, float x2, float y2, float depth, int color) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		float x3, y3, x4, y4;
		if (x1 <= x2) {
			x3 = x1;
			x4 = x2;
		} else {
			x3 = x2;
			x4 = x1;
		}
		if (y1 <= y2) {
			y3 = y1;
			y4 = y2;
		} else {
			y3 = y2;
			y4 = y1;
		}
		int red = color >> 16 & 255;
		int green = color >> 8 & 255;
		int blue = color & 255;
		int alpha = color >>> 24;
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		VertexConsumer vertexConsumer = guiGraphics.bufferSource().getBuffer(RenderType.gui());
		vertexConsumer.addVertex(matrix4f, x3, y3, -depth).setColor(red, green, blue, alpha);
		vertexConsumer.addVertex(matrix4f, x3, y4, -depth).setColor(red, green, blue, alpha);
		vertexConsumer.addVertex(matrix4f, x4, y4, -depth).setColor(red, green, blue, alpha);
		vertexConsumer.addVertex(matrix4f, x4, y3, -depth).setColor(red, green, blue, alpha);
	}

	public static void renderShape(VertexBuffer vertexBuffer, double x, double y, double depth, float yaw, float pitch, float roll, float xScale, float yScale, float zScale, int color) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		if (vertexBuffer == null)
			return;
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(x, y, -depth);
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(xScale, yScale, zScale);
		Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushMatrix();
		modelViewStack.mul(poseStack.last().pose());
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
		vertexBuffer.bind();
		vertexBuffer.drawWithShader(modelViewStack, RenderSystem.getProjectionMatrix(), vertexBuffer.getFormat().hasUV(0) ? GameRenderer.getPositionTexColorShader() : GameRenderer.getPositionColorShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		modelViewStack.popMatrix();
		poseStack.popPose();
	}

	public static void renderTexts(String texts, float x, float y, float depth, float angle, float scale, int color, int alignment) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		Font font = Minecraft.getInstance().font;
		float offsetX = 0.0F, offsetY = 0.0F;
		switch (alignment) {
			case 0 :
				offsetX = (font.width(texts) - 1) * 0.5F;
				offsetY = (font.lineHeight - 1) * 0.5F;
				break;
			case 1 :
				offsetY = (font.lineHeight - 1) * 0.5F;
				break;
			case 2 :
				offsetX = (font.width(texts) - 1) * -0.5F;
				offsetY = (font.lineHeight - 1) * 0.5F;
				break;
			case 3 :
				offsetX = (font.width(texts) - 1) * 0.5F;
				break;
			case 4 :
				break;
			case 5 :
				offsetX = (font.width(texts) - 1) * -0.5F;
				break;
			case 6 :
				offsetX = (font.width(texts) - 1) * 0.5F;
				offsetY = (font.lineHeight - 1) * -0.5F;
				break;
			case 7 :
				offsetY = (font.lineHeight - 1) * -0.5F;
				break;
			case 8 :
				offsetX = (font.width(texts) - 1) * -0.5F;
				offsetY = (font.lineHeight - 1) * -0.5F;
				break;
		}
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(x + offsetX * scale, y + offsetY * scale, -depth);
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(angle));
		poseStack.scale(scale, scale, 1.0F);
		poseStack.translate((font.width(texts) - 1) * -0.5F, (font.lineHeight - 1) * -0.5F, 0.0F);
		Matrix4f matrix4f = poseStack.last().pose();
		font.drawInBatch(texts, 0.0F, 0.0F, color, false, matrix4f, guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
		poseStack.popPose();
	}

	public static void renderTexture(float x, float y, float depth, float angle, float scale, int color, int alignment) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		float offsetX = 0.0F, offsetY = 0.0F;
		switch (alignment) {
			case 0 :
				offsetX = 0.5F;
				offsetY = 0.5F;
				break;
			case 1 :
				offsetY = 0.5F;
				break;
			case 2 :
				offsetX = -0.5F;
				offsetY = 0.5F;
				break;
			case 3 :
				offsetX = 0.5F;
				break;
			case 4 :
				break;
			case 5 :
				offsetX = -0.5F;
				break;
			case 6 :
				offsetX = 0.5F;
				offsetY = -0.5F;
				break;
			case 7 :
				offsetY = -0.5F;
				break;
			case 8 :
				offsetX = -0.5F;
				offsetY = -0.5F;
				break;
		}
		RenderSystem.bindTexture(RenderSystem.getShaderTexture(0));
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		int red = color >> 16 & 255;
		int green = color >> 8 & 255;
		int blue = color & 255;
		int alpha = color >>> 24;
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(x, y, -depth);
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(angle));
		poseStack.scale(width * scale, height * scale, 1.0F);
		poseStack.translate(offsetX, offsetY, 0.0F);
		Matrix4f matrix4f = poseStack.last().pose();
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferBuilder.addVertex(matrix4f, -0.5F, -0.5F, 0.0F).setUv(0.0F, 0.0F).setColor(red, green, blue, alpha);
		bufferBuilder.addVertex(matrix4f, -0.5F, 0.5F, 0.0F).setUv(0.0F, 1.0F).setColor(red, green, blue, alpha);
		bufferBuilder.addVertex(matrix4f, 0.5F, 0.5F, 0.0F).setUv(1.0F, 1.0F).setColor(red, green, blue, alpha);
		bufferBuilder.addVertex(matrix4f, 0.5F, -0.5F, 0.0F).setUv(1.0F, 0.0F).setColor(red, green, blue, alpha);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		poseStack.popPose();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderGUI(RenderGuiEvent.Pre event) {
		currentStage = 2;
		guiGraphics = event.getGuiGraphics();
		partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		renderOverlays(event);
		currentStage = 0;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderScreen(ScreenEvent.Render.Post event) {
		currentStage = 3;
		guiGraphics = event.getGuiGraphics();
		partialTick = event.getPartialTick();
		renderOverlays(event);
		currentStage = 0;
	}

	private static void renderOverlays(Event event) {
		Minecraft minecraft = Minecraft.getInstance();
		double scale = minecraft.getWindow().getGuiScale();
		if (scale > 0.0D) {
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			RenderSystem.disableCull();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			execute(event, minecraft.mouseHandler.xpos() / scale, minecraft.mouseHandler.ypos() / scale);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.enableCull();
			RenderSystem.enableDepthTest();
			RenderSystem.depthMask(true);
		}
	}

	public static void execute(double mouseX, double mouseY) {
		execute(null, mouseX, mouseY);
	}

	private static void execute(@Nullable Event event, double mouseX, double mouseY) {
		String texture = "";
		boolean render = false;
		double anchorX = 0;
		double anchorY = 0;
		double repeat = 0;
		double iconX = 0;
		double iconY = 0;
		double scale = 0;
		double mousedistance = 0;
		double angle = 0;
		double mouseclick = 0;
		double yrepeat = 0;
		double order = 0;
		double offset = 0;
		double pOrbX = 0;
		double pOrbY = 0;
		double a1 = 0;
		double a2 = 0;
		double a3 = 0;
		double adjust = 0;
		double var = 0;
		double meter = 0;
		if (Minecraft.getInstance().player != null) {
			Entity entity = Minecraft.getInstance().player;
			double x = entity.getX();
			double y = entity.getY();
			double z = entity.getZ();
			LevelAccessor world = entity.level();
			ResourceKey<Level> dimension = entity.level().dimension();
			if (entity instanceof Player _plr0 && _plr0.containerMenu instanceof MainGuiMenu) {
				anchorX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
				anchorY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
				repeat = 0;
				yrepeat = 0;
				order = 0;
				pOrbX = anchorX - 90;
				pOrbY = anchorY - 15;
				a1 = 50 * Math.sin(entity.tickCount / (5 * Math.round(Math.pow(10, 2) * 3.14159) / Math.pow(10, 2))) + 150;
				a2 = (-50) * Math.sin(entity.tickCount / (10 * Math.round(Math.pow(10, 2) * 3.14159) / Math.pow(10, 2))) + 150;
				a3 = 50 * Math.sin(entity.tickCount / (15 * Math.round(Math.pow(10, 2) * 3.14159) / Math.pow(10, 2))) + 150;
				render = false;
				if (target(3)) {
					for (int index0 = 0; index0 < 4; index0++) {
						if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
							mouseclick = 0.2;
						} else {
							mouseclick = 0;
						}
						iconX = anchorX - 60 * repeat;
						iconY = anchorY + 76;
						if (Math.abs(iconX - mouseX) > Math.abs(iconY - mouseY)) {
							mousedistance = Math.abs(iconX - mouseX);
						} else {
							mousedistance = Math.abs(iconY - mouseY);
						}
						if (20 > mousedistance) {
							if (15 <= mousedistance) {
								scale = (1.2 + 3.75 / mousedistance) - mouseclick;
								angle = (3.75 / mousedistance) * 45.1 * (double) Math.sin(entity.tickCount / 10.1);
								order = -2;
							} else {
								scale = (1.2 + 0.25) - mouseclick;
								angle = 0.5 * 45.1 * (double) Math.sin(entity.tickCount / 20.1);
								order = -2;
							}
						} else {
							order = 0;
							scale = 1.2;
						}
						if (repeat == 0) {
							texture = "mind";
						} else if (repeat == 1) {
							texture = "agility";
						} else if (repeat == 2) {
							texture = "fortitude";
						} else {
							texture = "strength";
						}
						RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + ("screens/statbase".replace("stat", texture)) + ".png")));
						renderTexture((float) iconX, (float) iconY, (float) order, 0, (float) scale, 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
						RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + ("screens/staticon".replace("stat", texture)) + ".png")));
						renderTexture((float) iconX, (float) iconY, (float) (order - 1), (float) angle, (float) (scale * 1.3), 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
						RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + "screens/numberdisplay" + ".png")));
						renderTexture((float) iconX, (float) (iconY + 32 * scale), (float) (order - 2), 0, (float) (scale * 1.3), 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
						renderTexts(("" + Math.round(ReturnStatValueProcedure.execute(entity, texture))), (float) iconX, (float) (iconY + 32 * scale), (float) (order - 3), 0, (float) (scale * 1.3 * 0.8), 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
						repeat = repeat + 1;
						scale = 0;
						angle = 0;
						order = 0;
					}
					repeat = 0;
					for (int index1 = 0; index1 < 8; index1++) {
						if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
							mouseclick = 0.2;
						} else {
							mouseclick = 0;
						}
						iconX = anchorX - 60 * offset;
						iconY = anchorY - (102.5 + 120 * yrepeat);
						if (Math.abs(iconX - mouseX) > Math.abs(iconY - mouseY)) {
							mousedistance = Math.abs(iconX - mouseX);
						} else {
							mousedistance = Math.abs(iconY - mouseY);
						}
						if (21 > mousedistance) {
							if (15 <= mousedistance) {
								scale = (1.2 + 3.75 / mousedistance) - mouseclick;
								angle = (3.75 / mousedistance) * 45.1 * (double) Math.sin(entity.tickCount / 10.1);
								order = -2;
							} else {
								scale = (1.2 + 0.25) - mouseclick;
								angle = 0.5 * 45.1 * (double) Math.sin(entity.tickCount / 20.1);
								order = -2;
							}
						} else {
							order = 0;
							scale = 1.2;
						}
						if (repeat == 0) {
							texture = "thundercall";
						} else if (repeat == 1) {
							texture = "frostdraw";
						} else if (repeat == 2) {
							texture = "galebreathe";
						} else if (repeat == 3) {
							texture = "flamecharm";
						}
						if (repeat == 4) {
							texture = "lifeweave";
						} else if (repeat == 5) {
							texture = "bloodrend";
						} else if (repeat == 6) {
							texture = "ironsing";
						} else if (repeat == 7) {
							texture = "shadowcast";
						}
						if (ReturnStatValueProcedure.execute(entity, texture) >= 1) {
							render = true;
						} else {
							render = false;
						}
						if (render == true) {
							RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + ("screens/statbase".replace("stat", texture)) + ".png")));
							renderTexture((float) iconX, (float) iconY, (float) order, 0, (float) scale, 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
							RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + ("screens/staticon".replace("stat", texture)) + ".png")));
							renderTexture((float) iconX, (float) iconY, (float) (order - 1), (float) angle, (float) (scale * 1.3), 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
							RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + "screens/numberdisplay" + ".png")));
							renderTexture((float) iconX, (float) (iconY + 32 * scale), (float) (order - 2), 0, (float) (scale * 1.3), 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
							renderTexts(("" + Math.round(ReturnStatValueProcedure.execute(entity, texture))), (float) iconX, (float) (iconY + 32 * scale), (float) (order - 3), 0, (float) (scale * 1.3 * 0.8), 255 << 24 | 255 << 16 | 255 << 8 | 255,
									4);
						}
						if (render == true) {
							if (offset < 3) {
								offset = offset + 1;
							} else if (offset == 3) {
								offset = 0;
								yrepeat = 1;
							}
						}
						repeat = repeat + 1;
						scale = 0;
						angle = 0;
						render = false;
					}
					renderEntity(world, entity, (anchorX + 75), (anchorY + 20), (-50), (float) ((((anchorX + 75) - mouseX) / (anchorX + 75)) * 45), (float) ((((anchorY + 20) - mouseY) / (anchorY + 20)) * 25), 0, 75, true);
					RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + "screens/powerorb" + ".png")));
					renderTexture((float) pOrbX, (float) pOrbY, 0, 0, (float) 1.5, 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
					renderTexts(("" + Math.round(entity.getData(VerseModVariables.PLAYER_VARIABLES).power)), (float) pOrbX, (float) pOrbY, -1, 0, (float) 1.5, 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
					if (Hasatleast1attunementProcedure.execute(entity) == true) {
						RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + "screens/attuneexp" + ".png")));
						renderTexture((float) pOrbX, (float) (pOrbY + 35), -2, 0, (float) 1.5, 255 << 24 | (int) a1 << 16 | (int) a2 << 8 | (int) a3, 4);
						renderTexts(("" + Math.round(entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp / (100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power)))), (float) pOrbX, (float) (pOrbY + 35), -3, 0, 1,
								255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
					}
					repeat = 1;
					for (int index2 = 0; index2 < 4; index2++) {
						if (repeat == 1) {
							texture = "str";
							var = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp;
						} else if (repeat == 2) {
							adjust = -10;
							texture = "fort";
							var = entity.getData(VerseModVariables.PLAYER_VARIABLES).fortExp;
						} else if (repeat == 3) {
							texture = "agl";
							adjust = 10;
							var = entity.getData(VerseModVariables.PLAYER_VARIABLES).aglExp;
						} else if (repeat == 4) {
							texture = "int";
							var = entity.getData(VerseModVariables.PLAYER_VARIABLES).intExp;
						}
						RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + ("screens/Yexp".replace("Y", texture)) + ".png")));
						renderTexture((float) (pOrbX - 100 + 40 * repeat + adjust), (float) (pOrbY + 35), -2, 0, (float) 1.5, 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
						renderTexts(("" + Math.round(var / (100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power)))), (float) (pOrbX - 100 + 40 * repeat + adjust), (float) (pOrbY + 35), -3, 0, 1,
								255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
						repeat = repeat + 1;
						adjust = 0;
					}
					RenderSystem.setShaderTexture(0, ResourceLocation.parse(("verse" + ":textures/" + "screens/bracket" + ".png")));
					meter = (entity.getData(VerseModVariables.PLAYER_VARIABLES).expgain / ReturnRequiredExpProcedure.execute(entity)) * 240;
					renderTexture((float) (pOrbX + 0), (float) (pOrbY + 35), -1, 0, (float) 1.5, 255 << 24 | 255 << 16 | 255 << 8 | 255, 4);
					renderRectangle((float) (pOrbX - 120), (float) (pOrbY + 33), (float) (pOrbX - 120 + meter), (float) (pOrbY + 35), 0, 255 << 24 | (int) a3 << 16 | (int) a1 << 8 | (int) a2);
					renderRectangle((float) (pOrbX - 120), (float) (pOrbY + 35), (float) (pOrbX - 120 + meter), (float) (pOrbY + 37), 0, 255 << 24 | (int) (a3 - 45) << 16 | (int) (a1 - 45) << 8 | (int) (a2 - 45));
					release();
				}
			}
		}
	}
}