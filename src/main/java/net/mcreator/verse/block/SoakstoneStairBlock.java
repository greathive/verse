package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class SoakstoneStairBlock extends StairBlock {
	public SoakstoneStairBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.BASALT).strength(4f, 10f));
	}

	@Override
	public float getExplosionResistance() {
		return 10f;
	}
}