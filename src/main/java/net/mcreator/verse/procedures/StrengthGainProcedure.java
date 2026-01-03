package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.network.VerseModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class StrengthGainProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		execute(event, event.getLevel(), event.getState(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, BlockState blockstate, Entity entity) {
		execute(null, world, blockstate, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		if (!(blockstate.getBlock() instanceof SlabBlock)) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.strExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp + blockstate.getDestroySpeed(world, BlockPos.containing(0, 0, 0));
				_vars.markSyncDirty();
			}
		} else {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.strExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp + blockstate.getDestroySpeed(world, BlockPos.containing(0, 0, 0)) / 2;
				_vars.markSyncDirty();
			}
		}
	}
}