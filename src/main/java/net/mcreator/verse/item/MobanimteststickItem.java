package net.mcreator.verse.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.verse.procedures.MobanimteststickLivingEntityIsHitWithItemProcedure;

public class MobanimteststickItem extends Item {
	public MobanimteststickItem() {
		super(new Item.Properties());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		MobanimteststickLivingEntityIsHitWithItemProcedure.execute(entity);
		return retval;
	}
}