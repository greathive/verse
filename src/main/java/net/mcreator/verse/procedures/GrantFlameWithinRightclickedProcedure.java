package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;

import net.mcreator.verse.network.VerseModVariables;

public class GrantFlameWithinRightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (new Object() {
			public boolean getValue() {
				boolean retBool = Minecraft.getInstance().options.keyUp.isDown();
				if (retBool) {
					if (Minecraft.getInstance().options.keyUp.getKeyModifier().toString().equals("SHIFT")) {
						retBool = Screen.hasShiftDown();
					} else if (Minecraft.getInstance().options.keyUp.getKeyModifier().toString().equals("CONTROL")) {
						retBool = Screen.hasControlDown();
					} else if (Minecraft.getInstance().options.keyUp.getKeyModifier().toString().equals("ALT")) {
						retBool = Screen.hasAltDown();
					}
				}
				return retBool;
			}
		}.getValue()) {
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.talentlist = "";
				_vars.markSyncDirty();
			}
		} else {
			if (entity.isShiftKeyDown()) {
				GrantTalentProcedure.execute(entity, "Carnivore");
			} else {
				GrantTalentProcedure.execute(entity, "Flame Within");
			}
		}
	}
}