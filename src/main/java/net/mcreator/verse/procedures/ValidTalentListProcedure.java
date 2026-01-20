package net.mcreator.verse.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.verse.network.VerseModVariables;
import net.mcreator.verse.VerseMod;

import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

import java.io.InputStream;

import com.ibm.icu.util.Output;

public class ValidTalentListProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		com.google.gson.JsonObject mainJOBJ = new com.google.gson.JsonObject();
		boolean requiredisfine = false;
		boolean mutualisfine = false;
		String required = "";
		String name = "";
		String reserve = "";
		String primary = "";
		String mutual = "";
		String requiredrepeater = "";
		String mutualrepeater = "";
		String finaltext = "";
		String validrare = "";
		String choice = "";
		double tesdt = 0;
		double mind = 0;
		double gale = 0;
		double strength = 0;
		double shadow = 0;
		double thunder = 0;
		double blood = 0;
		double life = 0;
		double frost = 0;
		double iron = 0;
		double power = 0;
		double agility = 0;
		double flame = 0;
		double fortitude = 0;
		double count = 0;
		double rare = 0;
		double checkrequired = 0;
		double checkmutual = 0;
		double handcount = 0;
		double randomchoice = 0;
		double rarecount = 0;
		{
			VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
			_vars.validdraw = "";
			_vars.markSyncDirty();
		}
		required = "";
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
						resource.listResources(PackType.SERVER_DATA, namespace, "talent", output);
					}
				});
				for (com.google.gson.JsonObject jsoniterator : jsons) {
					tesdt = tesdt + 1;
					mainJOBJ = jsoniterator;
					name = mainJOBJ.get("name").getAsString();
					if (entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.contains("(" + name + ")") && mainJOBJ.get("rarity").getAsDouble() == 1) {
						rarecount = rarecount + 1;
					}
					if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.contains("(" + name + ")") && !entity.getData(VerseModVariables.PLAYER_VARIABLES).burn.contains("(" + name + ")")) {
						if (!entity.getData(VerseModVariables.PLAYER_VARIABLES).freeze.contains("(" + name + ")")) {
							power = mainJOBJ.get("power").getAsDouble();
							if (power <= entity.getData(VerseModVariables.PLAYER_VARIABLES).power) {
								strength = mainJOBJ.get("strength").getAsDouble();
								fortitude = mainJOBJ.get("fortitude").getAsDouble();
								mind = mainJOBJ.get("mind").getAsDouble();
								agility = mainJOBJ.get("agility").getAsDouble();
								flame = mainJOBJ.get("flamecharm").getAsDouble();
								gale = mainJOBJ.get("galebreathe").getAsDouble();
								thunder = mainJOBJ.get("thundercall").getAsDouble();
								frost = mainJOBJ.get("frostdraw").getAsDouble();
								shadow = mainJOBJ.get("shadowcast").getAsDouble();
								blood = mainJOBJ.get("bloodrend").getAsDouble();
								iron = mainJOBJ.get("ironsing").getAsDouble();
								life = mainJOBJ.get("lifeweave").getAsDouble();
								required = mainJOBJ.get("required").getAsString();
								mutual = mainJOBJ.get("mutual").getAsString();
								if (required.contains(")") && required.contains("(")) {
									VerseMod.LOGGER.info(name + " " + "reqs have ()");
									requiredrepeater = required;
									for (int index0 = 0; index0 < (int) ReturnCountOfTheseTalentsProcedure.execute(entity, required); index0++) {
										if (entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.contains(requiredrepeater.substring((int) requiredrepeater.indexOf("("), (int) requiredrepeater.indexOf(")") + ")".length()))) {
											checkrequired = checkrequired + 1;
										}
										requiredrepeater = requiredrepeater.replace(requiredrepeater.substring((int) requiredrepeater.indexOf("("), (int) requiredrepeater.indexOf(")") + ")".length()), "");
									}
									if (ReturnCountOfTheseTalentsProcedure.execute(entity, required) == checkrequired) {
										requiredisfine = true;
									} else {
										requiredisfine = false;
									}
								} else {
									requiredisfine = true;
								}
								if (mutual.contains(")") && mutual.contains("(")) {
									VerseMod.LOGGER.info(name + " " + "mutual have ()");
									mutualrepeater = mutual;
									for (int index1 = 0; index1 < (int) ReturnCountOfTheseTalentsProcedure.execute(entity, mutual); index1++) {
										if (entity.getData(VerseModVariables.PLAYER_VARIABLES).talentlist.contains(mutualrepeater.substring((int) mutualrepeater.indexOf("("), (int) mutualrepeater.indexOf(")") + ")".length()))) {
											checkrequired = checkrequired + 1;
										}
										mutualrepeater = mutualrepeater.replace(mutualrepeater.substring((int) mutualrepeater.indexOf("("), (int) mutualrepeater.indexOf(")") + ")".length()), "");
									}
									if (ReturnCountOfTheseTalentsProcedure.execute(entity, mutual) == checkmutual) {
										mutualisfine = false;
									} else {
										mutualisfine = true;
									}
								} else {
									mutualisfine = true;
								}
								if (true == requiredisfine && true == mutualisfine) {
									if (strength <= entity.getData(VerseModVariables.PLAYER_VARIABLES).str && fortitude <= entity.getData(VerseModVariables.PLAYER_VARIABLES).fort && mind <= entity.getData(VerseModVariables.PLAYER_VARIABLES).intel
											&& agility <= entity.getData(VerseModVariables.PLAYER_VARIABLES).agility && flame <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Flamecharm
											&& gale <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Galebreathe && thunder <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Thundercall
											&& frost <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Frostdraw && shadow <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Shadowcast
											&& blood <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Bloodrend && iron <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Ironsing
											&& life <= entity.getData(VerseModVariables.PLAYER_VARIABLES).Lifeweave) {
										if (mainJOBJ.get("rarity").getAsDouble() == 0) {
											if (!(strength + fortitude + mind + agility + flame + gale + thunder + frost + shadow + blood + iron + life == 0)) {
												primary = primary + "" + ("(" + name + ")");
											} else {
												reserve = reserve + "" + ("(" + name + ")");
											}
										} else {
											validrare = validrare + "" + ("(" + name + ")");
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (entity.getData(VerseModVariables.PLAYER_VARIABLES).power % 2 == 0) {
			handcount = 2;
			count = 5;
		} else {
			handcount = 2;
			count = 4;
		}
		if (!((ReturnCountOfTheseTalentsProcedure.execute(entity, primary) + ReturnCountOfTheseTalentsProcedure.execute(entity, reserve)) / count > handcount + entity.getData(VerseModVariables.PLAYER_VARIABLES).fold)) {
			handcount = 2;
			count = Math.floor((ReturnCountOfTheseTalentsProcedure.execute(entity, primary) + ReturnCountOfTheseTalentsProcedure.execute(entity, reserve)) / 2);
		}
		for (int index2 = 0; index2 < (int) handcount; index2++) {
			for (int index3 = 0; index3 < (int) count; index3++) {
				choice = "";
				if (count > ReturnCountOfTheseTalentsProcedure.execute(entity, finaltext)) {
					if (primary.contains("(")) {
						randomchoice = Mth.nextInt(RandomSource.create(), 0, (int) (ReturnCountOfTheseTalentsProcedure.execute(entity, primary) - 1));
						choice = primary;
						if (randomchoice > 0) {
							for (int index4 = 0; index4 < (int) randomchoice; index4++) {
								choice = choice.replace(choice.substring((int) choice.indexOf("("), (int) choice.indexOf(")") + ")".length()), "");
							}
						}
						choice = choice.substring((int) choice.indexOf("("), (int) choice.indexOf(")") + ")".length());
						primary = primary.replace(choice, "");
					} else {
						randomchoice = Mth.nextInt(RandomSource.create(), 0, (int) (ReturnCountOfTheseTalentsProcedure.execute(entity, reserve) - 1));
						choice = reserve;
						if (randomchoice > 0) {
							for (int index5 = 0; index5 < (int) randomchoice; index5++) {
								choice = choice.replace(choice.substring((int) choice.indexOf("("), (int) choice.indexOf(")") + ")".length()), "");
							}
						}
						choice = choice.substring((int) choice.indexOf("("), (int) choice.indexOf(")") + ")".length());
						VerseMod.LOGGER.info("choice" + choice);
						reserve = reserve.replace(choice, "");
					}
					finaltext = finaltext + "" + choice;
				}
				if (20 > rarecount && 4 == ReturnCountOfTheseTalentsProcedure.execute(entity, finaltext) && validrare.contains("(")) {
					finaltext = finaltext + "" + validrare.substring((int) validrare.indexOf("("), (int) validrare.indexOf(")") + ")".length());
					validrare = validrare.replace(validrare.substring((int) validrare.indexOf("("), (int) validrare.indexOf(")") + ")".length()), "");
				}
			}
			{
				VerseModVariables.PlayerVariables _vars = entity.getData(VerseModVariables.PLAYER_VARIABLES);
				_vars.validdraw = entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw + "{" + finaltext + "}";
				_vars.markSyncDirty();
			}
			VerseMod.LOGGER.info("hand: " + entity.getData(VerseModVariables.PLAYER_VARIABLES).validdraw);
			finaltext = "";
		}
	}
}