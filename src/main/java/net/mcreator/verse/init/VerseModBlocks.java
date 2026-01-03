/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.verse.block.OutfitTableBlock;
import net.mcreator.verse.VerseMod;

public class VerseModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(VerseMod.MODID);
	public static final DeferredBlock<Block> OUTFIT_TABLE;
	static {
		OUTFIT_TABLE = REGISTRY.register("outfit_table", OutfitTableBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}