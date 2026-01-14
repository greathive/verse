/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.verse.block.OrchestratorBasinBlock;
import net.mcreator.verse.VerseMod;

public class VerseModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(VerseMod.MODID);
	public static final DeferredBlock<Block> ORCHESTRATOR_BASIN;
	static {
		ORCHESTRATOR_BASIN = REGISTRY.register("orchestrator_basin", OrchestratorBasinBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}