package net.mcreator.verse.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mcreator.verse.VerseMod;
import net.neoforged.fml.ModList;

import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class EndlagDataLoader {

    // Cache for item-specific endlag values
    private static final Map<ResourceLocation, Integer> itemEndlagCache = new HashMap<>();

    // Cache for tag-based endlag values
    private static final Map<ResourceLocation, Integer> tagEndlagCache = new HashMap<>();

    private static boolean loaded = false;

    /**
     * Gets the endlag (buffer) value for a given item stack.
     * Returns 5 if no data file exists for the item.
     */
    public static int getEndlag(ItemStack stack) {
        if (stack.isEmpty()) {
            VerseMod.LOGGER.debug("[ENDLAG DEBUG] Stack is empty, returning default 5");
            return 5;
        }

        // Load data if not yet loaded
        if (!loaded) {
            VerseMod.LOGGER.info("[ENDLAG DEBUG] Data not loaded yet, loading now...");
            loadEndlagData();
        }

        // Get the item's registry location
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        VerseMod.LOGGER.info("[ENDLAG DEBUG] Checking endlag for item: {}", itemId);

        // First check if there's a direct item file
        if (itemEndlagCache.containsKey(itemId)) {
            int endlag = itemEndlagCache.get(itemId);
            VerseMod.LOGGER.info("[ENDLAG DEBUG] Found direct item match! {} = {}", itemId, endlag);
            return endlag;
        } else {
            VerseMod.LOGGER.info("[ENDLAG DEBUG] No direct item match for {}", itemId);
        }

        // Then check if the item matches any tags
        VerseMod.LOGGER.info("[ENDLAG DEBUG] Checking {} tags...", tagEndlagCache.size());
        for (Map.Entry<ResourceLocation, Integer> tagEntry : tagEndlagCache.entrySet()) {
            TagKey<Item> tagKey = TagKey.create(BuiltInRegistries.ITEM.key(), tagEntry.getKey());

            if (stack.is(tagKey)) {
                int endlag = tagEntry.getValue();

                return endlag;
            }
        }

        // Default to 5 if no data found

        return 5;
    }

    /**
     * Loads all endlag data from the weapondata folder.
     * Searches for JSON files in data/*/
    
    public static void loadEndlagData() {

        itemEndlagCache.clear();
        tagEndlagCache.clear();

        try {
            // Iterate through all loaded mods to access their data folders
            ModList.get().forEachModFile(modFile -> {
                String modId = modFile.getModInfos().get(0).getModId();


                // Find the data root path for this mod
                Path rootPath = modFile.findResource("data");
                if (rootPath == null || !Files.exists(rootPath)) {

                    return;
                }



                try {
                    // Walk through all subdirectories in the data folder
                    try (Stream<Path> namespaces = Files.list(rootPath)) {
                        namespaces.filter(Files::isDirectory).forEach(namespacePath -> {
                            String namespace = namespacePath.getFileName().toString();


                            Path weaponDataPath = namespacePath.resolve("weapondata");
                            if (!Files.exists(weaponDataPath) || !Files.isDirectory(weaponDataPath)) {

                                return;
                            }



                            try (Stream<Path> files = Files.list(weaponDataPath)) {
                                files.filter(Files::isRegularFile)
                                        .filter(path -> path.toString().endsWith(".json"))
                                        .forEach(jsonFile -> {


                                            try {
                                                // Read and parse the JSON
                                                String content = Files.readString(jsonFile);
                                                JsonObject json = JsonParser.parseString(content).getAsJsonObject();



                                                if (!json.has("endlag")) {
                                                    VerseMod.LOGGER.warn("[ENDLAG DEBUG] File {} missing 'endlag' field, skipping", jsonFile);
                                                    return;
                                                }

                                                int endlag = json.get("endlag").getAsInt();


                                                // Extract filename without extension
                                                String filename = jsonFile.getFileName().toString();
                                                filename = filename.substring(0, filename.lastIndexOf('.'));


                                                // Check if this is a tag file (starts with #)
                                                if (filename.startsWith("#")) {
                                                    String tagString = filename.substring(1);


                                                    ResourceLocation tagLocation;
                                                    if (tagString.contains(":")) {
                                                        tagLocation = ResourceLocation.parse(tagString);
                                                    } else {
                                                        tagLocation = ResourceLocation.fromNamespaceAndPath(namespace, tagString);
                                                    }

                                                    tagEndlagCache.put(tagLocation, endlag);

                                                } else {


                                                    ResourceLocation itemLocation;
                                                    if (filename.contains(":")) {
                                                        itemLocation = ResourceLocation.parse(filename);
                                                    } else {
                                                        itemLocation = ResourceLocation.fromNamespaceAndPath(namespace, filename);
                                                    }

                                                    itemEndlagCache.put(itemLocation, endlag);

                                                }

                                            } catch (Exception e) {
                                                VerseMod.LOGGER.error("[ENDLAG DEBUG] Failed to parse file: {}", jsonFile, e);
                                            }
                                        });
                            } catch (Exception e) {
                                VerseMod.LOGGER.error("[ENDLAG DEBUG] Failed to list files in weapondata: {}", weaponDataPath, e);
                            }
                        });
                    }
                } catch (Exception e) {
                    VerseMod.LOGGER.error("[ENDLAG DEBUG] Failed to process data folder for mod: {}", modId, e);
                }
            });

            loaded = true;


        } catch (Exception e) {

            loaded = true;
        }
    }

    /**
     * Reloads all endlag data. Call this when resources are reloaded.
     */
    public static void reload() {
        loaded = false;
        loadEndlagData();
    }

    /**
     * Clears all cached data.
     */
    public static void clear() {
        itemEndlagCache.clear();
        tagEndlagCache.clear();
        loaded = false;
    }
}