package net.mcreator.verse.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GetWeaponForAnimProcedure {
	public static String execute(Entity entity) {
		if (entity == null) {
			return "fist";
		}
		
		if (!(entity instanceof LivingEntity livingEntity)) {
			return "fist";
		}
		
		ItemStack mainHandItem = livingEntity.getMainHandItem();
		
		if (mainHandItem.isEmpty()) {
			return "fist";
		}
		
		if (mainHandItem.getItem() instanceof SwordItem) {
			return "sword";
		}
		
		if (mainHandItem.getItem() instanceof DiggerItem) {
			return "axe";
		}
		
		return "fist";
	}
}