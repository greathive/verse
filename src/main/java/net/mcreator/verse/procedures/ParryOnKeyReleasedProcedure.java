package net.mcreator.verse.procedures;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.verse.network.PlayPlayerAnimationMessage;
import net.mcreator.verse.init.VerseModMobEffects;

public class ParryOnKeyReleasedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String anim = "";
		CompoundTag data = entity.getPersistentData();
		anim = data.getString("PlayerCurrentAnimation");;
		if (anim.contains("parry_")) {
			if (entity instanceof Player) {
				if (entity.level().isClientSide()) {
					data.remove("PlayerCurrentAnimation");
					data.remove("PlayerAnimationProgress");
					data.putBoolean("ResetPlayerAnimation", true);
					data.putBoolean("FirstPersonAnimation", false);
				} else {
					PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "", false, false));
				}
			}
		}
		if (entity instanceof LivingEntity _entity)
			_entity.removeEffect(VerseModMobEffects.BLOCK_FRAME);
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD, 15, 0));
	}
}