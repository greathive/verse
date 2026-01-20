package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;

public class CobbledTearstoneWallBlock extends WallBlock {
	public CobbledTearstoneWallBlock() {
		super(BlockBehaviour.Properties.of().strength(4f, 15f).forceSolidOn());
	}
}