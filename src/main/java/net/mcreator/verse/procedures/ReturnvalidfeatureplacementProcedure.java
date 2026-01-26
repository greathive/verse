package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class ReturnvalidfeatureplacementProcedure {
	public static boolean execute(LevelAccessor world) {
		double checkX = 0;
		double checkY = 0;
		double checkZ = 0;
		double valid = 0;
		double maxvalid = 0;
		checkY = -1;
		for (int index0 = 0; index0 < 3; index0++) {
			checkZ = -1;
			for (int index1 = 0; index1 < 5; index1++) {
				checkX = -1;
				for (int index2 = 0; index2 < 5; index2++) {
					if (world.hasChunkAt(BlockPos.containing(checkX, checkY, checkZ))) {
						if (!(world.getBlockState(BlockPos.containing(checkX, checkY, checkZ))).is(BlockTags.create(ResourceLocation.parse("verse:featureblock")))) {
							valid = valid + 1;
						}
						checkX = checkX + 1;
						maxvalid = maxvalid + 1;
					}
				}
				checkZ = checkZ + 1;
			}
			checkY = checkY + 1;
		}
		if (valid >= maxvalid - 1) {
			return true;
		}
		return false;
	}
}