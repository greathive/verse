/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.verse.VerseMod;

@EventBusSubscriber
public class VerseModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VerseMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERSE_WEAPONS = REGISTRY.register("verse_weapons",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.verse.verse_weapons")).icon(() -> new ItemStack(VerseModItems.GRAN_SUDA.get())).displayItems((parameters, tabData) -> {
				tabData.accept(VerseModItems.GRAN_SUDA.get());
				tabData.accept(VerseModItems.DEEPSPINDLE.get());
			}).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERSE = REGISTRY.register("verse",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.verse.verse")).icon(() -> new ItemStack(VerseModBlocks.ORCHESTRATOR_BASIN.get())).displayItems((parameters, tabData) -> {
				tabData.accept(VerseModItems.MEGALODAUNT_SPAWN_EGG.get());
				tabData.accept(VerseModBlocks.ORCHESTRATOR_BASIN.get().asItem());
				tabData.accept(VerseModBlocks.TEARSTONE.get().asItem());
				tabData.accept(VerseModBlocks.TEARSTONE_STAIR.get().asItem());
				tabData.accept(VerseModBlocks.TEARSTONE_SLAB.get().asItem());
				tabData.accept(VerseModBlocks.TEARSTONE_WALL.get().asItem());
				tabData.accept(VerseModBlocks.COBBLED_TEARSTONE.get().asItem());
				tabData.accept(VerseModBlocks.COBBLED_TEARSTONE_STAIR.get().asItem());
				tabData.accept(VerseModBlocks.COBBLED_TEARSTONE_WALL.get().asItem());
				tabData.accept(VerseModBlocks.DEEPSAND.get().asItem());
				tabData.accept(VerseModBlocks.SOAKSTONE.get().asItem());
				tabData.accept(VerseModBlocks.SOAKSTONE_STAIR.get().asItem());
				tabData.accept(VerseModBlocks.SOAKSTONE_SLAB.get().asItem());
				tabData.accept(VerseModBlocks.SOAKSTONE_WALL.get().asItem());
				tabData.accept(VerseModBlocks.FLOODSTONE.get().asItem());
				tabData.accept(VerseModBlocks.DEEPLIGHT.get().asItem());
				tabData.accept(VerseModBlocks.DEPTHS_CORAL.get().asItem());
				tabData.accept(VerseModBlocks.ALESTRIAN_CORAL.get().asItem());
				tabData.accept(VerseModBlocks.DROWNED_CORAL.get().asItem());
				tabData.accept(VerseModBlocks.WHEAT_CORAL.get().asItem());
				tabData.accept(VerseModBlocks.MUSHROOM_CORAL.get().asItem());
				tabData.accept(VerseModBlocks.DEPTHS_CORAL_SLAB.get().asItem());
				tabData.accept(VerseModBlocks.ALESTRIAN_CORAL_SLAB.get().asItem());
				tabData.accept(VerseModBlocks.DROWNED_CORAL_SLAB.get().asItem());
				tabData.accept(VerseModBlocks.WHEAT_CORAL_SLAB.get().asItem());
				tabData.accept(VerseModBlocks.MUSHROOM_CORAL_SLAB.get().asItem());
			}).withSearchBar().withTabsBefore(VERSE_WEAPONS.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
			if (tabData.hasPermissions()) {
				tabData.accept(VerseModItems.TEMP.get());
				tabData.accept(VerseModItems.NPC_SPAWN_EGG.get());
				tabData.accept(VerseModItems.GRANT_FLAME_WITHIN.get());
				tabData.accept(VerseModItems.TALENTCHECKER.get());
				tabData.accept(VerseModItems.MANTRATESTINGITEM.get());
			}
		}
	}
}