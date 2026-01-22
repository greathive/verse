package net.mcreator.verse.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = "verse", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ScyphoziaFogLayerRenderer {

	private static final ResourceLocation SCYPHOZIA_DIMENSION = ResourceLocation.fromNamespaceAndPath("verse", "scyphozia");
	private static final ResourceLocation CELTORIAN_CHASMS_BIOME = ResourceLocation.fromNamespaceAndPath("verse", "celtorian_chasms");
	private static final float FOG_TOP_HEIGHT = 20.0f;
	private static final float FOG_BOTTOM_HEIGHT = -16.0f;
	private static final int RENDER_DISTANCE = 512;
	private static final int GRID_SIZE = 16; // Larger grid for better performance

	// Cache for terrain data
	private static final Map<Long, TerrainData> terrainCache = new HashMap<>();
	private static final Map<Long, Float> distanceCache = new HashMap<>(); // Cache for distance calculations
	private static long lastClearTime = 0;
	private static int lastCameraChunkX = Integer.MAX_VALUE;
	private static int lastCameraChunkZ = Integer.MAX_VALUE;

	private static class TerrainData {
		int height;
		boolean isCeltorianChasms;

		TerrainData(int height, boolean isCeltorianChasms) {
			this.height = height;
			this.isCeltorianChasms = isCeltorianChasms;
		}
	}

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;

		if (level == null) {
			return;
		}

		// Check if we're in the Scyphozia dimension
		if (!level.dimension().location().equals(SCYPHOZIA_DIMENSION)) {
			return;
		}

		Camera camera = event.getCamera();
		double cameraX = camera.getPosition().x;
		double cameraY = camera.getPosition().y;
		double cameraZ = camera.getPosition().z;

		// Only render if camera is within reasonable distance of fog level
		if (cameraY > FOG_TOP_HEIGHT + 128 || cameraY < FOG_BOTTOM_HEIGHT - 64) {
			return;
		}

		// Clear cache when moving to a new chunk to prevent stale data
		int currentChunkX = (int)(cameraX / 16);
		int currentChunkZ = (int)(cameraZ / 16);
		if (currentChunkX != lastCameraChunkX || currentChunkZ != lastCameraChunkZ) {
			terrainCache.clear();
			distanceCache.clear();
			lastCameraChunkX = currentChunkX;
			lastCameraChunkZ = currentChunkZ;
		}

		// Clear cache periodically
		long currentTime = level.getGameTime();
		if (currentTime - lastClearTime > 200) {
			terrainCache.clear();
			distanceCache.clear();
			lastClearTime = currentTime;
		}

		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();

		// Set up rendering - solid fog doesn't need blending
		RenderSystem.disableBlend();
		RenderSystem.disableCull();
		RenderSystem.depthMask(true); // Write to depth buffer for solid fog
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		Matrix4f matrix = poseStack.last().pose();

		// Mid dark grey color - solid opaque
		float alpha = 1.0f; // Fully opaque

		float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(false);
		float time = (level.getGameTime() + partialTick) * 0.05f;

		// Render fog in a grid around the camera
		int gridCount = RENDER_DISTANCE / GRID_SIZE;
		boolean hasRenderedAnything = false;

		for (int gridX = -gridCount; gridX <= gridCount; gridX++) {
			for (int gridZ = -gridCount; gridZ <= gridCount; gridZ++) {
				int worldX = (int)(cameraX) + (gridX * GRID_SIZE);
				int worldZ = (int)(cameraZ) + (gridZ * GRID_SIZE);

				// Align to grid
				worldX = (worldX / GRID_SIZE) * GRID_SIZE;
				worldZ = (worldZ / GRID_SIZE) * GRID_SIZE;

				long cacheKey = ((long)worldX << 32) | (worldZ & 0xFFFFFFFFL);
				TerrainData data = terrainCache.get(cacheKey);

				if (data == null) {
					BlockPos checkPos = new BlockPos(worldX, (int)FOG_TOP_HEIGHT, worldZ);

					// Check if in Celtorian Chasms biome
					Holder<Biome> biome = level.getBiome(checkPos);
					boolean isCeltorianChasms = false;
					if (biome.unwrapKey().isPresent()) {
						ResourceLocation biomeKey = biome.unwrapKey().get().location();
						if (biomeKey.equals(CELTORIAN_CHASMS_BIOME)) {
							isCeltorianChasms = true;
						}
					}

					// Find the highest solid block - only check every 16 blocks for speed
					int terrainHeight = -64;
					for (int checkY = (int)FOG_TOP_HEIGHT; checkY >= -64; checkY -= 16) {
						BlockPos pos = new BlockPos(worldX, checkY, worldZ);
						if (!level.getBlockState(pos).isAir()) {
							terrainHeight = checkY;
							break;
						}
					}

					data = new TerrainData(terrainHeight, isCeltorianChasms);
					terrainCache.put(cacheKey, data);
				}

				// Only render fog if terrain is below fog level (or in Celtorian Chasms)
				// Allow fog to render even slightly above threshold to prevent gaps
				if (data.height > FOG_TOP_HEIGHT + 4 && !data.isCeltorianChasms) {
					continue;
				}

				hasRenderedAnything = true;

				// Calculate relative positions for rendering
				float x1 = worldX - (float)cameraX;
				float x2 = worldX + GRID_SIZE - (float)cameraX;
				float z1 = worldZ - (float)cameraZ;
				float z2 = worldZ + GRID_SIZE - (float)cameraZ;

				// Calculate distance to land for each corner to create smooth gradient
				float dist1 = calculateDistanceToLand(level, worldX, worldZ, GRID_SIZE);
				float dist2 = calculateDistanceToLand(level, worldX + GRID_SIZE, worldZ, GRID_SIZE);
				float dist3 = calculateDistanceToLand(level, worldX, worldZ + GRID_SIZE, GRID_SIZE);
				float dist4 = calculateDistanceToLand(level, worldX + GRID_SIZE, worldZ + GRID_SIZE, GRID_SIZE);

				// Convert distances to colors with longer gradient range and smoother curve
				float maxDistance = 128.0f; // Increased from 64 to 128 for longer blend
				// Use smoothstep function for more gradual transition
				float color1 = calculateSmoothColor(dist1, maxDistance);
				float color2 = calculateSmoothColor(dist2, maxDistance);
				float color3 = calculateSmoothColor(dist3, maxDistance);
				float color4 = calculateSmoothColor(dist4, maxDistance);

				// Calculate wave animation for each corner
				float wave1 = (float)(Math.sin(time + worldX * 0.05 + worldZ * 0.05) * 1.5);
				float wave2 = (float)(Math.sin(time + (worldX + GRID_SIZE) * 0.05 + worldZ * 0.05) * 1.5);
				float wave3 = (float)(Math.sin(time + worldX * 0.05 + (worldZ + GRID_SIZE) * 0.05) * 1.5);
				float wave4 = (float)(Math.sin(time + (worldX + GRID_SIZE) * 0.05 + (worldZ + GRID_SIZE) * 0.05) * 1.5);

				// Render top surface
				float yTop = FOG_TOP_HEIGHT - (float)cameraY;

				// Top surface - each vertex has its own color for smooth gradient
				buffer.addVertex(matrix, x1, yTop + wave1, z1).setColor(color1, color1, color1, alpha);
				buffer.addVertex(matrix, x1, yTop + wave3, z2).setColor(color3, color3, color3, alpha);
				buffer.addVertex(matrix, x2, yTop + wave4, z2).setColor(color4, color4, color4, alpha);
				buffer.addVertex(matrix, x2, yTop + wave2, z1).setColor(color2, color2, color2, alpha);

				// Bottom surface - slightly darker
				float yBottom = FOG_BOTTOM_HEIGHT - (float)cameraY;
				float bottomColor1 = color1 * 0.7f;
				float bottomColor2 = color2 * 0.7f;
				float bottomColor3 = color3 * 0.7f;
				float bottomColor4 = color4 * 0.7f;
				buffer.addVertex(matrix, x1, yBottom, z1).setColor(bottomColor1, bottomColor1, bottomColor1, alpha);
				buffer.addVertex(matrix, x2, yBottom, z1).setColor(bottomColor2, bottomColor2, bottomColor2, alpha);
				buffer.addVertex(matrix, x2, yBottom, z2).setColor(bottomColor4, bottomColor4, bottomColor4, alpha);
				buffer.addVertex(matrix, x1, yBottom, z2).setColor(bottomColor3, bottomColor3, bottomColor3, alpha);
			}
		}

		// Only draw if we actually rendered something
		if (hasRenderedAnything) {
			BufferUploader.drawWithShader(buffer.buildOrThrow());
		}

		// Restore rendering state
		RenderSystem.enableCull();

		poseStack.popPose();
	}

	private static float calculateSmoothColor(float distance, float maxDistance) {
		// Normalize distance to 0-1 range
		float t = Math.min(distance / maxDistance, 1.0f);

		// Use smoothstep function for gradual transition: t^2 * (3 - 2*t)
		float smoothT = t * t * (3.0f - 2.0f * t);

		// Map to color range: 15/255 (dark, close) to 40/255 (light, far)
		float darkGrey = 0.0f / 255.0f;   // 0.059 - close to land
		float lightGrey = 10.0f / 255.0f;  // 0.157 - far from land

		// Reversed: start dark, get lighter with distance
		return darkGrey + (smoothT * (lightGrey - darkGrey));
	}

	private static float calculateDistanceToLand(ClientLevel level, int centerX, int centerZ, int gridSize) {
		// Check cache first
		long cacheKey = ((long)centerX << 32) | (centerZ & 0xFFFFFFFFL);
		Float cachedDistance = distanceCache.get(cacheKey);
		if (cachedDistance != null) {
			return cachedDistance;
		}

		// Check in expanding rings to find nearest high terrain
		int maxCheckDistance = 128; // Increased to match gradient range
		int checkStep = gridSize; // Check every grid cell for accuracy near edges

		float minDistance = maxCheckDistance;

		for (int radius = 0; radius <= maxCheckDistance; radius += checkStep) {
			if (radius == 0) {
				// Check center point first
				long terrainKey = ((long)centerX << 32) | (centerZ & 0xFFFFFFFFL);
				TerrainData centerData = terrainCache.get(terrainKey);

				if (centerData != null && centerData.height >= FOG_TOP_HEIGHT && !centerData.isCeltorianChasms) {
					distanceCache.put(cacheKey, 0.0f);
					return 0.0f;
				}
				continue;
			}

			// Check 16 directions at this radius for smoother detection
			int numDirections = 16;
			for (int i = 0; i < numDirections; i++) {
				double angle = (i * 2.0 * Math.PI) / numDirections;
				int checkX = centerX + (int)(Math.cos(angle) * radius);
				int checkZ = centerZ + (int)(Math.sin(angle) * radius);

				// Align to grid
				checkX = (checkX / gridSize) * gridSize;
				checkZ = (checkZ / gridSize) * gridSize;

				long terrainKey = ((long)checkX << 32) | (checkZ & 0xFFFFFFFFL);
				TerrainData neighborData = terrainCache.get(terrainKey);

				if (neighborData == null) {
					// Quick check if not cached
					int height = -64;
					for (int checkY = (int)FOG_TOP_HEIGHT; checkY >= -64; checkY -= 16) {
						if (!level.getBlockState(new BlockPos(checkX, checkY, checkZ)).isAir()) {
							height = checkY;
							break;
						}
					}
					neighborData = new TerrainData(height, false);
				}

				// If we found high terrain, track minimum distance
				if (neighborData.height >= FOG_TOP_HEIGHT && !neighborData.isCeltorianChasms) {
					float actualDistance = (float)Math.sqrt((checkX - centerX) * (checkX - centerX) + (checkZ - centerZ) * (checkZ - centerZ));
					minDistance = Math.min(minDistance, actualDistance);
				}
			}

			// Early exit if we found nearby land
			if (minDistance < radius + checkStep) {
				break;
			}
		}

		// Cache and return result
		distanceCache.put(cacheKey, minDistance);
		return minDistance;
	}
}