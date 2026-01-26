package net.mcreator.verse.world.features;

import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;

import net.mcreator.verse.procedures.ReturnvalidfeatureplacementProcedure;

public class CoralbulbfeatureFeature extends RandomSelectorFeature {
	public CoralbulbfeatureFeature() {
		super(RandomFeatureConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<RandomFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		int x = context.origin().getX();
		int y = context.origin().getY();
		int z = context.origin().getZ();
		if (!ReturnvalidfeatureplacementProcedure.execute(world))
			return false;
		return super.place(context);
	}
}