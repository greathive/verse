package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class DrownedCoralSlabBlock extends SlabBlock {
	public DrownedCoralSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK).strength(5f, 10f));
	}
}