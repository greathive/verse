package net.mcreator.verse.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.mcreator.verse.procedures.OutfitReseterRightclickedProcedure;

public class OutfitReseterItem extends Item {
	public OutfitReseterItem() {
		super(new Item.Properties());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		OutfitReseterRightclickedProcedure.execute(entity);
		return ar;
	}
}