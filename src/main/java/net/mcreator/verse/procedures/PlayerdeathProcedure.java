package net.mcreator.verse.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.verse.network.VerseModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PlayerdeathProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity());
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			if ((entity.level().dimension()) == Level.OVERWORLD) {
				if (!(entity.getData(VerseModVariables.PLAYER_VARIABLES).life).equals("tattered")) {
					{
						VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
						_vars.life = "tattered";
						_vars.markSyncDirty();
					}
				} else if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).life).equals("tattered")) {
					{
						VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
						_vars.life = "drowned";
						_vars.markSyncDirty();
					}
				}
			} else {
				if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).life).equals("drowned")) {
					{
						VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
						_vars.life = "wiped";
						_vars.markSyncDirty();
					}
				} else {
					{
						VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
						_vars.life = "drowned";
						_vars.markSyncDirty();
					}
				}
			}
			if ((entity.getData(VerseModVariables.PLAYER_VARIABLES).life).equals("wiped")) {
				{
					VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
					_vars.life = "\"\"";
					_vars.markSyncDirty();
				}
			}
		}
	}
}