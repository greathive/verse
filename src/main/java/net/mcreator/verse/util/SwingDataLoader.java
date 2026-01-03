package net.mcreator.verse.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.MaceItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.Minecraft;

import java.io.InputStreamReader;
import java.util.Optional;

public class SwingDataLoader {

    private static final Gson GSON = new Gson();

    public static class SwingData {
        public int swingCount;
        public String animType;

        public SwingData(int swingCount, String animType) {
            this.swingCount = swingCount;
            this.animType = animType;
        }
    }

    public static SwingData getSwingData(ItemStack item) {
        // Check if item is a valid weapon/tool
        if (!isValidWeapon(item)) {
            return null;
        }

        // Get item registry name
        ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item.getItem());
        String itemName = itemId.getPath(); // e.g., "diamond_sword" or "trident"

        // Try to load custom swing data file
        ResourceLocation swingDataLocation = ResourceLocation.fromNamespaceAndPath("verse", "swing/" + itemName + ".json");
        SwingData customData = loadSwingDataFromFile(swingDataLocation);

        if (customData != null) {
            // Has custom file, use it regardless of tool type
            return customData;
        }

        // No custom file - check if this is an excluded tool type
        if (item.getItem() instanceof PickaxeItem ||
                item.getItem() instanceof ShovelItem ||
                item.getItem() instanceof HoeItem) {
            // These tools are excluded from the swing system if they don't have custom files
            return null;
        }

        // Fall back to generic files for other tools
        if (item.getItem() instanceof SwordItem) {
            // Use sword_generic.json
            ResourceLocation swordGeneric = ResourceLocation.fromNamespaceAndPath("verse", "swing/sword_generic.json");
            SwingData swordData = loadSwingDataFromFile(swordGeneric);
            return swordData != null ? swordData : new SwingData(2, "sword");
        } else {
            // Use generic_else.json (for axes, tridents, maces, etc.)
            ResourceLocation genericElse = ResourceLocation.fromNamespaceAndPath("verse", "swing/generic_else.json");
            SwingData genericData = loadSwingDataFromFile(genericElse);
            return genericData != null ? genericData : new SwingData(2, "axe");
        }
    }

    private static boolean isValidWeapon(ItemStack item) {
        // Include TieredItem (swords, axes, pickaxes, shovels, hoes)
        if (item.getItem() instanceof TieredItem) {
            return true;
        }

        // Also include Tridents and Maces which aren't TieredItems
        if (item.getItem() instanceof TridentItem) {
            return true;
        }

        if (item.getItem() instanceof MaceItem) {
            return true;
        }

        return false;
    }

    private static SwingData loadSwingDataFromFile(ResourceLocation location) {
        try {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            Optional<Resource> resource = resourceManager.getResource(location);

            if (resource.isPresent()) {
                try (InputStreamReader reader = new InputStreamReader(resource.get().open())) {
                    JsonObject json = GSON.fromJson(reader, JsonObject.class);

                    int swingCount = json.has("swingcount") ? Integer.parseInt(json.get("swingcount").getAsString()) : 2;
                    String animType = json.has("anim") ? json.get("anim").getAsString() : "sword";

                    return new SwingData(swingCount, animType);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load swing data from " + location + ": " + e.getMessage());
        }

        return null;
    }
}