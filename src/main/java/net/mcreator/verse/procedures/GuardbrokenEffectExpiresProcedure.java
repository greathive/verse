package net.mcreator.verse.procedures;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.verse.network.PlayPlayerAnimationMessage;

public class GuardbrokenEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			if (entity.level().isClientSide()) {
				CompoundTag data = entity.getPersistentData();
				data.putString("PlayerCurrentAnimation", "verse:animationcancel");
				data.putBoolean("OverrideCurrentAnimation", true);
				data.putBoolean("FirstPersonAnimation", false);
			} else {
				PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "verse:animationcancel", true, false));
			}
		}
	}
}