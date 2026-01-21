package net.mcreator.verse.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mcreator.verse.init.VerseModBlockEntities;

public class DeeplightBlockEntity extends BlockEntity {
	private float swingAngle = 0f;
	private float swingSpeed = 0.02f;
	private float swingDirection = 1f;
	private long randomOffset;

	public DeeplightBlockEntity(BlockPos pos, BlockState state) {
		super(VerseModBlockEntities.DEEPLIGHT.get(), pos, state);
		// Add random offset so not all blocks swing in sync
		this.randomOffset = (long) (Math.random() * 10000);
	}

	public float getSwingAngle(float partialTick) {
		// Use world time + random offset for unique swing pattern
		if (level != null) {
			long time = level.getGameTime() + randomOffset;
			float timeWithPartial = time + partialTick;

			// Create a sine wave for smooth pendulum motion
			// Swing between -15 and +15 degrees
			return (float) Math.sin(timeWithPartial * 0.05f) * 15f;
		}
		return 0f;
	}

	public float getSwingAngleX(float partialTick) {
		if (level != null) {
			long time = level.getGameTime() + randomOffset;
			float timeWithPartial = time + partialTick;
			// Slightly different frequency for X axis
			return (float) Math.sin(timeWithPartial * 0.04f) * 15f;
		}
		return 0f;
	}

	public float getSwingAngleZ(float partialTick) {
		if (level != null) {
			long time = level.getGameTime() + randomOffset;
			float timeWithPartial = time + partialTick;
			// Different phase for Z axis
			return (float) Math.cos(timeWithPartial * 0.045f) * 15f;
		}
		return 0f;
	}

	/**
	 * Get the index of this block in the vertical stack counting from the TOP (0 = topmost block)
	 */
	public int getStackIndex() {
		if (level == null) return 0;

		// Find the topmost block first
		BlockPos topPos = worldPosition;
		while (level.getBlockEntity(topPos.above()) instanceof DeeplightBlockEntity) {
			topPos = topPos.above();
		}

		// Now count down from the top to this block
		int index = 0;
		BlockPos checkPos = topPos;

		while (!checkPos.equals(worldPosition)) {
			index++;
			checkPos = checkPos.below();

			// Safety check to prevent infinite loop
			if (!(level.getBlockEntity(checkPos) instanceof DeeplightBlockEntity)) {
				return 0;
			}
		}

		return index;
	}

	/**
	 * Get total height of the stack
	 */
	public int getStackHeight() {
		if (level == null) return 1;

		int height = 1;
		BlockPos checkPos = worldPosition.above();

		// Count blocks above
		while (level.getBlockEntity(checkPos) instanceof DeeplightBlockEntity) {
			height++;
			checkPos = checkPos.above();
		}

		checkPos = worldPosition.below();

		// Count blocks below
		while (level.getBlockEntity(checkPos) instanceof DeeplightBlockEntity) {
			height++;
			checkPos = checkPos.below();
		}

		return height;
	}

	/**
	 * Check if there's a deeplight block above this one
	 */
	public boolean hasBlockAbove() {
		if (level == null) return false;
		return level.getBlockEntity(worldPosition.above()) instanceof DeeplightBlockEntity;
	}

	/**
	 * Check if there's a deeplight block below this one
	 */
	public boolean hasBlockBelow() {
		if (level == null) return false;
		return level.getBlockEntity(worldPosition.below()) instanceof DeeplightBlockEntity;
	}
}