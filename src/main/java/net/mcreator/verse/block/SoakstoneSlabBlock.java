package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class SoakstoneSlabBlock extends SlabBlock {
	public SoakstoneSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.BASALT).strength(4f, 10f));
	}
}