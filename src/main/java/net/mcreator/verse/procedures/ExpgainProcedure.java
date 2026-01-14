package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.network.VerseModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ExpgainProcedure {
	@SubscribeEvent
	public static void onPlayerXPChange(PlayerXpEvent.XpChange event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getAmount());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double amount) {
		execute(null, world, x, y, z, entity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, double amount) {
		if (entity == null)
			return;
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).expgain >= Math.round(112 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power))) {
			if ((((((((((((entity.getData(VerseModVariables.PLAYER_VARIABLES).power * 6 - entity.getData(VerseModVariables.PLAYER_VARIABLES).str) - entity.getData(VerseModVariables.PLAYER_VARIABLES).fort)
					- entity.getData(VerseModVariables.PLAYER_VARIABLES).intel) - entity.getData(VerseModVariables.PLAYER_VARIABLES).agility) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm)
					- entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw)
					- entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing) - entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave)
					- entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend == 0) {
				if (false == entity.getData(VerseModVariables.PLAYER_VARIABLES).hasCard) {
					ValidTalentListProcedure.execute(world, entity);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:powerup")), SoundSource.PLAYERS, (float) 0.2, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:powerup")), SoundSource.PLAYERS, (float) 0.2, 1, false);
						}
					}
					{
						VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
						_vars.expgain = 0;
						_vars.power = entity.getData(VerseModVariables.PLAYER_VARIABLES).power + 1;
						_vars.hasCard = true;
						_vars.pickedcards = false;
						_vars.markSyncDirty();
					}
				}
			}
		} else {
			if (false == entity.getData(VerseModVariables.PLAYER_VARIABLES).hasCard) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.expgain = entity.getData(VerseModVariables.PLAYER_VARIABLES).expgain + amount;
					_vars.markSyncDirty();
				}
			}
		}
	}
}