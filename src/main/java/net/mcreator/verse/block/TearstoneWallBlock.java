package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;

public class TearstoneWallBlock extends WallBlock {
	public TearstoneWallBlock() {
		super(BlockBehaviour.Properties.of().strength(7f, 10f).forceSolidOn());
	}
}