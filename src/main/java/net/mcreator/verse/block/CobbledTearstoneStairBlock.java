package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.Blocks;

public class CobbledTearstoneStairBlock extends StairBlock {
	public CobbledTearstoneStairBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().strength(4f, 15f));
	}

	@Override
	public float getExplosionResistance() {
		return 15f;
	}
}