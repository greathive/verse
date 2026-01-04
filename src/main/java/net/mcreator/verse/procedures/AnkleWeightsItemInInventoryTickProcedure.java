package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.component.DataComponents;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.init.VerseModMobEffects;

public class AnkleWeightsItemInInventoryTickProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		Entity player = null;
		double speed = 0;
		double totalstatexp = 0;
		if ((float) (entity.tickCount / 49.5) == Math.floor(entity.tickCount / 49.5)) {
			if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("active") == true) {
				player = entity;
				speed = player.getDeltaMovement().length();;
				if (world instanceof ServerLevel _level) {
					itemstack.hurtAndBreak(1, _level, null, _stkprov -> {
					});
				}
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.aglExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).aglExp + Mth.nextInt(RandomSource.create(), (int) ((speed / 20) * 100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power)),
							(int) (speed * 100 * Math.pow(1.24, entity.getData(VerseModVariables.PLAYER_VARIABLES).power)));
					_vars.markSyncDirty();
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(VerseModMobEffects.AGILITY_TRAINING, 100, 0, false, false));
			}
		}
	}
}