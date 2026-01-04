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
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERSE = REGISTRY.register("verse",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.verse.verse")).icon(() -> new ItemStack(VerseModBlocks.OUTFIT_TABLE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(VerseModBlocks.OUTFIT_TABLE.get().asItem());
			}).withSearchBar().build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERSE_WEAPONS = REGISTRY.register("verse_weapons",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.verse.verse_weapons")).icon(() -> new ItemStack(VerseModItems.GRAN_SUDA.get())).displayItems((parameters, tabData) -> {
				tabData.accept(VerseModItems.GRAN_SUDA.get());
			}).withTabsBefore(VERSE.getId()).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TRAINING_GEAR = REGISTRY.register("training_gear",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.verse.training_gear")).icon(() -> new ItemStack(VerseModItems.ANKLE_WEIGHTS.get())).displayItems((parameters, tabData) -> {
				tabData.accept(VerseModItems.ANKLE_WEIGHTS.get());
			}).withTabsBefore(VERSE_WEAPONS.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
			if (tabData.hasPermissions()) {
				tabData.accept(VerseModItems.TEMP.get());
				tabData.accept(VerseModItems.NPC_SPAWN_EGG.get());
				tabData.accept(VerseModItems.OUTFIT_RESETER.get());
				tabData.accept(VerseModItems.GRANT_FLAME_WITHIN.get());
				tabData.accept(VerseModItems.TALENTCHECKER.get());
			}
		}
	}
}