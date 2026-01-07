package net.mcreator.verse.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Camera;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class WeaponTrailRenderer {

	private static final Map<ResourceLocation, TrailInfo> trailCache = new HashMap<>();

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;

		if (player == null) {
			return;
		}

		ItemStack mainHand = player.getMainHandItem();

		if (mainHand.isEmpty()) {
			return;
		}


		CompoundTag data = player.getPersistentData();

		// Check if we're in an attack
		if (!data.contains("CustomAttackCooldownUntil") || !data.contains("AttackStartTime")) {
			return;
		}


		long cooldownUntil = data.getLong("CustomAttackCooldownUntil");
		long attackStartTime = data.getLong("AttackStartTime");
		long currentTime = player.level().getGameTime();

		// Calculate swing progress
		double attackSpeed = player.getAttributeValue(Attributes.ATTACK_SPEED);
		long totalSwingTicks = Math.round(20.0 / attackSpeed);
		long ticksSinceStart = currentTime - attackStartTime;

		// Add partial tick for smooth animation
		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		float progress = (ticksSinceStart + partialTick) / totalSwingTicks;

		// Get the item's registry name and extract just the path (item name)
		ResourceLocation itemId = mainHand.getItem().builtInRegistryHolder().key().location();
		String itemName = itemId.getPath(); // Gets "iron_sword" from "minecraft:iron_sword"

		// Create trail ID using the item name
		ResourceLocation trailId = ResourceLocation.fromNamespaceAndPath("verse", itemName);

		// Get trail info
		TrailInfo trailInfo = getTrailInfo(trailId);
		if (trailInfo == null) {
			return;
		}



		// Only render during the specified percent range
		if (progress < trailInfo.startPercent || progress > trailInfo.endPercent) {
			// Clear spawn data when trail is not rendering
			if (data.contains("TrailSpawnX")) {
				data.remove("TrailSpawnX");
				data.remove("TrailSpawnY");
				data.remove("TrailSpawnZ");
				data.remove("TrailSpawnYaw");
				data.remove("TrailSpawnPitch");
				data.remove("TrailAttackStartTime");
				data.remove("TrailVelocityX");
				data.remove("TrailVelocityY");
				data.remove("TrailVelocityZ");
			}
			return;
		}

		// Calculate which frame to show based on progress
		// Map startPercent-endPercent to 0.0-1.0
		float animProgress = (progress - trailInfo.startPercent) / (trailInfo.endPercent - trailInfo.startPercent);
		int currentFrame = (int) (animProgress * trailInfo.frameCount);
		currentFrame = Math.min(currentFrame, trailInfo.frameCount - 1);

		// Get swing counter to determine which swing we're on
		int swingCounter = data.getInt("SwingCounter");

		// Check if we need to store the spawn position (only at the start of THIS attack)
		if (!data.contains("TrailSpawnX") || data.getLong("TrailAttackStartTime") != attackStartTime) {
			// Store the spawn position when the attack starts
			Vec3 playerPos = player.getPosition(partialTick).add(0, player.getEyeHeight(), 0);

			// Get player's pitch and apply followPitch percentage
			float playerPitch = player.getXRot();
			float adjustedPitch = playerPitch * trailInfo.followPitch;



			// Use distance from JSON
			double distance = trailInfo.distance;
			double yaw = Math.toRadians(-player.getYRot());
			double pitch = Math.toRadians(adjustedPitch); // Don't negate here

			// Calculate position in front of player
			double offsetX = Math.sin(yaw) * Math.cos(pitch) * distance;
			double offsetZ = Math.cos(yaw) * Math.cos(pitch) * distance;

			// Vertical offset: negative because positive pitch is looking down in Minecraft
			double offsetY = -Math.sin(pitch) * distance + trailInfo.yOffset;
			

			Vec3 spawnPos = playerPos.add(offsetX, offsetY, offsetZ);

			// Store player's horizontal velocity at spawn time (full velocity, no Y component)
			Vec3 playerVelocity = player.getDeltaMovement();
			double velX = playerVelocity.x * 0.5;
			double velY = 0; // No vertical movement
			double velZ = playerVelocity.z * 0.5;

			// Store the spawn position and rotation (using adjusted pitch)
			data.putDouble("TrailSpawnX", spawnPos.x);
			data.putDouble("TrailSpawnY", spawnPos.y);
			data.putDouble("TrailSpawnZ", spawnPos.z);
			data.putFloat("TrailSpawnYaw", player.getYRot());
			data.putFloat("TrailSpawnPitch", adjustedPitch);
			data.putLong("TrailAttackStartTime", attackStartTime);

			// Store velocity
			data.putDouble("TrailVelocityX", velX);
			data.putDouble("TrailVelocityY", velY);
			data.putDouble("TrailVelocityZ", velZ);
		}

		// Get the stored spawn position
		Vec3 basePos = new Vec3(
				data.getDouble("TrailSpawnX"),
				data.getDouble("TrailSpawnY"),
				data.getDouble("TrailSpawnZ")
		);

		// Get stored velocity
		Vec3 velocity = new Vec3(
				data.getDouble("TrailVelocityX"),
				data.getDouble("TrailVelocityY"),
				data.getDouble("TrailVelocityZ")
		);

		// Calculate time since spawn and apply velocity
		float timeSinceSpawn = ticksSinceStart + partialTick;
		Vec3 trailPos = basePos.add(
				velocity.x * timeSinceSpawn,
				velocity.y * timeSinceSpawn,
				velocity.z * timeSinceSpawn
		);

		float spawnYaw = data.getFloat("TrailSpawnYaw");
		float spawnPitch = data.getFloat("TrailSpawnPitch");

		// Render the trail at the calculated position
		renderTrail(event.getPoseStack(), event.getCamera(), trailInfo, currentFrame, trailPos, spawnYaw, spawnPitch, swingCounter);
	}

	private static void renderTrail(PoseStack poseStack, Camera camera, TrailInfo trailInfo, int frame, Vec3 trailPos, float yaw, float pitch, int swingCounter) {
		Minecraft mc = Minecraft.getInstance();

		poseStack.pushPose();

		// Get camera position for proper world positioning
		Vec3 cameraPos = camera.getPosition();

		// Translate relative to camera
		poseStack.translate(
				trailPos.x - cameraPos.x,
				trailPos.y - cameraPos.y,
				trailPos.z - cameraPos.z
		);

		// Rotate yaw and INVERTED pitch + 90 degrees (using stored rotation)
		poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-yaw + 180));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-pitch + 90)); // Inverted pitch

		// Apply roll rotation from JSON based on swing counter (Z-axis for spinning on own axis)
		float rollAngle = trailInfo.getSwingRoll(swingCounter);
		poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rollAngle));

		// Set up rendering with depth testing enabled
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, trailInfo.textureLocation);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(770, 771); // GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA
		RenderSystem.disableCull();
		RenderSystem.enableDepthTest(); // Enable depth testing
		RenderSystem.depthFunc(515); // GL_LEQUAL - render if less than or equal to depth
		RenderSystem.depthMask(true); // Write to depth buffer
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		// Calculate UV coordinates for current frame (HORIZONTAL layout)
		// Frames are side-by-side horizontally in the sprite sheet
		float frameWidthUV = 1.0f / trailInfo.frameCount;
		float minU = frame * frameWidthUV;
		float maxU = (frame + 1) * frameWidthUV;
		float minV = 0.0f;
		float maxV = 1.0f;

		// Apply scale from JSON (horizontal = width, vertical = height)
		float width = 2.5f * trailInfo.horizontalScale;
		float height = 2.5f * trailInfo.verticalScale;
		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;

		Matrix4f matrix = poseStack.last().pose();
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

		// Draw quad with correct UV mapping for horizontal frames and custom scaling
		buffer.addVertex(matrix, -halfWidth, -halfHeight, 0).setUv(minU, maxV);
		buffer.addVertex(matrix, halfWidth, -halfHeight, 0).setUv(maxU, maxV);
		buffer.addVertex(matrix, halfWidth, halfHeight, 0).setUv(maxU, minV);
		buffer.addVertex(matrix, -halfWidth, halfHeight, 0).setUv(minU, minV);

		BufferUploader.drawWithShader(buffer.buildOrThrow());

		// Restore rendering state
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.depthMask(true);
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();

		poseStack.popPose();
	}

	private static TrailInfo getTrailInfo(ResourceLocation trailId) {
		// Return cached if available
		if (trailCache.containsKey(trailId)) {
			return trailCache.get(trailId);
		}

		try {
			// Load JSON file - using item name as filename
			ResourceLocation jsonLocation = ResourceLocation.fromNamespaceAndPath(
					trailId.getNamespace(),
					"trails/" + trailId.getPath() + ".json"
			);

			var resource = Minecraft.getInstance().getResourceManager()
					.getResource(jsonLocation);

			if (resource.isEmpty()) {
				return null;
			}

			// Parse JSON
			JsonObject json = JsonParser.parseReader(
					new InputStreamReader(resource.get().open())
			).getAsJsonObject();

			int frameWidth = json.get("frameWidth").getAsInt();
			int frameHeight = json.get("frameHeight").getAsInt();
			int frameCount = json.get("frameCount").getAsInt();
			String path = json.get("path").getAsString();

			// Parse swing roll values
			Map<Integer, Float> swingRolls = new HashMap<>();
			if (json.has("swingroll")) {
				JsonObject swingRollObj = json.getAsJsonObject("swingroll");
				for (String key : swingRollObj.keySet()) {
					int swingNum = Integer.parseInt(key);
					float rollValue = swingRollObj.get(key).getAsFloat();
					swingRolls.put(swingNum, rollValue);
				}
			}

			// Parse optional fields with defaults
			float horizontalScale = json.has("horizontalScale") ? json.get("horizontalScale").getAsFloat() : 1.0f;
			float verticalScale = json.has("verticalScale") ? json.get("verticalScale").getAsFloat() : 1.0f;
			float yOffset = json.has("yOffset") ? json.get("yOffset").getAsFloat() : 0.0f;
			float distance = json.has("distance") ? json.get("distance").getAsFloat() : 2.0f;
			// Check both "followPitch" (camelCase) and "followpitch" (lowercase)
			float followPitch = 1.0f;
			if (json.has("followPitch")) {
				followPitch = json.get("followPitch").getAsFloat();
			} else if (json.has("followpitch")) {
				followPitch = json.get("followpitch").getAsFloat();
			}

			// Parse percent values with defaults (0.25 to 0.76)
			float startPercent = 0.25f;
			float endPercent = 0.76f;
			if (json.has("percent")) {
				JsonObject percentObj = json.getAsJsonObject("percent");
				startPercent = percentObj.has("start") ? percentObj.get("start").getAsFloat() : 0.25f;
				endPercent = percentObj.has("end") ? percentObj.get("end").getAsFloat() : 0.76f;
			}

			// Convert path to texture location (add .png extension)
			ResourceLocation textureLocation = ResourceLocation.parse(path + ".png");

			TrailInfo info = new TrailInfo(frameWidth, frameHeight, frameCount, textureLocation,
					swingRolls, horizontalScale, verticalScale, yOffset, distance, startPercent, endPercent, followPitch);

			// Cache it
			trailCache.put(trailId, info);



			return info;

		} catch (Exception e) {
			System.err.println("Error loading trail info for " + trailId + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static class TrailInfo {
		public final int frameWidth;
		public final int frameHeight;
		public final int frameCount;
		public final ResourceLocation textureLocation;
		public final Map<Integer, Float> swingRolls;
		public final float horizontalScale;
		public final float verticalScale;
		public final float yOffset;
		public final float distance;
		public final float startPercent;
		public final float endPercent;
		public final float followPitch;

		public TrailInfo(int frameWidth, int frameHeight, int frameCount, ResourceLocation textureLocation,
						 Map<Integer, Float> swingRolls, float horizontalScale, float verticalScale,
						 float yOffset, float distance, float startPercent, float endPercent, float followPitch) {
			this.frameWidth = frameWidth;
			this.frameHeight = frameHeight;
			this.frameCount = frameCount;
			this.textureLocation = textureLocation;
			this.swingRolls = swingRolls;
			this.horizontalScale = horizontalScale;
			this.verticalScale = verticalScale;
			this.yOffset = yOffset;
			this.distance = distance;
			this.startPercent = startPercent;
			this.endPercent = endPercent;
			this.followPitch = followPitch;
		}

		public float getSwingRoll(int swingCounter) {
			return swingRolls.getOrDefault(swingCounter, 0.0f);
		}
	}
}