/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.verse.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.core.registries.Registries;

import net.mcreator.verse.world.features.*;
import net.mcreator.verse.VerseMod;

public class VerseModFeatures {
	public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, VerseMod.MODID);
	public static final DeferredHolder<Feature<?>, Feature<?>> CORAL_MUSHROOM_FEATURE = REGISTRY.register("coral_mushroom_feature", CoralMushroomFeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> ALESTRIAN_CORAL_FEATURE = REGISTRY.register("alestrian_coral_feature", AlestrianCoralFeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> ALTERNATE_CORAL_FEATURE = REGISTRY.register("alternate_coral_feature", AlternateCoralFeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> CORAL_TREE_FEATURE = REGISTRY.register("coral_tree_feature", CoralTreeFeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> MED_CORAL_TREE_FEATURE = REGISTRY.register("med_coral_tree_feature", MedCoralTreeFeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> L_CORAL_TREE_FEATUREARG = REGISTRY.register("l_coral_tree_featurearg", LCoralTreeFeatureargFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGCORALTREEFEATURE = REGISTRY.register("bigcoraltreefeature", BigcoraltreefeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGLIGHTCORALTREEFEATURE = REGISTRY.register("biglightcoraltreefeature", BiglightcoraltreefeatureFeature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGLIGHTCORALTREEFEATURE_2 = REGISTRY.register("biglightcoraltreefeature_2", Biglightcoraltreefeature2Feature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGCORALTREEFEATURE_2 = REGISTRY.register("bigcoraltreefeature_2", Bigcoraltreefeature2Feature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGLIGHTCORALTREEFEATURE_3 = REGISTRY.register("biglightcoraltreefeature_3", Biglightcoraltreefeature3Feature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGCORALTREEFEATURE_3 = REGISTRY.register("bigcoraltreefeature_3", Bigcoraltreefeature3Feature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGLIGHTCORALTREEFEATURE_4 = REGISTRY.register("biglightcoraltreefeature_4", Biglightcoraltreefeature4Feature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> BIGLIGHTCORALTREEFEATURE_5 = REGISTRY.register("biglightcoraltreefeature_5", Biglightcoraltreefeature5Feature::new);
	public static final DeferredHolder<Feature<?>, Feature<?>> CORALBULBFEATURE = REGISTRY.register("coralbulbfeature", CoralbulbfeatureFeature::new);
}