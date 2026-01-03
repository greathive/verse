package net.mcreator.verse.procedures;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.network.PlayPlayerAnimationMessage;
import net.mcreator.verse.init.VerseModMobEffects;
import net.mcreator.verse.VerseMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class MobParryProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double z, Entity entity) {
		execute(null, world, x, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double z, Entity entity) {
		if (entity == null)
			return;
		boolean parry = false;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("verse:canparry")))) {
			if (!(entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(VerseModMobEffects.PARRY_CD))) {
				if (!(entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(VerseModMobEffects.PARRY_FRAME))) {
					if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
						if (((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) != null ? entity.distanceTo((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null)) : -1) <= 5) {
							if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) instanceof LivingEntity _entity ? _entity.swinging : false) {
								parry = true;
							}
							if ((world.getBlockState(BlockPos.containing(x, (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() - 1, z))).getBlock() == Blocks.AIR) {
								parry = true;
							}
							if (parry == true) {
								if (!((entity instanceof LivingEntity _livEnt ? _livEnt.hurtTime : 0) != 0)) {
									if (!((entity instanceof LivingEntity _entity) ? _entity.isUsingItem() : false)) {
										if (!(entity instanceof LivingEntity _entity ? _entity.swinging : false)) {
											if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
												_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_FRAME, 10, 0, false, false));
											if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
												_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD, 50, 0, false, false));
											if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
												_entity.addEffect(new MobEffectInstance(VerseModMobEffects.NO_ATTACK, 10, 0, false, false));
											if (entity instanceof LivingEntity _entity)
												_entity.stopUsingItem();
											if (!entity.level().isClientSide()) {
												// Server side - send packet to all clients
												PacketDistributor.sendToPlayersTrackingEntity(entity, new PlayPlayerAnimationMessage(entity.getId(), "verse:parry", true, true));
												VerseMod.LOGGER.info("Sent animation packet for zombie ID: " + entity.getId());
											}
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
			}
		}
	}
}