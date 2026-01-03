package net.mcreator.verse.procedures;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.network.PlayPlayerAnimationMessage;
import net.mcreator.verse.init.VerseModMobEffects;

public class ParryOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String anim = "";
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(VerseModMobEffects.PARRY_CD))) {
			if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).currentmantra).equals("")) {
				if (!((entity instanceof LivingEntity _livEnt ? _livEnt.hurtTime : 0) != 0)) {
					if (!((entity instanceof LivingEntity _entity) ? _entity.isUsingItem() : false)) {
						if (!(entity instanceof LivingEntity _entity ? _entity.swinging : false)) {
							anim = "parry_" + GetWeaponForAnimProcedure.execute(entity);
							if (entity instanceof Player) {
								if (entity.level().isClientSide()) {
									CompoundTag data = entity.getPersistentData();
									data.putString("PlayerCurrentAnimation", "verse:" + anim);
									data.putBoolean("OverrideCurrentAnimation", true);
									data.putBoolean("FirstPersonAnimation", true);
								} else {
									PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "verse:" + anim, true, true));
								}
							}
							if (entity instanceof Player) {
								if (entity.level().isClientSide()) {
									CompoundTag data = entity.getPersistentData();
									data.putString("PlayerCurrentAnimation", "verse:parry_sword");
									data.putBoolean("OverrideCurrentAnimation", true);
									data.putBoolean("FirstPersonAnimation", true);
								} else {
									PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "verse:parry_sword", true, true));
								}
							}
							if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_FRAME, 6, 0, false, false));
							if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD, 35, 0, false, false));
							if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(VerseModMobEffects.NO_ATTACK, 12, 0, false, false));
							if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(VerseModMobEffects.BLOCK_FRAME, 6, 0, false, false));
						}
					}
				} else {
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD, 20, 0, false, false));
				}
			}
		}
	}
}