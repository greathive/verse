package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.verse.world.inventory.MainGuiMenu;
import net.mcreator.verse.network.VerseModVariables;

import io.netty.buffer.Unpooled;

public class ConfirmChoiceProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.choseattunement = true;
			_vars.markSyncDirty();
		}
		if (entity instanceof ServerPlayer _ent) {
			BlockPos _bpos = BlockPos.containing(x, y, z);
			_ent.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("MainGui");
				}

				@Override
				public boolean shouldTriggerClientSideContainerClosingOnOpen() {
					return false;
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new MainGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
				}
			}, _bpos);
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.contains(")")) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.attuneExp = 260;
				_vars.markSyncDirty();
			}
			InvestpointProcedure.execute(world, x, y, z, entity, entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.substring((int) entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.indexOf("(") + "(".length(),
					(int) entity.getData(VerseModVariables.PLAYER_VARIABLES).choice.indexOf(")")));
		}
	}
}