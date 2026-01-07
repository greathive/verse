package net.mcreator.verse.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mcreator.verse.VerseMod;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

            return 5;
        }
        
        // Load data if not yet loaded
        if (!loaded) {

            loadEndlagData();
        }
        
        // Get the item's registry location
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
      
        
        // First check if there's a direct item file
        if (itemEndlagCache.containsKey(itemId)) {
            int endlag = itemEndlagCache.get(itemId);

            return endlag;
        } else {
     
        }
        
        // Then check if the item matches any tags

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
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
       
            
            // Get all namespaces
            for (String namespace : resourceManager.getNamespaces()) {
                
                try {
                    // List all resources in the weapondata folder for this namespace
                    Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                        "weapondata",
                        location -> location.getPath().endsWith(".json")
                    );
                    

                    
                    for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                        ResourceLocation location = entry.getKey();

                        
                        try {
                            // Read the JSON file
                            JsonObject json = JsonParser.parseReader(
                                new InputStreamReader(entry.getValue().open())
                            ).getAsJsonObject();
                            
                          
                            
                            // Get the endlag value
                            if (!json.has("endlag")) {

                                continue; // Skip files without endlag
                            }
                            
                            int endlag = json.get("endlag").getAsInt();
                           
                            
                            // Extract filename without extension
                            String path = location.getPath();
                            String filename = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));

                            
                            // Check if this is a tag file (starts with #)
                            if (filename.startsWith("#")) {
                                // This is a tag - parse it as namespace:path
                                String tagString = filename.substring(1); // Remove the #
                               
                                ResourceLocation tagLocation;
                                
                                if (tagString.contains(":")) {
                                    // Full tag with namespace (e.g., #minecraft:axes)
                                    tagLocation = ResourceLocation.parse(tagString);
                                } else {
                                    // Tag without namespace (e.g., #axes) - use the namespace from the file location
                                    tagLocation = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), tagString);
                                }
                                
                                tagEndlagCache.put(tagLocation, endlag);

                            } else {
                                // This is an item file - parse it as namespace:item
                              
                                ResourceLocation itemLocation;
                                
                                if (filename.contains(":")) {
                                    // Full item ID with namespace
                                    itemLocation = ResourceLocation.parse(filename);
                                } else {
                                    // Item without namespace - use the namespace from the file location
                                    itemLocation = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), filename);
                                }
                                
                                itemEndlagCache.put(itemLocation, endlag);

                            }
                            
                        } catch (Exception e) {
                           
                        }
                    }
                } catch (Exception e) {

                }
            }
            
            loaded = true;
            
            
        } catch (Exception e) {

            loaded = true; // Mark as loaded even on failure to prevent repeated attempts
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