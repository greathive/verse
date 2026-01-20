package net.mcreator.verse.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.mcreator.verse.block.entity.DeeplightBlockEntity;
import org.joml.Matrix4f;
import org.joml.Matrix3f;

public class DeeplightBlockRenderer implements BlockEntityRenderer<DeeplightBlockEntity> {

	public DeeplightBlockRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(DeeplightBlockEntity blockEntity, float partialTick, PoseStack poseStack,
					   MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

		// Only render if this block has blocks below it (we render the chain downward)
		if (!blockEntity.hasBlockBelow()) {
			return;
		}

		poseStack.pushPose();

		// Move to center of block
		poseStack.translate(0.5, 0.5, 0.5);

		// Get swing angles for this block
		float swingX = blockEntity.getSwingAngleX(partialTick);
		float swingZ = blockEntity.getSwingAngleZ(partialTick);

		// Apply rotation at this point
		poseStack.mulPose(Axis.ZP.rotationDegrees(swingZ));
		poseStack.mulPose(Axis.XP.rotationDegrees(swingX));

		// Draw connection line to block below
		drawConnectionLine(blockEntity, partialTick, poseStack, bufferSource, combinedLight);

		poseStack.popPose();
	}

	private void drawConnectionLine(DeeplightBlockEntity blockEntity, float partialTick,
									PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight) {

		// Get the block below
		BlockPos belowPos = blockEntity.getBlockPos().below();
		if (!(blockEntity.getLevel().getBlockEntity(belowPos) instanceof DeeplightBlockEntity belowEntity)) {
			return;
		}

		// Get rotation angles for the block below
		float belowSwingX = belowEntity.getSwingAngleX(partialTick);
		float belowSwingZ = belowEntity.getSwingAngleZ(partialTick);

		// Calculate the position of the bottom of the current block (already rotated)
		Vec3 startPos = new Vec3(0, -0.5, 0);

		// Calculate where the top of the below block is (with its rotation)
		// We need to reverse our current rotation, move down, apply below rotation, move to top
		poseStack.pushPose();

		// Move to world position of below block
		poseStack.translate(0, -1, 0);

		// Apply below block's rotation
		poseStack.mulPose(Axis.ZP.rotationDegrees(belowSwingZ));
		poseStack.mulPose(Axis.XP.rotationDegrees(belowSwingX));

		// End position is at top of below block
		Vec3 endPos = new Vec3(0, 0.5, 0);

		poseStack.popPose();

		// Draw a glowing line/beam between the blocks
		VertexConsumer buffer = bufferSource.getBuffer(RenderType.lightning());

		// Calculate actual end position with below block's rotation
		double radX = Math.toRadians(belowSwingX);
		double radZ = Math.toRadians(belowSwingZ);

		// Simple rotation calculation for end point
		double endY = -0.5 + Math.cos(radX) * Math.cos(radZ);
		double endX = Math.sin(radZ);
		double endZ = Math.sin(radX);

		// Draw the connecting beam (make it glow)
		Matrix4f matrix4f = poseStack.last().pose();
		Matrix3f matrix3f = poseStack.last().normal();

		// Draw multiple lines for thickness and glow effect
		for (int i = 0; i < 4; i++) {
			float offset = (i - 1.5f) * 0.02f;

			// Line 1
			buffer.addVertex(matrix4f, offset, (float)startPos.y, 0)
					.setColor(135, 206, 235, 128) // Light blue with transparency
					.setUv(0, 0)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(240) // Full brightness
					.setNormal(matrix3f, 0, 1, 0);

			buffer.addVertex(matrix4f, (float)endX + offset, (float)endY, (float)endZ)
					.setColor(135, 206, 235, 128)
					.setUv(0, 1)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(240)
					.setNormal(matrix3f, 0, 1, 0);

			// Line 2 (perpendicular for thickness)
			buffer.addVertex(matrix4f, 0, (float)startPos.y, offset)
					.setColor(135, 206, 235, 128)
					.setUv(0, 0)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(240)
					.setNormal(matrix3f, 0, 1, 0);

			buffer.addVertex(matrix4f, (float)endX, (float)endY, (float)endZ + offset)
					.setColor(135, 206, 235, 128)
					.setUv(0, 1)
					.setOverlay(OverlayTexture.NO_OVERLAY)
					.setLight(240)
					.setNormal(matrix3f, 0, 1, 0);
		}
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public boolean shouldRenderOffScreen(DeeplightBlockEntity blockEntity) {
		return true;
	}
}