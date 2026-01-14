/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.verse.item.*;
import net.mcreator.verse.VerseMod;

public class VerseModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(VerseMod.MODID);
	public static final DeferredItem<Item> TEMP;
	public static final DeferredItem<Item> NPC_SPAWN_EGG;
	public static final DeferredItem<Item> GRANT_FLAME_WITHIN;
	public static final DeferredItem<Item> TALENTCHECKER;
	public static final DeferredItem<Item> GRAN_SUDA;
	public static final DeferredItem<Item> ANKLE_WEIGHTS;
	public static final DeferredItem<Item> DUMBBELL;
	public static final DeferredItem<Item> PRAYER_BEADS;
	public static final DeferredItem<Item> TRAINING_VEST;
	public static final DeferredItem<Item> DEEPSPINDLE;
	public static final DeferredItem<Item> MEGALODAUNT_SPAWN_EGG;
	public static final DeferredItem<Item> ORCHESTRATOR_BASIN;
	static {
		TEMP = REGISTRY.register("temp", TempItem::new);
		NPC_SPAWN_EGG = REGISTRY.register("npc_spawn_egg", () -> new DeferredSpawnEggItem(VerseModEntities.NPC, -1, -1, new Item.Properties()));
		GRANT_FLAME_WITHIN = REGISTRY.register("grant_flame_within", GrantFlameWithinItem::new);
		TALENTCHECKER = REGISTRY.register("talentchecker", TalentcheckerItem::new);
		GRAN_SUDA = REGISTRY.register("gran_suda", GranSudaItem::new);
		ANKLE_WEIGHTS = REGISTRY.register("ankle_weights", AnkleWeightsItem::new);
		DUMBBELL = REGISTRY.register("dumbbell", DumbbellItem::new);
		PRAYER_BEADS = REGISTRY.register("prayer_beads", PrayerBeadsItem::new);
		TRAINING_VEST = REGISTRY.register("training_vest", TrainingVestItem::new);
		DEEPSPINDLE = REGISTRY.register("deepspindle", DeepspindleItem::new);
		MEGALODAUNT_SPAWN_EGG = REGISTRY.register("megalodaunt_spawn_egg", () -> new DeferredSpawnEggItem(VerseModEntities.MEGALODAUNT, -13210, -13261, new Item.Properties()));
		ORCHESTRATOR_BASIN = block(VerseModBlocks.ORCHESTRATOR_BASIN, new Item.Properties().rarity(Rarity.UNCOMMON));
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
}