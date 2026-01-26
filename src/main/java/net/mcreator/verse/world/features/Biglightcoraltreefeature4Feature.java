package net.mcreator.verse.world.features;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;

import net.mcreator.verse.world.features.configurations.StructureFeatureConfiguration;
import net.mcreator.verse.procedures.ReturnvalidfeatureplacementProcedure;

public class Biglightcoraltreefeature4Feature extends StructureFeature {
	public Biglightcoraltreefeature4Feature() {
		super(StructureFeatureConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<StructureFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		int x = context.origin().getX();
		int y = context.origin().getY();
		int z = context.origin().getZ();
		if (!ReturnvalidfeatureplacementProcedure.execute(world))
			return false;
		return super.place(context);
	}
}