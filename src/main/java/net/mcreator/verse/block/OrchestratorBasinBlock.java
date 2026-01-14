package net.mcreator.verse.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.procedures.OrchestratorBasinBlockValidPlacementConditionProcedure;

public class OrchestratorBasinBlock extends Block {
	public OrchestratorBasinBlock() {
		super(BlockBehaviour.Properties.of().strength(10f, 40f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.or(box(24, 0, 24, 32, 16, 32), box(24, 0, 0, 32, 16, 8), box(0, 0, 0, 8, 16, 8), box(0, 0, 24, 8, 16, 32), box(6, 0, 6, 26, 4, 26), box(4, 4, 4, 28, 10, 28), box(24, 10, 8, 30, 16, 24), box(2, 10, 8, 8, 16, 24),
				box(8, 10, 24, 24, 16, 30), box(8, 10, 2, 24, 16, 8));
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		if (worldIn instanceof LevelAccessor world) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			return OrchestratorBasinBlockValidPlacementConditionProcedure.execute(world, x, y, z);
		}
		return super.canSurvive(blockstate, worldIn, pos);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		return !state.canSurvive(world, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}
}