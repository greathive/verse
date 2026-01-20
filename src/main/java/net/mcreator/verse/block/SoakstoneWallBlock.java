package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class SoakstoneWallBlock extends WallBlock {
	public SoakstoneWallBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.BASALT).strength(4f, 10f).forceSolidOn());
	}
}