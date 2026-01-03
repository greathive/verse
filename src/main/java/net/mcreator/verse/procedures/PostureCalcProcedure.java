package net.mcreator.verse.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.verse.init.VerseModAttributes;

public class PostureCalcProcedure {
	public static double execute(Entity source) {
		if (source == null)
			return 0;
		double dmg = 0;
		dmg = source instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(VerseModAttributes.POSTURE_BONUS) ? _livingEntity0.getAttribute(VerseModAttributes.POSTURE_BONUS).getValue() : 0;
		return dmg;
	}
}