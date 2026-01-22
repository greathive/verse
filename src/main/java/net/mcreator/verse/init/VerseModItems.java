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
	public static final DeferredItem<Item> DEEPSPINDLE;
	public static final DeferredItem<Item> MEGALODAUNT_SPAWN_EGG;
	public static final DeferredItem<Item> ORCHESTRATOR_BASIN;
	public static final DeferredItem<Item> MANTRATESTINGITEM;
	public static final DeferredItem<Item> TEARSTONE;
	public static final DeferredItem<Item> TEARSTONE_STAIR;
	public static final DeferredItem<Item> TEARSTONE_SLAB;
	public static final DeferredItem<Item> TEARSTONE_WALL;
	public static final DeferredItem<Item> COBBLED_TEARSTONE;
	public static final DeferredItem<Item> COBBLED_TEARSTONE_STAIR;
	public static final DeferredItem<Item> COBBLED_TEARSTONE_SLAB;
	public static final DeferredItem<Item> COBBLED_TEARSTONE_WALL;
	public static final DeferredItem<Item> DEEPSAND;
	public static final DeferredItem<Item> SOAKSTONE;
	public static final DeferredItem<Item> SOAKSTONE_STAIR;
	public static final DeferredItem<Item> SOAKSTONE_SLAB;
	public static final DeferredItem<Item> SOAKSTONE_WALL;
	public static final DeferredItem<Item> FLOODSTONE;
	public static final DeferredItem<Item> BARRIER;
	public static final DeferredItem<Item> DEEPLIGHT;
	public static final DeferredItem<Item> DEPTHS_CORAL;
	public static final DeferredItem<Item> DEEPLIGHTSPIKE;
	public static final DeferredItem<Item> ALESTRIAN_CORAL;
	public static final DeferredItem<Item> DROWNED_CORAL;
	public static final DeferredItem<Item> WHEAT_CORAL;
	public static final DeferredItem<Item> MUSHROOM_CORAL;
	public static final DeferredItem<Item> DEPTHS_CORAL_SLAB;
	public static final DeferredItem<Item> ALESTRIAN_CORAL_SLAB;
	public static final DeferredItem<Item> DROWNED_CORAL_SLAB;
	public static final DeferredItem<Item> WHEAT_CORAL_SLAB;
	public static final DeferredItem<Item> MUSHROOM_CORAL_SLAB;
	static {
		TEMP = REGISTRY.register("temp", TempItem::new);
		NPC_SPAWN_EGG = REGISTRY.register("npc_spawn_egg", () -> new DeferredSpawnEggItem(VerseModEntities.NPC, -1, -1, new Item.Properties()));
		GRANT_FLAME_WITHIN = REGISTRY.register("grant_flame_within", GrantFlameWithinItem::new);
		TALENTCHECKER = REGISTRY.register("talentchecker", TalentcheckerItem::new);
		GRAN_SUDA = REGISTRY.register("gran_suda", GranSudaItem::new);
		ANKLE_WEIGHTS = REGISTRY.register("ankle_weights", AnkleWeightsItem::new);
		DUMBBELL = REGISTRY.register("dumbbell", DumbbellItem::new);
		PRAYER_BEADS = REGISTRY.register("prayer_beads", PrayerBeadsItem::new);
		DEEPSPINDLE = REGISTRY.register("deepspindle", DeepspindleItem::new);
		MEGALODAUNT_SPAWN_EGG = REGISTRY.register("megalodaunt_spawn_egg", () -> new DeferredSpawnEggItem(VerseModEntities.MEGALODAUNT, -13210, -13261, new Item.Properties()));
		ORCHESTRATOR_BASIN = block(VerseModBlocks.ORCHESTRATOR_BASIN, new Item.Properties().rarity(Rarity.UNCOMMON));
		MANTRATESTINGITEM = REGISTRY.register("mantratestingitem", MantratestingitemItem::new);
		TEARSTONE = block(VerseModBlocks.TEARSTONE);
		TEARSTONE_STAIR = block(VerseModBlocks.TEARSTONE_STAIR);
		TEARSTONE_SLAB = block(VerseModBlocks.TEARSTONE_SLAB);
		TEARSTONE_WALL = block(VerseModBlocks.TEARSTONE_WALL);
		COBBLED_TEARSTONE = block(VerseModBlocks.COBBLED_TEARSTONE);
		COBBLED_TEARSTONE_STAIR = block(VerseModBlocks.COBBLED_TEARSTONE_STAIR);
		COBBLED_TEARSTONE_SLAB = block(VerseModBlocks.COBBLED_TEARSTONE_SLAB);
		COBBLED_TEARSTONE_WALL = block(VerseModBlocks.COBBLED_TEARSTONE_WALL);
		DEEPSAND = block(VerseModBlocks.DEEPSAND);
		SOAKSTONE = block(VerseModBlocks.SOAKSTONE);
		SOAKSTONE_STAIR = block(VerseModBlocks.SOAKSTONE_STAIR);
		SOAKSTONE_SLAB = block(VerseModBlocks.SOAKSTONE_SLAB);
		SOAKSTONE_WALL = block(VerseModBlocks.SOAKSTONE_WALL);
		FLOODSTONE = block(VerseModBlocks.FLOODSTONE);
		BARRIER = block(VerseModBlocks.BARRIER);
		DEEPLIGHT = block(VerseModBlocks.DEEPLIGHT);
		DEPTHS_CORAL = block(VerseModBlocks.DEPTHS_CORAL);
		DEEPLIGHTSPIKE = block(VerseModBlocks.DEEPLIGHTSPIKE);
		ALESTRIAN_CORAL = block(VerseModBlocks.ALESTRIAN_CORAL);
		DROWNED_CORAL = block(VerseModBlocks.DROWNED_CORAL);
		WHEAT_CORAL = block(VerseModBlocks.WHEAT_CORAL);
		MUSHROOM_CORAL = block(VerseModBlocks.MUSHROOM_CORAL);
		DEPTHS_CORAL_SLAB = block(VerseModBlocks.DEPTHS_CORAL_SLAB);
		ALESTRIAN_CORAL_SLAB = block(VerseModBlocks.ALESTRIAN_CORAL_SLAB);
		DROWNED_CORAL_SLAB = block(VerseModBlocks.DROWNED_CORAL_SLAB);
		WHEAT_CORAL_SLAB = block(VerseModBlocks.WHEAT_CORAL_SLAB);
		MUSHROOM_CORAL_SLAB = block(VerseModBlocks.MUSHROOM_CORAL_SLAB);
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