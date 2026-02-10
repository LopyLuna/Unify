package dev.lopyluna.unify.helper;

import dev.lopyluna.unify.register.AllSoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

import static dev.lopyluna.unify.content.AllUtils.array;
import static dev.lopyluna.unify.content.AllUtils.pair;

@SuppressWarnings("unused")
public enum MetalOreEntries {
    COPPER("Copper", MapColor.COLOR_ORANGE, SoundType.COPPER, array(), array(
            pair("ingot", Items.COPPER_INGOT),
            pair("rawChunk", Items.RAW_COPPER),
            pair("storage", Blocks.COPPER_BLOCK),
            pair("rawStorage", Blocks.RAW_COPPER_BLOCK),
            pair("oreStone", Blocks.COPPER_ORE),
            pair("oreDeep", Blocks.DEEPSLATE_COPPER_ORE)
    )),
    IRON("Iron", MapColor.METAL, SoundType.METAL, array("beaconCompatible"), array(
            pair("ingot", Items.IRON_INGOT),
            pair("nugget", Items.IRON_NUGGET),
            pair("rawChunk", Items.RAW_IRON),
            pair("storage", Blocks.IRON_BLOCK),
            pair("rawStorage", Blocks.RAW_IRON_BLOCK),
            pair("oreStone", Blocks.IRON_ORE),
            pair("oreDeep", Blocks.DEEPSLATE_IRON_ORE)
    )),
    GOLD("Gold", MapColor.GOLD, SoundType.METAL, array("ironMineable", "beaconCompatible"), array(
            pair("ingot", Items.GOLD_INGOT),
            pair("nugget", Items.GOLD_NUGGET),
            pair("rawChunk", Items.RAW_GOLD),
            pair("storage", Blocks.GOLD_BLOCK),
            pair("rawStorage", Blocks.RAW_GOLD_BLOCK),
            pair("oreStone", Blocks.GOLD_ORE),
            pair("oreDeep", Blocks.DEEPSLATE_GOLD_ORE)
    ), "golden"),
    ALUMINUM("Aluminum", MapColor.METAL, SoundType.COPPER, array(), "bauxite"),
    TIN("Tin", MapColor.METAL, SoundType.METAL, array("beaconCompatible")),
    OSMIUM("Osmium", MapColor.METAL, AllSoundEvents.COPPER_HEAVY, array("beaconCompatible")),
    ZINC("Zinc", MapColor.GLOW_LICHEN, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    LEAD("Lead", MapColor.COLOR_PURPLE, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    NICKEL("Nickel", MapColor.COLOR_YELLOW, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    SILVER("Silver", MapColor.METAL, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    COBALT("Cobalt", MapColor.METAL, AllSoundEvents.COPPER_HEAVY, array("diamondMineable", "beaconCompatible")),
    PLATINUM("Platinum", MapColor.ICE, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    PALLADIUM("Palladium", MapColor.TERRACOTTA_PINK, SoundType.METAL, array("diamondMineable", "beaconCompatible")),
    TUNGSTEN("Tungsten", MapColor.TERRACOTTA_LIGHT_GREEN, AllSoundEvents.NETHERITE_HEAVY, array("diamondMineable", "beaconCompatible"), "wolfram"),
    TITANIUM("Titanium", MapColor.TERRACOTTA_CYAN, AllSoundEvents.NETHERITE_HEAVY, array("diamondMineable", "beaconCompatible")),
    URANIUM("Uranium", MapColor.TERRACOTTA_GREEN, AllSoundEvents.METAL_HEAVY, array("ironMineable", "beaconCompatible")),
    THORIUM("Thorium", MapColor.COLOR_PINK, AllSoundEvents.METAL_HEAVY, array("diamondMineable", "beaconCompatible"));

    public final MetalOreEntry entry;
    public final List<String> targets;

    MetalOreEntries(String type, MapColor color, SoundType sound, String... targets) {
        this(type, color, sound, new ArrayList<>(), new ArrayList<>(), targets);
    }

    MetalOreEntries(String type, MapColor color, SoundType sound, List<String> additions, String... targets) {
        this(type, color, sound, additions, new ArrayList<>(), targets);
    }

    @SuppressWarnings("unchecked")
    MetalOreEntries(String type, MapColor color, SoundType sound, List<String> additions, List<Pair<String, Object>> additionsPairs, String... targets) {
        List<String> values = array(targets);
        values.add(type.toLowerCase().replace(" ", "_"));
        this.targets = values;
        var entry = new MetalOreEntry(type, color, sound);
        for (var add : additions) switch (add) {
            case "smallBlock" -> entry = entry.smallBlock();
            case "beaconCompatible" -> entry = entry.beaconCompatible();
            case "woodenMineable" -> entry = entry.woodenMineable();
            case "stoneMineable" -> entry = entry.stoneMineable();
            case "ironMineable" -> entry = entry.ironMineable();
            case "goldMineable" -> entry = entry.goldMineable();
            case "diamondMineable" -> entry = entry.diamondMineable();
            case "netheriteMineable" -> entry = entry.netheriteMineable();
        }
        for (var add : additionsPairs) {
            var a = add.getA();
            var b = add.getB();
            switch (a) {
                case "mineable" -> entry = entry.mineable((TagKey<Block>) b);
                case "ingot" -> entry = entry.ingot((ItemLike) b);
                case "nugget" -> entry = entry.nugget((ItemLike) b);
                case "sheet" -> entry = entry.sheet((ItemLike) b);
                case "gear" -> entry = entry.gear((ItemLike) b);
                case "rod" -> entry = entry.rod((ItemLike) b);
                case "wire" -> entry = entry.wire((ItemLike) b);
                case "dust" -> entry = entry.dust((ItemLike) b);
                case "storageItem" -> entry = entry.storage((ItemLike) b);
                case "storageBlock", "storage" -> entry = entry.storage((Block) b);

                case "rawChunk" -> entry = entry.rawChunk((ItemLike) b);
                case "crushedOre" -> entry = entry.crushedOre((ItemLike) b);

                case "rawStorageItem" -> entry = entry.rawStorage((ItemLike) b);
                case "rawStorageBlock", "rawStorage" -> entry = entry.rawStorage((Block) b);
                case "oreStoneItem" -> entry = entry.oreStone((ItemLike) b);
                case "oreStoneBlock", "oreStone" -> entry = entry.oreStone((Block) b);
                case "oreDeepItem" -> entry = entry.oreDeep((ItemLike) b);
                case "oreDeepBlock", "oreDeep" -> entry = entry.oreDeep((Block) b);
            }
        }
        this.entry = entry;
    }
}
