package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.verse.network.VerseModVariables;

public class StrengthExpProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double maxvalue = 0;
		if (!(((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) == null) && ((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null).tickCount
				- (((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) instanceof LivingEntity _livEnt ? _livEnt.getLastHurtByMobTimestamp() : 0) < 600) {
			maxvalue = ((entity instanceof LivingEntity _entity) ? _entity.getLastHurtMob() : null) instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1;
		} else {
			maxvalue = 0;
		}
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.strExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).strExp + Mth.nextInt(RandomSource.create(), 0, (int) maxvalue);
			_vars.markSyncDirty();
		}
	}
}