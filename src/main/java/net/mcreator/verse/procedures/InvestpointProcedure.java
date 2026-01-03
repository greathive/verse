package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.network.VerseModVariables;

public class InvestpointProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, String stat) {
		if (entity == null || stat == null)
			return;
		double attunement = 0;
		double exp = 0;
		if (AvailablestatcheckerProcedure.execute(entity)) {
			if ((stat).equals("flamecharm")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("galebreathe")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("frostdraw")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("thundercall")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("shadowcast")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("ironsing")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("lifeweave")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("bloodrend")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).attuneExp;
			} else if ((stat).equals("strength")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).str;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp;
			} else if ((stat).equals("fortitude")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).fort;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).fortExp;
			} else if ((stat).equals("agility")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).agility;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).aglExp;
			} else if ((stat).equals("mind")) {
				attunement = entity.getData(VerseModVariables.PLAYER_VARIABLES).intel;
				exp = entity.getData(VerseModVariables.PLAYER_VARIABLES).intExp;
			}
			if (Math.round(exp / (100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power))) >= 1) {
				if (attunement < 35) {
					attunement = attunement + 1;
					exp = exp - 100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:attributeincrease")), SoundSource.PLAYERS, (float) 0.2, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:attributeincrease")), SoundSource.PLAYERS, (float) 0.2, 1, false);
						}
					}
				} else if (attunement < 50 && CheckForTalentProcedure.execute(entity, "(x uncapped)".replace("x", stat))) {
					attunement = attunement + 1;
					exp = exp - 100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:attributeincrease")), SoundSource.PLAYERS, (float) 0.2, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:attributeincrease")), SoundSource.PLAYERS, (float) 0.2, 1, false);
						}
					}
				} else {
					if (attunement == 35) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("You've hit a wall, maybe there is some way to improve this attribute further?"), true);
					} else if (attunement == 50) {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("You have achieved the very pinnacle of this attribute."), true);
					}
				}
			}
			if ((stat).equals("flamecharm")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Flamecharm = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("galebreathe")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Galebreathe = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("frostdraw")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Frostdraw = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("thundercall")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Thundercall = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("shadowcast")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Shadowcast = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("ironsing")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Ironsing = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("lifeweave")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Lifeweave = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("bloodrend")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.Bloodrend = attunement;
					_vars.attuneExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("strength")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.str = attunement;
					_vars.strExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("fortitude")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.fort = attunement;
					_vars.fortExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("agility")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.agility = attunement;
					_vars.aglExp = exp;
					_vars.markSyncDirty();
				}
			} else if ((stat).equals("mind")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.intel = attunement;
					_vars.intExp = exp;
					_vars.markSyncDirty();
				}
			}
		}
	}
}