package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.Blocks;

public class TearstoneStairBlock extends StairBlock {
	public TearstoneStairBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().strength(7f, 10f));
	}

	@Override
	public float getExplosionResistance() {
		return 10f;
	}
}