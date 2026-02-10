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

public enum MetalEntries {
    NETHERITE("Netherite", MapColor.COLOR_BLACK, SoundType.NETHERITE_BLOCK, array("diamondMineable", "beaconCompatible"), array(
            pair("ingot", Items.NETHERITE_INGOT),
            pair("storage", Blocks.NETHERITE_BLOCK)
    )),
    ANNEALED_COPPER("Annealed Copper", MapColor.TERRACOTTA_ORANGE, AllSoundEvents.COPPER_HEAVY, array("beaconCompatible")),
    WROUGHT_IRON("Wrought Iron", MapColor.COLOR_GRAY, SoundType.NETHERITE_BLOCK, array("beaconCompatible")),
    TARNISHED_GOLD("Tarnished Gold", MapColor.TERRACOTTA_YELLOW, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    CAST_IRON("Cast Iron", MapColor.COLOR_BLACK, SoundType.NETHERITE_BLOCK, array("beaconCompatible")),
    ROSE_GOLD("Rose Gold", MapColor.COLOR_PINK, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    CONSTANTAN("Constantan", MapColor.COLOR_ORANGE, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    INVAR("Invar", MapColor.METAL, AllSoundEvents.COPPER_HEAVY, array("ironMineable", "beaconCompatible")),
    ELECTRUM("Electrum", MapColor.COLOR_YELLOW, AllSoundEvents.COPPER_HEAVY, array("ironMineable", "beaconCompatible")),
    BRONZE("Bronze", MapColor.COLOR_ORANGE, AllSoundEvents.NETHERITE_HEAVY, array("ironMineable", "beaconCompatible")),
    BRASS("Brass", MapColor.COLOR_YELLOW, SoundType.METAL, array("ironMineable", "beaconCompatible")),
    STEEL("Steel", MapColor.COLOR_LIGHT_GRAY, AllSoundEvents.NETHERITE_HEAVY, array("ironMineable", "beaconCompatible"));

    public final MetalEntry entry;
    public final List<String> targets;

    MetalEntries(String type, MapColor color, SoundType sound, String... targets) {
        this(type, color, sound, new ArrayList<>(), new ArrayList<>(), targets);
    }

    MetalEntries(String type, MapColor color, SoundType sound, List<String> additions, String... targets) {
        this(type, color, sound, additions, new ArrayList<>(), targets);
    }

    @SuppressWarnings("unchecked")
    MetalEntries(String type, MapColor color, SoundType sound, List<String> additions, List<Pair<String, Object>> additionsPairs, String... targets) {
        List<String> values = array(targets);
        values.add(type.toLowerCase().replace(" ", "_"));
        this.targets = values;
        var entry = new MetalEntry(type, color, sound);
        for (var add : additions) switch (add) {
            case "smallBlock" -> entry = entry.smallBlock();
            case "beaconCompatible" -> entry = entry.beaconCompatible();
            case "woodenMineable" -> entry = entry.woodenMineable();
            case "stoneMineable" -> entry = entry.stoneMineable();
            case "ironMineable" -> entry = entry.ironMineable();
            case "goldMineable" -> entry = entry.goldMineable();
            case "diamondMineable" -> entry = entry.diamondMineable();
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
            }
        }
        this.entry = entry;
    }
}
