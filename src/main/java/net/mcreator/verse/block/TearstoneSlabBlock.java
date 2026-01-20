package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SlabBlock;

public class TearstoneSlabBlock extends SlabBlock {
	public TearstoneSlabBlock() {
		super(BlockBehaviour.Properties.of().strength(7f, 10f));
	}
}