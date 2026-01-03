package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.verse.init.VerseModMobEffects;
import net.mcreator.verse.init.VerseModAttributes;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ReducePostureProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(VerseModMobEffects.BLOCK_FRAME))
				&& 0 < (entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(VerseModAttributes.POSTURE) ? _livingEntity1.getAttribute(VerseModAttributes.POSTURE).getValue() : 0)) {
			if (entity instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(VerseModAttributes.POSTURE))
				_livingEntity3.getAttribute(VerseModAttributes.POSTURE)
						.setBaseValue(((entity instanceof LivingEntity _livingEntity2 && _livingEntity2.getAttributes().hasAttribute(VerseModAttributes.POSTURE) ? _livingEntity2.getAttribute(VerseModAttributes.POSTURE).getValue() : 0) - 0.025));
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(
					Component.literal(((entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(VerseModAttributes.POSTURE) ? _livingEntity4.getAttribute(VerseModAttributes.POSTURE).getValue() : 0) + "/"
							+ (entity instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(VerseModAttributes.MAX_POSTURE) ? _livingEntity5.getAttribute(VerseModAttributes.MAX_POSTURE).getValue() : 0))),
					true);
	}
}