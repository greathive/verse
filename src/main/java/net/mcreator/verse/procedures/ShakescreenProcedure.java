package net.mcreator.verse.procedures;

import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.init.VerseModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber(value = Dist.CLIENT)
public class ShakescreenProcedure {
	public static ViewportEvent.ComputeCameraAngles provider = null;

	public static void setAngles(float yaw, float pitch, float roll) {
		provider.setYaw(yaw);
		provider.setPitch(pitch);
		provider.setRoll(roll);
	}

	@SubscribeEvent
	public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
		provider = event;
		ClientLevel level = Minecraft.getInstance().level;
		Entity entity = provider.getCamera().getEntity();
		if (level != null && entity != null) {
			Vec3 entPos = entity.getPosition((float) provider.getPartialTick());
			execute(provider, entity, provider.getPitch(), provider.getRoll(), provider.getYaw());
		}
	}

	public static void execute(Entity entity, double pitch, double roll, double yaw) {
		execute(null, entity, pitch, roll, yaw);
	}

	private static void execute(@Nullable Event event, Entity entity, double pitch, double roll, double yaw) {
		if (entity == null)
			return;
		double variable = 0;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(VerseModMobEffects.SCREENSHAKING)) {
			if (Math.random() < 0.5) {
				setAngles((float) (yaw + 0.1 + (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0)),
						(float) (pitch - Mth.nextDouble(RandomSource.create(),
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) * (-1) - 0.075,
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) + 0.075)),
						(float) (roll - Mth.nextDouble(RandomSource.create(),
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) * (-1) - 0.25,
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) + 0.25)));
			} else {
				setAngles((float) (yaw - (0.1 + (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0))),
						(float) (pitch - Mth.nextDouble(RandomSource.create(),
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) * (-1) - 0.1,
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) + 0.1)),
						(float) (roll - Mth.nextDouble(RandomSource.create(),
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) * (-1) - 0.1,
								(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(VerseModMobEffects.SCREENSHAKING) ? _livEnt.getEffect(VerseModMobEffects.SCREENSHAKING).getAmplifier() : 0) + 0.1)));
			}
		}
	}
}