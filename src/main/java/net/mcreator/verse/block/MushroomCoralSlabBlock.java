package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class MushroomCoralSlabBlock extends SlabBlock {
	public MushroomCoralSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(5f, 10f));
	}
}