package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.init.VerseModBlocks;

public class DeeplightBlockValidPlacementConditionProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == VerseModBlocks.DEEPLIGHT.get() || world.getBlockFloorHeight(BlockPos.containing(x, y + 1, z)) > 0) {
			return true;
		}
		return false;
	}
}