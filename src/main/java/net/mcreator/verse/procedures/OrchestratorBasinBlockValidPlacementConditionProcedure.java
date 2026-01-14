package net.mcreator.verse.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class OrchestratorBasinBlockValidPlacementConditionProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x + 0, y, z + 1))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x + 1, y, z + 0))).getBlock() == Blocks.AIR
				&& (world.getBlockState(BlockPos.containing(x + 1, y, z + 1))).getBlock() == Blocks.AIR) {
			return true;
		}
		return false;
	}
}