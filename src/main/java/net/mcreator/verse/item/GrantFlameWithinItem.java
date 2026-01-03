package net.mcreator.verse.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.mcreator.verse.procedures.GrantFlameWithinRightclickedProcedure;

public class GrantFlameWithinItem extends Item {
	public GrantFlameWithinItem() {
		super(new Item.Properties());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		GrantFlameWithinRightclickedProcedure.execute(entity);
		return ar;
	}
}