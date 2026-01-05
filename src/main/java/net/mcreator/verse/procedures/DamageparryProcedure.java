package net.mcreator.verse.procedures;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.network.PlayPlayerAnimationMessage;
import net.mcreator.verse.init.VerseModMobEffects;
import net.mcreator.verse.init.VerseModAttributes;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DamageparryProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, damagesource, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity sourceentity) {
		if (damagesource == null || entity == null || sourceentity == null)
			return;
		Vec3 parry1 = Vec3.ZERO;
		Vec3 parry2 = Vec3.ZERO;
		String anim = "";
		if (entity instanceof Player) {
			if (entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(VerseModMobEffects.PARRY_FRAME)) {
				if (!damagesource.is(TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("verse:parryblacklist")))) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:parry")), SoundSource.PLAYERS, 1, (float) Mth.nextDouble(RandomSource.create(), 0.8, 1.2));
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:parry")), SoundSource.PLAYERS, 1, (float) Mth.nextDouble(RandomSource.create(), 0.8, 1.2), false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_FRAME,
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.PARRY_FRAME) ? _livEnt.getEffect(VerseModMobEffects.PARRY_FRAME).getDuration() : 0) + 6, 0, false, false));
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(VerseModMobEffects.PARRY_CD,
								entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.PARRY_FRAME) ? _livEnt.getEffect(VerseModMobEffects.PARRY_FRAME).getDuration() : 0, 0, false, false));
					if (sourceentity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(VerseModMobEffects.NO_ATTACK,
								entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.PARRY_FRAME) ? _livEnt.getEffect(VerseModMobEffects.PARRY_FRAME).getDuration() : 0, 0, false, false));
					if (entity instanceof LivingEntity _entity)
						_entity.removeEffect(VerseModMobEffects.PARRY_CD);
					if (entity instanceof LivingEntity _entity)
						_entity.removeEffect(VerseModMobEffects.NO_ATTACK);
					anim = "parrysuccess_" + GetWeaponForAnimProcedure.execute(entity) + Math.round(Mth.nextInt(RandomSource.create(), 1, 2));
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
					if ((entity instanceof LivingEntity _livingEntity14 && _livingEntity14.getAttributes().hasAttribute(VerseModAttributes.POSTURE) ? _livingEntity14.getAttribute(VerseModAttributes.POSTURE).getValue() : 0)
							- PostureCalcProcedure.execute(sourceentity) > 0) {
						if (entity instanceof LivingEntity _livingEntity16 && _livingEntity16.getAttributes().hasAttribute(VerseModAttributes.POSTURE))
							_livingEntity16.getAttribute(VerseModAttributes.POSTURE)
									.setBaseValue(((entity instanceof LivingEntity _livingEntity15 && _livingEntity15.getAttributes().hasAttribute(VerseModAttributes.POSTURE) ? _livingEntity15.getAttribute(VerseModAttributes.POSTURE).getValue() : 0)
											- PostureCalcProcedure.execute(sourceentity)));
					} else {
						if (entity instanceof LivingEntity _livingEntity17 && _livingEntity17.getAttributes().hasAttribute(VerseModAttributes.POSTURE))
							_livingEntity17.getAttribute(VerseModAttributes.POSTURE).setBaseValue(0);
					}
					if (event instanceof ICancellableEvent _cancellable) {
						_cancellable.setCanceled(true);
					}
				}
			} else if (entity instanceof LivingEntity _livEnt18 && _livEnt18.hasEffect(VerseModMobEffects.BLOCK_FRAME)) {
				if (entity instanceof LivingEntity _livingEntity20 && _livingEntity20.getAttributes().hasAttribute(VerseModAttributes.POSTURE))
					_livingEntity20.getAttribute(VerseModAttributes.POSTURE)
							.setBaseValue(((entity instanceof LivingEntity _livingEntity19 && _livingEntity19.getAttributes().hasAttribute(VerseModAttributes.POSTURE) ? _livingEntity19.getAttribute(VerseModAttributes.POSTURE).getValue() : 0)
									+ PostureCalcProcedure.execute(sourceentity)));
				if ((entity instanceof LivingEntity _livingEntity21 && _livingEntity21.getAttributes().hasAttribute(VerseModAttributes.POSTURE)
						? _livingEntity21.getAttribute(VerseModAttributes.POSTURE).getValue()
						: 0) < (entity instanceof LivingEntity _livingEntity22 && _livingEntity22.getAttributes().hasAttribute(VerseModAttributes.MAX_POSTURE) ? _livingEntity22.getAttribute(VerseModAttributes.MAX_POSTURE).getValue() : 0)) {
					if (event instanceof ICancellableEvent _cancellable) {
						_cancellable.setCanceled(true);
					}
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:blockedattack")), SoundSource.PLAYERS, 1, (float) Mth.nextDouble(RandomSource.create(), 1.5, 2));
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:blockedattack")), SoundSource.PLAYERS, 1, (float) Mth.nextDouble(RandomSource.create(), 1.5, 2), false);
						}
					}
					entity.setDeltaMovement(new Vec3(((entity.getX() - sourceentity.getX()) * 2), ((entity.getY() - sourceentity.getY()) * 2), ((entity.getZ() - sourceentity.getZ()) * 2)));
				} else {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:posturebreak")), SoundSource.PLAYERS, 1, (float) Mth.nextDouble(RandomSource.create(), 0.9, 1.1));
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("verse:posturebreak")), SoundSource.PLAYERS, 1, (float) Mth.nextDouble(RandomSource.create(), 0.9, 1.1), false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(VerseModMobEffects.GUARDBROKEN, 35, 0, false, false));
					if (entity instanceof LivingEntity _livingEntity35 && _livingEntity35.getAttributes().hasAttribute(VerseModAttributes.POSTURE))
						_livingEntity35.getAttribute(VerseModAttributes.POSTURE).setBaseValue(0);
					if (entity instanceof Player) {
						if (entity.level().isClientSide()) {
							CompoundTag data = entity.getPersistentData();
							data.putString("PlayerCurrentAnimation", "verse:guardbreak");
							data.putBoolean("OverrideCurrentAnimation", true);
							data.putBoolean("FirstPersonAnimation", true);
						} else {
							PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "verse:guardbreak", true, true));
						}
					}
				}
			} else if (sourceentity instanceof LivingEntity _livEnt37 && _livEnt37.hasEffect(VerseModMobEffects.NO_ATTACK)) {
				if (event instanceof ICancellableEvent _cancellable) {
					_cancellable.setCanceled(true);
				}
			}
		}
	}
}