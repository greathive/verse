package net.mcreator.verse.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

import com.mojang.serialization.MapCodec;

public class DeepsandBlock extends FallingBlock {
	public static final MapCodec<DeepsandBlock> CODEC = simpleCodec(properties -> new DeepsandBlock());

	public MapCodec<DeepsandBlock> codec() {
		return CODEC;
	}

	public DeepsandBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SAND).strength(0.5f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}