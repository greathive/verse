/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.verse.block.*;
import net.mcreator.verse.VerseMod;

public class VerseModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(VerseMod.MODID);
	public static final DeferredBlock<Block> ORCHESTRATOR_BASIN;
	public static final DeferredBlock<Block> TEARSTONE;
	public static final DeferredBlock<Block> TEARSTONE_STAIR;
	public static final DeferredBlock<Block> TEARSTONE_SLAB;
	public static final DeferredBlock<Block> TEARSTONE_WALL;
	public static final DeferredBlock<Block> COBBLED_TEARSTONE;
	public static final DeferredBlock<Block> COBBLED_TEARSTONE_STAIR;
	public static final DeferredBlock<Block> COBBLED_TEARSTONE_SLAB;
	public static final DeferredBlock<Block> COBBLED_TEARSTONE_WALL;
	public static final DeferredBlock<Block> DEEPSAND;
	public static final DeferredBlock<Block> SOAKSTONE;
	public static final DeferredBlock<Block> SOAKSTONE_STAIR;
	public static final DeferredBlock<Block> SOAKSTONE_SLAB;
	public static final DeferredBlock<Block> SOAKSTONE_WALL;
	public static final DeferredBlock<Block> FLOODSTONE;
	public static final DeferredBlock<Block> BARRIER;
	public static final DeferredBlock<Block> DEEPLIGHT;
	public static final DeferredBlock<Block> DEPTHS_CORAL;
	public static final DeferredBlock<Block> DEEPLIGHTSPIKE;
	public static final DeferredBlock<Block> ALESTRIAN_CORAL;
	public static final DeferredBlock<Block> DROWNED_CORAL;
	public static final DeferredBlock<Block> WHEAT_CORAL;
	public static final DeferredBlock<Block> MUSHROOM_CORAL;
	public static final DeferredBlock<Block> DEPTHS_CORAL_SLAB;
	public static final DeferredBlock<Block> ALESTRIAN_CORAL_SLAB;
	public static final DeferredBlock<Block> DROWNED_CORAL_SLAB;
	public static final DeferredBlock<Block> WHEAT_CORAL_SLAB;
	public static final DeferredBlock<Block> MUSHROOM_CORAL_SLAB;
	public static final DeferredBlock<Block> DEEPLIGHT_BULB;
	public static final DeferredBlock<Block> TUBE_CORAL;
	public static final DeferredBlock<Block> TEARSTONE_BRICK;
	public static final DeferredBlock<Block> TEARSTONE_BRICK_SLAB;
	public static final DeferredBlock<Block> TEARSTONE_BRICK_STAIR;
	public static final DeferredBlock<Block> TEARSTONE_BRICK_WALL;
	public static final DeferredBlock<Block> SMALL_TEARSTONE_BRICK;
	public static final DeferredBlock<Block> SMALL_TEARSTONE_BRICK_SLAB;
	public static final DeferredBlock<Block> SMALL_TEARSTONE_BRICK_STAIR;
	public static final DeferredBlock<Block> SMALL_TEARSTONE_BRICK_WALL;
	static {
		ORCHESTRATOR_BASIN = REGISTRY.register("orchestrator_basin", OrchestratorBasinBlock::new);
		TEARSTONE = REGISTRY.register("tearstone", TearstoneBlock::new);
		TEARSTONE_STAIR = REGISTRY.register("tearstone_stair", TearstoneStairBlock::new);
		TEARSTONE_SLAB = REGISTRY.register("tearstone_slab", TearstoneSlabBlock::new);
		TEARSTONE_WALL = REGISTRY.register("tearstone_wall", TearstoneWallBlock::new);
		COBBLED_TEARSTONE = REGISTRY.register("cobbled_tearstone", CobbledTearstoneBlock::new);
		COBBLED_TEARSTONE_STAIR = REGISTRY.register("cobbled_tearstone_stair", CobbledTearstoneStairBlock::new);
		COBBLED_TEARSTONE_SLAB = REGISTRY.register("cobbled_tearstone_slab", CobbledTearstoneSlabBlock::new);
		COBBLED_TEARSTONE_WALL = REGISTRY.register("cobbled_tearstone_wall", CobbledTearstoneWallBlock::new);
		DEEPSAND = REGISTRY.register("deepsand", DeepsandBlock::new);
		SOAKSTONE = REGISTRY.register("soakstone", SoakstoneBlock::new);
		SOAKSTONE_STAIR = REGISTRY.register("soakstone_stair", SoakstoneStairBlock::new);
		SOAKSTONE_SLAB = REGISTRY.register("soakstone_slab", SoakstoneSlabBlock::new);
		SOAKSTONE_WALL = REGISTRY.register("soakstone_wall", SoakstoneWallBlock::new);
		FLOODSTONE = REGISTRY.register("floodstone", FloodstoneBlock::new);
		BARRIER = REGISTRY.register("barrier", BarrierBlock::new);
		DEEPLIGHT = REGISTRY.register("deeplight", DeeplightBlock::new);
		DEPTHS_CORAL = REGISTRY.register("depths_coral", DepthsCoralBlock::new);
		DEEPLIGHTSPIKE = REGISTRY.register("deeplightspike", DeeplightspikeBlock::new);
		ALESTRIAN_CORAL = REGISTRY.register("alestrian_coral", AlestrianCoralBlock::new);
		DROWNED_CORAL = REGISTRY.register("drowned_coral", DrownedCoralBlock::new);
		WHEAT_CORAL = REGISTRY.register("wheat_coral", WheatCoralBlock::new);
		MUSHROOM_CORAL = REGISTRY.register("mushroom_coral", MushroomCoralBlock::new);
		DEPTHS_CORAL_SLAB = REGISTRY.register("depths_coral_slab", DepthsCoralSlabBlock::new);
		ALESTRIAN_CORAL_SLAB = REGISTRY.register("alestrian_coral_slab", AlestrianCoralSlabBlock::new);
		DROWNED_CORAL_SLAB = REGISTRY.register("drowned_coral_slab", DrownedCoralSlabBlock::new);
		WHEAT_CORAL_SLAB = REGISTRY.register("wheat_coral_slab", WheatCoralSlabBlock::new);
		MUSHROOM_CORAL_SLAB = REGISTRY.register("mushroom_coral_slab", MushroomCoralSlabBlock::new);
		DEEPLIGHT_BULB = REGISTRY.register("deeplight_bulb", DeeplightBulbBlock::new);
		TUBE_CORAL = REGISTRY.register("tube_coral", TubeCoralBlock::new);
		TEARSTONE_BRICK = REGISTRY.register("tearstone_brick", TearstoneBrickBlock::new);
		TEARSTONE_BRICK_SLAB = REGISTRY.register("tearstone_brick_slab", TearstoneBrickSlabBlock::new);
		TEARSTONE_BRICK_STAIR = REGISTRY.register("tearstone_brick_stair", TearstoneBrickStairBlock::new);
		TEARSTONE_BRICK_WALL = REGISTRY.register("tearstone_brick_wall", TearstoneBrickWallBlock::new);
		SMALL_TEARSTONE_BRICK = REGISTRY.register("small_tearstone_brick", SmallTearstoneBrickBlock::new);
		SMALL_TEARSTONE_BRICK_SLAB = REGISTRY.register("small_tearstone_brick_slab", SmallTearstoneBrickSlabBlock::new);
		SMALL_TEARSTONE_BRICK_STAIR = REGISTRY.register("small_tearstone_brick_stair", SmallTearstoneBrickStairBlock::new);
		SMALL_TEARSTONE_BRICK_WALL = REGISTRY.register("small_tearstone_brick_wall", SmallTearstoneBrickWallBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}