package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class TearstoneBrickStairBlock extends StairBlock {
	public TearstoneBrickStairBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).strength(10f));
	}

	@Override
	public float getExplosionResistance() {
		return 10f;
	}
}