package net.mcreator.verse.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.mcreator.verse.block.entity.DeeplightBlockEntity;

public class DeeplightBlockRenderer implements BlockEntityRenderer<DeeplightBlockEntity> {

    public DeeplightBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DeeplightBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        poseStack.pushPose();

        // Move to center of block
        poseStack.translate(0.5, 0.5, 0.5);

        // Get swing angles for this block
        float swingX = blockEntity.getSwingAngleX(partialTick);
        float swingZ = blockEntity.getSwingAngleZ(partialTick);

        // Apply rotation at this point
        poseStack.mulPose(Axis.ZP.rotationDegrees(swingZ));
        poseStack.mulPose(Axis.XP.rotationDegrees(swingX));

        // Move back to corner for model rendering
        poseStack.translate(-0.5, -0.5, -0.5);

        // Render the block model with the rotation applied
        BlockState blockState = blockEntity.getBlockState();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);

        // Use a small depth offset to prevent z-fighting
        poseStack.pushPose();
        poseStack.translate(0, 0.004, 0);

        // Create a custom vertex consumer that applies transparency
        VertexConsumer buffer = new TransparentVertexConsumer(
                bufferSource.getBuffer(RenderType.translucent()),
                0.9f // 90% opacity
        );

        // Render with translucent type
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                buffer,
                blockState,
                model,
                1.0f, 1.0f, 1.0f,
                combinedLight,
                combinedOverlay
        );

        poseStack.popPose();

        // Move back to center for line rendering
        poseStack.translate(0.5, 0.5, 0.5);

        // Draw connection line to block below (if exists)
        if (blockEntity.hasBlockBelow()) {
            drawConnectionLine(blockEntity, partialTick, poseStack, bufferSource, combinedLight);
        }

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

        // Get the pose matrices
        PoseStack.Pose pose = poseStack.last();

        // Draw multiple lines for thickness and glow effect
        for (int i = 0; i < 4; i++) {
            float offset = (i - 1.5f) * 0.02f;

            // Line 1
            buffer.addVertex(pose, offset, (float)startPos.y, 0)
                    .setColor(135, 206, 235, 128) // Light blue with transparency
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240) // Full brightness
                    .setNormal(pose, 0, 1, 0);

            buffer.addVertex(pose, (float)endX + offset, (float)endY, (float)endZ)
                    .setColor(135, 206, 235, 128)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(pose, 0, 1, 0);

            // Line 2 (perpendicular for thickness)
            buffer.addVertex(pose, 0, (float)startPos.y, offset)
                    .setColor(135, 206, 235, 128)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(pose, 0, 1, 0);

            buffer.addVertex(pose, (float)endX, (float)endY, (float)endZ + offset)
                    .setColor(135, 206, 235, 128)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(pose, 0, 1, 0);
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

    // Wrapper class to apply transparency to vertex colors
    private static class TransparentVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float alpha;

        public TransparentVertexConsumer(VertexConsumer delegate, float alpha) {
            this.delegate = delegate;
            this.alpha = alpha;
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            return delegate.addVertex(x, y, z);
        }

        @Override
        public VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
            return delegate.addVertex(pose, x, y, z);
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            return delegate.setColor(red, green, blue, (int)(this.alpha * 255));
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            return delegate.setUv(u, v);
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return delegate.setUv1(u, v);
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return delegate.setUv2(u, v);
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            return delegate.setNormal(x, y, z);
        }

        @Override
        public VertexConsumer setNormal(PoseStack.Pose pose, float x, float y, float z) {
            return delegate.setNormal(pose, x, y, z);
        }

        @Override
        public void addVertex(float x, float y, float z, int color, float u, float v, int overlayUV, int lightmapUV, float normalX, float normalY, float normalZ) {
            // Extract RGB from color, apply our alpha
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int newAlpha = (int)(this.alpha * 255);
            int newColor = (newAlpha << 24) | (r << 16) | (g << 8) | b;

            delegate.addVertex(x, y, z, newColor, u, v, overlayUV, lightmapUV, normalX, normalY, normalZ);
        }
    }
}