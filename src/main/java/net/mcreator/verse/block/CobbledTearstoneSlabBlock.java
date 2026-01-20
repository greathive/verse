package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SlabBlock;

public class CobbledTearstoneSlabBlock extends SlabBlock {
	public CobbledTearstoneSlabBlock() {
		super(BlockBehaviour.Properties.of().strength(4f, 15f));
	}
}