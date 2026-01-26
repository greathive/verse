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
    private static final int MAX_RENDER_DISTANCE = 256; // blocks
    private static final ResourceLocation STEM_TEXTURE = ResourceLocation.fromNamespaceAndPath("verse", "textures/block/deeplightstem.png");

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

        // Render with cutout_mipped for proper transparency without blending
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutoutMipped());

        // Render the model
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

        // Only render the full chain line if this is the TOP block and within render distance
        if (!blockEntity.hasBlockAbove() && blockEntity.hasBlockBelow() && isWithinRenderDistance(blockEntity)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            drawFullChainLine(blockEntity, partialTick, poseStack, bufferSource, combinedLight);
            poseStack.popPose();
        }
    }

    /**
     * Check if block is within render distance
     */
    private boolean isWithinRenderDistance(DeeplightBlockEntity blockEntity) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        BlockPos blockPos = blockEntity.getBlockPos();
        Vec3 playerPos = mc.player.getEyePosition();

        double distanceSq = playerPos.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
        return distanceSq <= MAX_RENDER_DISTANCE * MAX_RENDER_DISTANCE;
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
     * Draw a single continuous textured beam from the top of the stack to the bottom
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

        if (blocksBelow == 0) return; // No blocks below, don't render

        // Start point: bottom of the current (topmost) block
        float startY = -0.5f;

        // End point: top of the bottom block
        float endY = -(blocksBelow - 0.5f);

        // Calculate the height of the beam
        float beamHeight = Math.abs(startY - endY);

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

        // Width of the beam
        float beamWidth = 0.125f; // 2 pixels in block units (1/8 of a block)

        // Get the textured buffer
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(STEM_TEXTURE));

        // Get the pose matrices
        PoseStack.Pose pose = poseStack.last();

        // Calculate the four corners of the quad, billboarded to face camera
        float halfWidth = beamWidth / 2f;

        // Right side offset
        float rightX = (float)(right.x * halfWidth);
        float rightY = (float)(right.y * halfWidth);
        float rightZ = (float)(right.z * halfWidth);

        // Left side offset (negative right)
        float leftX = -rightX;
        float leftY = -rightY;
        float leftZ = -rightZ;

        // Draw the quad with correct winding order
        // Bottom-left
        buffer.addVertex(pose, leftX, endY, leftZ)
                .setColor(255, 255, 255, 255)
                .setUv(0, beamHeight)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(240)
                .setNormal(pose, (float)toCamera.x, (float)toCamera.y, (float)toCamera.z);

        // Bottom-right
        buffer.addVertex(pose, rightX, endY, rightZ)
                .setColor(255, 255, 255, 255)
                .setUv(1, beamHeight)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(240)
                .setNormal(pose, (float)toCamera.x, (float)toCamera.y, (float)toCamera.z);

        // Top-right
        buffer.addVertex(pose, rightX, startY, rightZ)
                .setColor(255, 255, 255, 255)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(240)
                .setNormal(pose, (float)toCamera.x, (float)toCamera.y, (float)toCamera.z);

        // Top-left
        buffer.addVertex(pose, leftX, startY, leftZ)
                .setColor(255, 255, 255, 255)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(240)
                .setNormal(pose, (float)toCamera.x, (float)toCamera.y, (float)toCamera.z);
    }

    @Override
    public int getViewDistance() {
        return MAX_RENDER_DISTANCE;
    }

    @Override
    public boolean shouldRenderOffScreen(DeeplightBlockEntity blockEntity) {
        return false;
    }
}