package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;

import net.mcreator.verse.network.VerseModVariables;

public class Addpoint5Procedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String stat = "";
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall > 0) {
			stat = "thundercall";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw > 0) {
			stat = "frostdraw";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe > 0) {
			stat = "galebreathe";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm > 0) {
			stat = "flamecharm";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave > 0) {
			stat = "lifeweave";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend > 0) {
			stat = "bloodrend";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing > 0) {
			stat = "ironsing";
		} else if (entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast > 0) {
			stat = "shadowcast";
		}
		InvestpointProcedure.execute(world, x, y, z, entity, stat);
		if (entity instanceof LivingEntity _entity)
			_entity.swing(InteractionHand.MAIN_HAND, true);
	}
}