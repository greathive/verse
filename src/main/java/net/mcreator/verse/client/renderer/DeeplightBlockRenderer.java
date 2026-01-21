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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.mcreator.verse.block.entity.DeeplightBlockEntity;
import net.mcreator.verse.init.VerseModBlocks;

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

        // Calculate scale based on position in stack
        float scale = calculateScale(blockEntity);

        // Apply scale
        poseStack.scale(scale, scale, scale);

        // Move back to corner for model rendering
        poseStack.translate(-0.5, -0.5, -0.5);

        // Determine which model to use based on stack position
        boolean isBottomBlock = !blockEntity.hasBlockBelow();

        // Get the appropriate block state
        BlockState blockState;

        if (isBottomBlock) {
            // Try to use the deeplightspike block state
            try {
                blockState = VerseModBlocks.DEEPLIGHTSPIKE.get().defaultBlockState();
            } catch (Exception e) {
                // If deeplightspike doesn't exist, use regular deeplight
                blockState = blockEntity.getBlockState();
            }
        } else {
            // Use the regular deeplight model
            blockState = blockEntity.getBlockState();
        }

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

        poseStack.popPose();

        // Only render the full chain line if this is the TOP block
        // Render OUTSIDE the rotation transform so the line doesn't rotate
        if (!blockEntity.hasBlockAbove() && blockEntity.hasBlockBelow()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            drawFullChainLine(blockEntity, partialTick, poseStack, bufferSource, combinedLight);
            poseStack.popPose();
        }
    }

    /**
     * Calculate the scale for this block based on its position in the stack.
     * Top 5 blocks get scaled: 1/5, 2/5, 3/5, 4/5, 5/5 (from top to bottom)
     */
    private float calculateScale(DeeplightBlockEntity blockEntity) {
        int stackHeight = blockEntity.getStackHeight();
        int indexFromTop = blockEntity.getStackIndex();

        // Determine how many blocks should be scaled (max 5, or total height if less)
        int scaledBlockCount = Math.min(5, stackHeight);

        // Check if this block is within the top scaled blocks
        if (indexFromTop < scaledBlockCount) {
            // Scale increases from top to bottom: 1/5, 2/5, 3/5, 4/5, 5/5
            return (indexFromTop + 1) / 5.0f;
        }

        // Blocks below the top 5 (or top N if less than 5) are full scale
        return 1.0f;
    }

    /**
     * Draw a single continuous line from the top of the stack to the bottom
     */
    private void drawFullChainLine(DeeplightBlockEntity blockEntity, float partialTick,
                                   PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight) {

        // Count how many blocks are below this one
        int blocksBelow = 0;
        DeeplightBlockEntity checkBlock = blockEntity;

        while (checkBlock.hasBlockBelow()) {
            BlockPos belowPos = checkBlock.getBlockPos().below();
            if (checkBlock.getLevel().getBlockEntity(belowPos) instanceof DeeplightBlockEntity belowEntity) {
                checkBlock = belowEntity;
                blocksBelow++;
            } else {
                break;
            }
        }

        // Start point: top of the current (topmost) block
        float startY = 0.5f; // Top face of this block

        // End point: center of the bottom block
        // Each block is 1.0 unit tall, so we go down blocksBelow blocks, then 0.5 more to reach center
        float endY = -(blocksBelow + 0.5f);

        // Get camera position for billboarding
        Minecraft mc = Minecraft.getInstance();
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 blockPos = new Vec3(
                blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5,
                blockEntity.getBlockPos().getZ() + 0.5
        );

        // Calculate direction to camera (for billboarding)
        Vec3 toCamera = cameraPos.subtract(blockPos).normalize();

        // Calculate perpendicular vectors for billboarding
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = toCamera.cross(up).normalize();
        Vec3 actualUp = right.cross(toCamera).normalize();

        // Draw a glowing line/beam
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lightning());

        // Get the pose matrices
        PoseStack.Pose pose = poseStack.last();

        // Draw multiple lines for thickness and glow effect
        for (int i = 0; i < 13; i++) {
            float offset = (i - 6f) * 0.015f; // Reduced from 8 iterations and 0.03f spacing

            // Calculate billboard offsets using the right vector
            float offsetX = (float)(right.x * offset);
            float offsetY = (float)(right.y * offset);
            float offsetZ = (float)(right.z * offset);

            // Line 1 - using right vector for billboarding
            buffer.addVertex(pose, offsetX, startY + offsetY, offsetZ)
                    .setColor(11, 20, 116, 255) // #0b1474 with higher opacity
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(120)
                    .setNormal(pose, 0, 1, 0);

            buffer.addVertex(pose, offsetX, endY + offsetY, offsetZ)
                    .setColor(11, 20, 116, 255) // #0b1474 with higher opacity
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(120)
                    .setNormal(pose, 0, 1, 0);

            // Calculate perpendicular offset using up vector
            float offsetX2 = (float)(actualUp.x * offset);
            float offsetY2 = (float)(actualUp.y * offset);
            float offsetZ2 = (float)(actualUp.z * offset);

            // Line 2 - using up vector for billboarding (perpendicular to line 1)
            buffer.addVertex(pose, offsetX2, startY + offsetY2, offsetZ2)
                    .setColor(11, 20, 116, 180) // #0b1474 with higher opacity
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(pose, 0, 1, 0);

            buffer.addVertex(pose, offsetX2, endY + offsetY2, offsetZ2)
                    .setColor(11, 20, 116, 255) // #0b1474 with higher opacity
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(120)
                    .setNormal(pose, 0, 1, 0);
        }
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

        // Start at the bottom face of the current block (y = 0 in block space)
        // From block center (0.5, 0.5, 0.5), bottom face is at y = 0, so offset is -0.5
        float startY = -0.5f;

        // Get camera position for billboarding
        Minecraft mc = Minecraft.getInstance();
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 blockPos = new Vec3(
                blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5,
                blockEntity.getBlockPos().getZ() + 0.5
        );

        // Calculate direction to camera (for billboarding)
        Vec3 toCamera = cameraPos.subtract(blockPos).normalize();

        // Draw a glowing line/beam between the blocks
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lightning());

        // Calculate end position
        // If this is the last block (bottom block has no block below it), end at its center
        // Otherwise, end at the top face of the block below
        double endX, endY, endZ;

        boolean belowIsBottom = !belowEntity.hasBlockBelow();

        if (belowIsBottom) {
            // End at the center of the bottom block
            // We need to account for the below block's rotation
            double radX = Math.toRadians(belowSwingX);
            double radZ = Math.toRadians(belowSwingZ);

            // Center of the below block is at y = -1.0 from current block center
            endY = -1.0;
            endX = 0;
            endZ = 0;
        } else {
            // End at the top face of the below block (y = 1.0 in that block's space)
            // From current block center, that's y = -0.5 (one block down, then to top face)
            // The rotation of the below block shifts where its top face center is
            double radX = Math.toRadians(belowSwingX);
            double radZ = Math.toRadians(belowSwingZ);

            // Top face of below block, accounting for its rotation
            endY = -0.5;
            endX = 0;
            endZ = 0;
        }

        // Get the pose matrices
        PoseStack.Pose pose = poseStack.last();

        // Calculate perpendicular vectors for billboarding
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = toCamera.cross(up).normalize();
        Vec3 actualUp = right.cross(toCamera).normalize();

        // Draw multiple lines for thickness and glow effect (increased from 4 to 8 iterations)
        for (int i = 0; i < 8; i++) {
            float offset = (i - 3.5f) * 0.03f; // Increased spacing from 0.02f to 0.03f

            // Calculate billboard offsets using the right vector
            float offsetX = (float)(right.x * offset);
            float offsetY = (float)(right.y * offset);
            float offsetZ = (float)(right.z * offset);

            // Line 1 - using right vector for billboarding
            buffer.addVertex(pose, offsetX, startY + offsetY, offsetZ)
                    .setColor(135, 206, 235, 128) // Light blue with transparency
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240) // Full brightness
                    .setNormal(pose, 0, 1, 0);

            buffer.addVertex(pose, (float)endX + offsetX, (float)endY + offsetY, (float)endZ + offsetZ)
                    .setColor(135, 206, 235, 128)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(pose, 0, 1, 0);

            // Calculate perpendicular offset using up vector
            float offsetX2 = (float)(actualUp.x * offset);
            float offsetY2 = (float)(actualUp.y * offset);
            float offsetZ2 = (float)(actualUp.z * offset);

            // Line 2 - using up vector for billboarding (perpendicular to line 1)
            buffer.addVertex(pose, offsetX2, startY + offsetY2, offsetZ2)
                    .setColor(135, 206, 235, 128)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(240)
                    .setNormal(pose, 0, 1, 0);

            buffer.addVertex(pose, (float)endX + offsetX2, (float)endY + offsetY2, (float)endZ + offsetZ2)
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