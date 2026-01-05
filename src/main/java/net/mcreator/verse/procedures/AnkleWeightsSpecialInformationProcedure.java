package net.mcreator.verse.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

import net.mcreator.verse.init.VerseModItems;

public class AnkleWeightsSpecialInformationProcedure {
	public static String execute(ItemStack itemstack) {
		String active = "";
		String stattext = "";
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("active") == true) {
			active = "Active, right-click to disable";
		} else {
			active = "Inactive, right-click to enable";
		}
		if (itemstack.getItem() == VerseModItems.ANKLE_WEIGHTS.get()) {
			stattext = "When enabled, gain agility stat exp both passively and when you gain normal experience, however you will be slowed while it is active.";
		} else if (itemstack.getItem() == VerseModItems.DUMBBELL.get()) {
			stattext = "When enabled, gain strength stat exp both passively and when you gain normal experience, however you will deal less damage and mine blocks slower while it is active.";
		} else if (itemstack.getItem() == VerseModItems.PRAYER_BEADS.get()) {
			stattext = "When enabled, gain mind stat exp both passively and when you gain normal experience, however your usage of items will be slowed while it is active.";
		} else if (itemstack.getItem() == VerseModItems.TRAINING_VEST.get()) {
			stattext = "When enabled, gain fortitude stat exp both passively and when you gain normal experience, however you will take more damage while it is active.";
		}
		return active + "\n" + stattext;
	}
}