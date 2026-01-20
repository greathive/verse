package net.mcreator.verse.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.Minecraft;

public class MantraSelectionOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double keynumber = 0;
		String key = "";
		String texturechoice = "";
		if (entity.tickCount - entity.getPersistentData().getDouble("mantraselectiontick") <= 100) {
			entity.getPersistentData().putDouble("mantraselectiontick", 0);
			texturechoice = "mantradeactive";
			ResourceLocation soundLocation = ResourceLocation.fromNamespaceAndPath("verse", texturechoice);
			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(soundLocation), 1.0F));
		} else {
			entity.getPersistentData().putDouble("mantraselectiontick", (entity.tickCount));
			texturechoice = "mantraactive";
			ResourceLocation soundLocation = ResourceLocation.fromNamespaceAndPath("verse", texturechoice);
			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(soundLocation), 1.0F));
		}
		if (entity.getPersistentData().getDouble("selected") == 0) {
			entity.getPersistentData().putDouble("selected", 1);
		}
	}
}