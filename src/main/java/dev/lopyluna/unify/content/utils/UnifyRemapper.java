package dev.lopyluna.unify.content.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lopyluna.unify.Unify;
import dev.lopyluna.unify.content.AllUtils;
import dev.lopyluna.unify.helper.MaterialEntry;
import dev.lopyluna.unify.helper.MetalEntry;
import dev.lopyluna.unify.helper.MetalType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.lopyluna.unify.helper.MetalType.DUST;
import static dev.lopyluna.unify.helper.MetalType.GEAR;
import static dev.lopyluna.unify.helper.MetalType.INGOT;
import static dev.lopyluna.unify.helper.MetalType.NA;
import static dev.lopyluna.unify.helper.MetalType.NUGGET;
import static dev.lopyluna.unify.helper.MetalType.ORE_DEEPSLATE;
import static dev.lopyluna.unify.helper.MetalType.ORE_STONE;
import static dev.lopyluna.unify.helper.MetalType.RAW;
import static dev.lopyluna.unify.helper.MetalType.RAW_CRUSHED;
import static dev.lopyluna.unify.helper.MetalType.RAW_STORAGE;
import static dev.lopyluna.unify.helper.MetalType.ROD;
import static dev.lopyluna.unify.helper.MetalType.SHEET;
import static dev.lopyluna.unify.helper.MetalType.STORAGE;
import static dev.lopyluna.unify.helper.MetalType.WIRE;
import static dev.lopyluna.unify.register.AllTags.blockC;
import static dev.lopyluna.unify.register.AllTags.itemC;

@SuppressWarnings("unused")
public class UnifyRemapper {
    public static final List<Item> ITEM_DISPLAY = new ArrayList<>();
    public static final List<Item> ITEM_HIDDEN = new ArrayList<>();
    private static final Map<ResourceLocation, ResourceLocation> ITEMID_REMAP = new HashMap<>();
    private static final Map<Item, Item> ITEM_REMAP = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> BLOCKID_REMAP = new HashMap<>();
    private static final Map<Block, Block> BLOCK_REMAP = new HashMap<>();
    private static final Map<String, String> ID_REMAP = new HashMap<>();

    public static void register() {
        buildBlockRemap();
        buildItemRemap();
    }

    public static void register(HolderLookup.Provider provider) {
        register();
        buildIDRemap(provider);
    }

    public static void buildBlockRemap() {
        for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
            var block = entry.getValue();
            var blockID = BuiltInRegistries.BLOCK.getKey(block);
            if (blockID.getNamespace().equals(Unify.MOD_ID)) continue;
            var remap = remapBlocks(block);
            var remapID = BuiltInRegistries.BLOCK.getKey(remap);
            if (!blockID.equals(remapID)) {
                if (!BLOCKID_REMAP.containsKey(blockID)) BLOCKID_REMAP.put(blockID, remapID);
                if (!BLOCK_REMAP.containsKey(block)) BLOCK_REMAP.put(block, remap);
                var itemAlt = block.asItem();
                var remapAlt = remap.asItem();
                if (!ITEM_DISPLAY.contains(remapAlt)) ITEM_DISPLAY.add(remapAlt);
                if (!ITEM_HIDDEN.contains(itemAlt)) ITEM_HIDDEN.add(itemAlt);
            }
        }
    }

    public static void buildItemRemap() {
        for (var entry : BuiltInRegistries.ITEM.entrySet()) {
            var item = entry.getValue();
            var itemID = BuiltInRegistries.ITEM.getKey(item);
            if (itemID.getNamespace().equals(Unify.MOD_ID)) continue;
            var remap = remapItems(item);
            var remapID = BuiltInRegistries.ITEM.getKey(remap);
            if (!itemID.equals(remapID)) {
                if (!ID_REMAP.containsKey(itemID.toString())) ID_REMAP.put(itemID.toString(), remapID.toString());
                if (!ITEMID_REMAP.containsKey(itemID)) ITEMID_REMAP.put(itemID, remapID);
                if (!ITEM_REMAP.containsKey(item)) ITEM_REMAP.put(item, remap);
                if (!ITEM_DISPLAY.contains(remap)) ITEM_DISPLAY.add(remap);
                if (!ITEM_HIDDEN.contains(item)) ITEM_HIDDEN.add(item);
            }
        }
    }

    public static void buildIDRemap(HolderLookup.Provider provider) {
        var itemLookup = provider.lookupOrThrow(Registries.ITEM);
        for (var tag : itemLookup.listTagIds().toList()) {
            var loc = tag.location().toString();
            var remap = remapLocIDs(loc);
            if (!loc.equals(remap) && !ID_REMAP.containsKey(loc)) ID_REMAP.put(loc, remap);
        }
    }

    public static Item remapItem(Item item) {
        var newItem = ITEM_REMAP.get(item);
        return newItem != null ? newItem : item;
    }

    public static ItemStack remapItemStack(ItemStack stack) {
        var item = ITEM_REMAP.get(stack.getItem());
        return item != null ? stack.transmuteCopy(item) : stack;
    }

    public static Block remapBlock(Block block) {
        var newBlock = BLOCK_REMAP.get(block);
        return newBlock != null ? newBlock : block;
    }

    public static BlockState remapBlockState(BlockState state) {
        var block = BLOCK_REMAP.get(state.getBlock());
        return block != null ? block.defaultBlockState() : state;
    }

    public static ResourceLocation remapItemID(ResourceLocation loc) {
        var item = ITEMID_REMAP.get(loc);
        return item != null ? item : loc;
    }

    public static ResourceLocation remapBlockID(ResourceLocation loc) {
        var block = BLOCKID_REMAP.get(loc);
        return block != null ? block : loc;
    }

    public static String remapID(String id) {
        var target = ID_REMAP.get(id);
        return target != null ? target : id;
    }

    @SuppressWarnings("deprecation")
    private static Block remapBlocks(Block block) {
        var type = NA;
        var registry = block.builtInRegistryHolder();
        var tags = registry.tags().toList();
        var id = AllUtils.safeId(block);
        var newList = new ArrayList<>(MaterialEntry.types);
        newList.sort((a, b) -> Integer.compare(b.length(), a.length()));
        var metal = "";
        for (var s : newList)
            if (id.contains(s)) {
                metal = s;
                break;
            }
        var check = checkTypeForBlock(metal, id, tags);
        if (check != NA) type = check;
        if (type == NA && metal.equals("gold")) {
            check = checkTypeForBlock("golden", id, tags);
            if (check != NA) type = check;
        }
        if (type == NA) return block;
        Block remapped = null;
        if (!metal.isEmpty() && MetalEntry.typeEntries.get(metal) instanceof MetalEntry entry)
            remapped = entry.getBlock(type).get();
        return remapped != null ? remapped : block;
    }

    private static MetalType checkTypeForBlock(String metal, String id, List<TagKey<Block>> tags) {
        if (tags.contains(blockC("storage_blocks/" + metal))) return STORAGE;
        else if (tags.contains(blockC("storage_blocks/raw_" + metal))) return RAW_STORAGE;
        else if (tags.contains(blockC("ores/" + metal))) {
            if (tags.contains(blockC("ores_in_ground/stone"))) return ORE_STONE;
            else if (tags.contains(blockC("ores_in_ground/deepslate"))) return ORE_DEEPSLATE;
        } else if (id.equals(metal + "_storage_block") || id.equals("storage_block_" + metal) || id.equals(metal + "_storage") || id.equals("storage_" + metal) || id.equals(metal + "_block") || id.equals("block_" + metal))
            return STORAGE;
        else if (id.equals("stone_" + metal + "_ore")
                || id.equals("ore_" + metal + "_stone")
                || id.equals(metal + "_stone_ore")
                || id.equals(metal + "_ore_stone")
                || id.equals("stone_ore_" + metal)
                || id.equals("ore_stone_" + metal)
                || id.equals(metal + "_ore")
                || id.equals("ore_" + metal)
        ) return ORE_STONE;
        else if (id.equals("deepslate_" + metal + "_ore")
                || id.equals("ore_" + metal + "_deepslate")
                || id.equals(metal + "_deepslate_ore")
                || id.equals(metal + "_ore_deepslate")
                || id.equals("deepslate_ore_" + metal)
                || id.equals("ore_deepslate_" + metal)
        ) return ORE_DEEPSLATE;
        else if (id.equals("raw_" + metal + "_storage_block")
                || id.equals("storage_block_" + metal + "_raw")
                || id.equals("raw_" + metal + "_storage")
                || id.equals("storage_" + metal + "_raw")
                || id.equals("raw_" + metal + "_block")
                || id.equals("block_" + metal + "_raw")

                || id.equals(metal + "_raw_storage_block")
                || id.equals("storage_block_raw_" + metal)
                || id.equals(metal + "_raw_storage")
                || id.equals("storage_raw_" + metal)
                || id.equals(metal + "_raw_block")
                || id.equals("block_raw_" + metal)

                || id.equals(metal + "_storage_block_raw")
                || id.equals("raw_storage_block_" + metal)
                || id.equals(metal + "_storage_raw")
                || id.equals("raw_storage_" + metal)
                || id.equals(metal + "_block_raw")
                || id.equals("raw_block_" + metal)
        ) return RAW_STORAGE;
        return NA;
    }

    @SuppressWarnings("deprecation")
    private static Item remapItems(Item item) {
        var type = NA;
        var registry = item.builtInRegistryHolder();
        var tags = registry.tags().toList();
        var id = AllUtils.safeId(item);
        var newList = new ArrayList<>(MaterialEntry.types);
        newList.sort((a, b) -> Integer.compare(b.length(), a.length()));
        var metal = "";
        for (var s : newList)
            if (id.contains(s)) {
                metal = s;
                break;
            }
        var check = checkTypeForItem(metal, id, tags);
        if (check != NA) type = check;
        if (type == NA && metal.equals("gold")) {
            check = checkTypeForItem("golden", id, tags);
            if (check != NA) type = check;
        }
        if (type == NA) return item;
        Item remapped = null;
        if (!metal.isEmpty() && MetalEntry.typeEntries.get(metal) instanceof MetalEntry entry)
            remapped = entry.get(type).get().asItem();
        return remapped != null ? remapped : item;
    }

    private static MetalType checkTypeForItem(String metal, String id, List<TagKey<Item>> tags) {
        if (tags.contains(itemC("storage_blocks/" + metal))) return STORAGE;
        else if (tags.contains(itemC("ingots/" + metal))) return INGOT;
        else if (tags.contains(itemC("nuggets/" + metal))) return NUGGET;
        else if (tags.contains(itemC("plates/" + metal))) return SHEET;
        else if (tags.contains(itemC("gears/" + metal))) return GEAR;
        else if (tags.contains(itemC("wires/" + metal))) return WIRE;
        else if (tags.contains(itemC("rods/" + metal))) return ROD;
        else if (tags.contains(itemC("dusts/" + metal))) return DUST;

        else if (tags.contains(itemC("raw_materials/" + metal))) return RAW;
        else if (tags.contains(itemC("dusts/raw_" + metal))) return RAW_CRUSHED;
        else if (tags.contains(itemC("storage_blocks/raw_" + metal))) return RAW_STORAGE;
        else if (tags.contains(itemC("ores/" + metal))) {
            if (tags.contains(itemC("ores_in_ground/stone"))) return ORE_STONE;
            else if (tags.contains(itemC("ores_in_ground/deepslate"))) return ORE_DEEPSLATE;
        } else if (id.equals(metal + "_storage_block") || id.equals("storage_block_" + metal) || id.equals(metal + "_storage") || id.equals("storage_" + metal) || id.equals(metal + "_block") || id.equals("block_" + metal))
            return STORAGE;
        else if (id.equals(metal + "_ingot") || id.equals("ingot_" + metal)) return INGOT;
        else if (id.equals(metal + "_nugget") || id.equals("nugget_" + metal)) return NUGGET;
        else if (id.equals(metal + "_plate") || id.equals("plate_" + metal) || id.equals(metal + "_sheet") || id.equals("sheet_" + metal))
            return SHEET;
        else if (id.equals(metal + "_gear") || id.equals("gear_" + metal)) return GEAR;
        else if (id.equals(metal + "_wire") || id.equals("wire_" + metal)) return WIRE;
        else if (id.equals(metal + "_rod") || id.equals("rod_" + metal)) return ROD;
        else if (id.equals(metal + "_dust") || id.equals("dust_" + metal) || id.equals(metal + "_crushed") || id.equals("crushed_" + metal))
            return DUST;

        else if (id.equals(metal + "_raw") || id.equals("raw_" + metal)) return RAW;
        else if (id.equals("crushed_" + metal + "_raw")
                || id.equals("raw_" + metal + "_crushed")
                || id.equals(metal + "_crushed_raw")
                || id.equals(metal + "_raw_crushed")
                || id.equals("crushed_raw_" + metal)
                || id.equals("raw_crushed_" + metal)
                || id.equals("dust_" + metal + "_raw")
                || id.equals("raw_" + metal + "_dust")
                || id.equals(metal + "_dust_raw")
                || id.equals(metal + "_raw_dust")
                || id.equals("dust_raw_" + metal)
                || id.equals("raw_dust_" + metal)
        ) return RAW_CRUSHED;
        else if (id.equals("stone_" + metal + "_ore")
                || id.equals("ore_" + metal + "_stone")
                || id.equals(metal + "_stone_ore")
                || id.equals(metal + "_ore_stone")
                || id.equals("stone_ore_" + metal)
                || id.equals("ore_stone_" + metal)
                || id.equals(metal + "_ore")
                || id.equals("ore_" + metal)
        ) return ORE_STONE;
        else if (id.equals("deepslate_" + metal + "_ore")
                || id.equals("ore_" + metal + "_deepslate")
                || id.equals(metal + "_deepslate_ore")
                || id.equals(metal + "_ore_deepslate")
                || id.equals("deepslate_ore_" + metal)
                || id.equals("ore_deepslate_" + metal)
        ) return ORE_DEEPSLATE;
        else if (id.equals("raw_" + metal + "_storage_block")
                || id.equals("storage_block_" + metal + "_raw")
                || id.equals("raw_" + metal + "_storage")
                || id.equals("storage_" + metal + "_raw")
                || id.equals("raw_" + metal + "_block")
                || id.equals("block_" + metal + "_raw")

                || id.equals(metal + "_raw_storage_block")
                || id.equals("storage_block_raw_" + metal)
                || id.equals(metal + "_raw_storage")
                || id.equals("storage_raw_" + metal)
                || id.equals(metal + "_raw_block")
                || id.equals("block_raw_" + metal)

                || id.equals(metal + "_storage_block_raw")
                || id.equals("raw_storage_block_" + metal)
                || id.equals(metal + "_storage_raw")
                || id.equals("raw_storage_" + metal)
                || id.equals(metal + "_block_raw")
                || id.equals("raw_block_" + metal)
        ) return RAW_STORAGE;
        return NA;
    }

    public static String remapLocIDs(String id) {
        if (id.contains("unify")) return id;
        var type = NA;
        var newList = new ArrayList<>(MaterialEntry.types);
        newList.sort((a, b) -> Integer.compare(b.length(), a.length()));
        var itemId = id.contains(":") ? id.split(":", 2)[1] : id;
        var metal = "";
        for (var s : newList)
            if (itemId.contains(s)) {
                metal = s;
                break;
            }

        var check = checkTypeForItem(metal, itemId);
        if (check != NA) type = check;
        if (type == NA && metal.equals("gold")) {
            check = checkTypeForItem("golden", itemId);
            if (check != NA) type = check;
        }

        if (type == NA) return id;
        if (!metal.isEmpty() && MetalEntry.typeEntries.get(metal) instanceof MetalEntry entry)
            return BuiltInRegistries.ITEM.getKey(entry.get(type).get().asItem()).toString();
        return id;
    }

    private static MetalType checkTypeForItem(String metal, String itemId) {

        if (itemId.equals("storage_blocks/" + metal)) return STORAGE;
        else if (itemId.equals("ingots/" + metal)) return INGOT;
        else if (itemId.equals("nuggets/" + metal)) return NUGGET;
        else if (itemId.equals("plates/" + metal)) return SHEET;
        else if (itemId.equals("gears/" + metal)) return GEAR;
        else if (itemId.equals("wires/" + metal)) return WIRE;
        else if (itemId.equals("rods/" + metal)) return ROD;
        else if (itemId.equals("dusts/" + metal)) return DUST;

        else if (itemId.equals("raw_materials/" + metal)) return RAW;
        else if (itemId.equals("dusts/raw_" + metal)) return RAW_CRUSHED;
        else if (itemId.equals("storage_blocks/raw_" + metal)) return RAW_STORAGE;

        else if (itemId.equals(metal + "_storage_block") || itemId.equals("storage_block_" + metal) || itemId.equals(metal + "_storage") || itemId.equals("storage_" + metal) || itemId.equals(metal + "_block") || itemId.equals("block_" + metal))
            return STORAGE;
        else if (itemId.equals(metal + "_ingot") || itemId.equals("ingot_" + metal)) return INGOT;
        else if (itemId.equals(metal + "_nugget") || itemId.equals("nugget_" + metal)) return NUGGET;
        else if (itemId.equals(metal + "_plate") || itemId.equals("plate_" + metal) || itemId.equals(metal + "_sheet") || itemId.equals("sheet_" + metal))
            return SHEET;
        else if (itemId.equals(metal + "_gear") || itemId.equals("gear_" + metal)) return GEAR;
        else if (itemId.equals(metal + "_wire") || itemId.equals("wire_" + metal)) return WIRE;
        else if (itemId.equals(metal + "_rod") || itemId.equals("rod_" + metal)) return ROD;
        else if (itemId.equals(metal + "_dust") || itemId.equals("dust_" + metal) || itemId.equals(metal + "_crushed") || itemId.equals("crushed_" + metal))
            return DUST;

        else if (itemId.equals(metal + "_raw") || itemId.equals("raw_" + metal)) return RAW;
        else if (itemId.equals("crushed_" + metal + "_raw")
                || itemId.equals("raw_" + metal + "_crushed")
                || itemId.equals(metal + "_crushed_raw")
                || itemId.equals(metal + "_raw_crushed")
                || itemId.equals("crushed_raw_" + metal)
                || itemId.equals("raw_crushed_" + metal)
                || itemId.equals("dust_" + metal + "_raw")
                || itemId.equals("raw_" + metal + "_dust")
                || itemId.equals(metal + "_dust_raw")
                || itemId.equals(metal + "_raw_dust")
                || itemId.equals("dust_raw_" + metal)
                || itemId.equals("raw_dust_" + metal)
        ) return RAW_CRUSHED;
        else if (itemId.equals("stone_" + metal + "_ore")
                || itemId.equals("ore_" + metal + "_stone")
                || itemId.equals(metal + "_stone_ore")
                || itemId.equals(metal + "_ore_stone")
                || itemId.equals("stone_ore_" + metal)
                || itemId.equals("ore_stone_" + metal)
                || itemId.equals(metal + "_ore")
                || itemId.equals("ore_" + metal)
        ) return ORE_STONE;
        else if (itemId.equals("deepslate_" + metal + "_ore")
                || itemId.equals("ore_" + metal + "_deepslate")
                || itemId.equals(metal + "_deepslate_ore")
                || itemId.equals(metal + "_ore_deepslate")
                || itemId.equals("deepslate_ore_" + metal)
                || itemId.equals("ore_deepslate_" + metal)
        ) return ORE_DEEPSLATE;
        else if (itemId.equals("raw_" + metal + "_storage_block")
                || itemId.equals("storage_block_" + metal + "_raw")
                || itemId.equals("raw_" + metal + "_storage")
                || itemId.equals("storage_" + metal + "_raw")
                || itemId.equals("raw_" + metal + "_block")
                || itemId.equals("block_" + metal + "_raw")

                || itemId.equals(metal + "_raw_storage_block")
                || itemId.equals("storage_block_raw_" + metal)
                || itemId.equals(metal + "_raw_storage")
                || itemId.equals("storage_raw_" + metal)
                || itemId.equals(metal + "_raw_block")
                || itemId.equals("block_raw_" + metal)

                || itemId.equals(metal + "_storage_block_raw")
                || itemId.equals("raw_storage_block_" + metal)
                || itemId.equals(metal + "_storage_raw")
                || itemId.equals("raw_storage_" + metal)
                || itemId.equals(metal + "_block_raw")
                || itemId.equals("raw_block_" + metal)
        ) return RAW_STORAGE;
        return NA;
    }

    public static void handleRemapping(Map.Entry<ResourceLocation, JsonElement> entry, JsonElement json, JsonObject obj, List<String> outputs, List<String> inner) {
        for (var field : outputs)
            if (obj.has(field) && obj.get(field).isJsonObject()) {
                var inputObj = obj.getAsJsonObject(field);

                var target = inputObj;
                for (var inside : inner)
                    if (inputObj.has(inside) && inputObj.get(inside).isJsonObject()) {
                        target = inputObj.getAsJsonObject(inside);
                        break;
                    }

                String id = null, idType = null;
                for (var key : List.of("item", "id", "tag"))
                    if (target.has(key)) {
                        var targetKey = target.get(key);
                        if (targetKey != null && targetKey.isJsonPrimitive()) {
                            id = targetKey.getAsString();
                            idType = key;
                        }
                    }

                if (id != null) {
                    var remapped = UnifyRemapper.remapLocIDs(id);
                    if (!remapped.equals(id)) {
                        target.remove(idType);
                        target.addProperty(idType.equals("tag") ? "item" : idType, remapped);
                    }
                }
            } else if (obj.has(field) && obj.get(field).isJsonArray()) {
                var array = obj.getAsJsonArray(field);
                for (int i = 0; i < array.size(); i++) {
                    var elem = array.get(i);
                    if (!elem.isJsonObject()) continue;
                    var ingObj = elem.getAsJsonObject();

                    var target = ingObj;
                    for (var inside : inner)
                        if (ingObj.has(inside) && ingObj.get(inside).isJsonObject()) {
                            target = ingObj.getAsJsonObject(inside);
                            break;
                        }

                    String id = null, idType = null;
                    for (var key : List.of("item", "id", "tag"))
                        if (target.has(key)) {
                            var targetKey = target.get(key);
                            if (targetKey != null && targetKey.isJsonPrimitive()) {
                                id = targetKey.getAsString();
                                idType = key;
                            }
                        }

                    if (id != null) {
                        var remapped = UnifyRemapper.remapLocIDs(id);
                        if (!remapped.equals(id)) {
                            target.remove(idType);
                            target.addProperty(idType.equals("tag") ? "item" : idType, remapped);
                        }
                    }
                }
            }
        if (obj.has("key") && obj.get("key").isJsonObject()) {
            var keyObj = obj.getAsJsonObject("key");
            for (var keyEntry : keyObj.entrySet()) {
                var keyVal = keyEntry.getValue();
                if (!keyVal.isJsonObject()) continue;
                var ingredient = keyVal.getAsJsonObject();

                String id = null, idType = null;
                for (var key : List.of("item", "id", "tag"))
                    if (ingredient.has(key)) {
                        var targetKey = ingredient.get(key);
                        if (targetKey != null && targetKey.isJsonPrimitive()) {
                            id = targetKey.getAsString();
                            idType = key;
                        }
                    }

                if (id != null) {
                    var remapped = UnifyRemapper.remapLocIDs(id);
                    if (!remapped.equals(id)) {
                        ingredient.remove(idType);
                        ingredient.addProperty(idType.equals("tag") ? "item" : idType, remapped);
                    }
                }
            }
        }
    }

    public static void remapConfiguredFeature(ResourceLocation id, JsonElement json) {
        if (!json.isJsonObject()) return;
        var obj = json.getAsJsonObject();

        if (!"minecraft:ore".equals(obj.get("type").getAsString())) return;

        var config = obj.getAsJsonObject("config");
        if (!config.has("targets") || !config.get("targets").isJsonArray()) return;

        var targets = config.getAsJsonArray("targets");

        for (int i = 0; i < targets.size(); i++) {
            var entry = targets.get(i);
            if (!entry.isJsonObject()) continue;

            var targetObj = entry.getAsJsonObject();
            if (!targetObj.has("state")) continue;

            var stateObj = targetObj.getAsJsonObject("state");
            if (!stateObj.has("Name")) continue;

            var originalId = stateObj.get("Name").getAsString();
            var remappedId = UnifyRemapper.remapID(originalId);
            if (!originalId.equals(remappedId)) {
                stateObj.addProperty("Name", remappedId);
            }
        }
    }

}
