package net.mcreator.verse.procedures;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.verse.network.PlayPlayerAnimationMessage;
import net.mcreator.verse.VerseMod;

public class MobanimteststickLivingEntityIsHitWithItemProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("verse:canparry")))) {
			if (!entity.level().isClientSide()) {
				// Server side - send packet to all clients
				PacketDistributor.sendToPlayersTrackingEntity(entity, new PlayPlayerAnimationMessage(entity.getId(), "verse:rapidpunches", true, true));
				VerseMod.LOGGER.info("Sent animation packet for zombie ID: " + entity.getId());
			}
		}
	}
}