package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class StrengthGainCombatProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity(), event.getSource().getDirectEntity(), event.getAmount());
		}
	}

	public static void execute(Entity entity, Entity immediatesourceentity, double amount) {
		execute(null, entity, immediatesourceentity, amount);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity immediatesourceentity, double amount) {
		if (entity == null || immediatesourceentity == null)
			return;
		if (immediatesourceentity instanceof Player) {
			{
				VerseModVariables.PlayerVariables _vars = immediatesourceentity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.strExp = immediatesourceentity.getData(VerseModVariables.PLAYER_VARIABLES).strExp + amount * (immediatesourceentity.getData(VerseModVariables.PLAYER_VARIABLES).power / 2);
				_vars.markSyncDirty();
			}
		}
		if (entity instanceof Player) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.fortExp = entity.getData(VerseModVariables.PLAYER_VARIABLES).fortExp + amount * ((entity instanceof LivingEntity _livEnt ? _livEnt.getArmorValue() : 0) + 1) * (entity.getData(VerseModVariables.PLAYER_VARIABLES).power / 4);
				_vars.markSyncDirty();
			}
		}
	}
}