package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

import java.io.InputStream;

import com.ibm.icu.util.Output;

public class GetTextureForTrailProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		com.google.gson.JsonObject mainJsonObj = new com.google.gson.JsonObject();
		String path = "";
		double frameCount = 0;
		double FrameHeight = 0;
		double FrameWidth = 0;
		{
			if (world instanceof ServerLevel srvlvl_) {
				class Output implements PackResources.ResourceOutput {
					private List<com.google.gson.JsonObject> jsonObjects;
					private PackResources packResources;

					public Output(List<com.google.gson.JsonObject> jsonObjects) {
						this.jsonObjects = jsonObjects;
					}

					public void setPackResources(PackResources packResources) {
						this.packResources = packResources;
					}

					@Override
					public void accept(ResourceLocation resourceLocation, IoSupplier<InputStream> ioSupplier) {
						try {
							com.google.gson.JsonObject jsonObject = new com.google.gson.Gson()
									.fromJson(new java.io.BufferedReader(new java.io.InputStreamReader(ioSupplier.get(), java.nio.charset.StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")), com.google.gson.JsonObject.class);
							this.jsonObjects.add(jsonObject);
						} catch (Exception e) {
						}
					}
				}
				List<com.google.gson.JsonObject> jsons = new ArrayList<>();
				Output output = new Output(jsons);
				ResourceManager rm = srvlvl_.getServer().getResourceManager();
				rm.listPacks().forEach(resource -> {
					for (String namespace : resource.getNamespaces(PackType.SERVER_DATA)) {
						output.setPackResources(resource);
						resource.listResources(PackType.SERVER_DATA, namespace, "trail", output);
					}
				});
				for (com.google.gson.JsonObject jsoniterator : jsons) {
					mainJsonObj = jsoniterator;
					if (!(mainJsonObj.size() == 0)) {
						if (itemstack.is(ItemTags.create(ResourceLocation.parse((mainJsonObj.get("id").getAsString()).toLowerCase(java.util.Locale.ENGLISH))))
								|| (BuiltInRegistries.ITEM.getKey(itemstack.getItem()).toString()).equals(mainJsonObj.get("id").getAsString())) {
							path = mainJsonObj.get("path").getAsString();
							frameCount = mainJsonObj.get("frameCount").getAsDouble();
							FrameHeight = mainJsonObj.get("frameHeight").getAsDouble();
							FrameWidth = mainJsonObj.get("frameWidth").getAsDouble();
						}
					}
				}
			}
		}
	}
}