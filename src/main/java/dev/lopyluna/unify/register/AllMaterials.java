package dev.lopyluna.unify.register;

import dev.lopyluna.unify.helper.MetalEntry;
import dev.lopyluna.unify.helper.MetalOreEntry;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

@SuppressWarnings("unused")
public class AllMaterials {
    public static MetalOreEntry COPPER = new MetalOreEntry("Copper", MapColor.COLOR_ORANGE, SoundType.COPPER)
            .ingot(Items.COPPER_INGOT)
            .rawMaterial(Items.RAW_COPPER)
            .storage(Blocks.COPPER_BLOCK)
            .rawStorage(Blocks.RAW_COPPER_BLOCK)
            .oreStone(Blocks.COPPER_ORE)
            .oreDeepslate(Blocks.DEEPSLATE_COPPER_ORE)
            .register();
    public static MetalOreEntry IRON = new MetalOreEntry("Iron", MapColor.METAL, SoundType.METAL)
            .ingot(Items.IRON_INGOT)
            .nugget(Items.IRON_NUGGET)
            .rawMaterial(Items.RAW_IRON)
            .storage(Blocks.IRON_BLOCK)
            .rawStorage(Blocks.RAW_IRON_BLOCK)
            .oreStone(Blocks.IRON_ORE)
            .oreDeepslate(Blocks.DEEPSLATE_IRON_ORE)
            .beaconCompatible().register();
    public static MetalOreEntry GOLD = new MetalOreEntry("Gold", MapColor.GOLD, SoundType.METAL)
            .ingot(Items.GOLD_INGOT)
            .nugget(Items.GOLD_NUGGET)
            .rawMaterial(Items.RAW_GOLD)
            .storage(Blocks.GOLD_BLOCK)
            .rawStorage(Blocks.RAW_GOLD_BLOCK)
            .oreStone(Blocks.GOLD_ORE)
            .oreDeepslate(Blocks.DEEPSLATE_GOLD_ORE)
            .ironMineable()
            .beaconCompatible().register();
    public static MetalEntry NETHERITE = new MetalEntry("Netherite", MapColor.COLOR_BLACK, SoundType.NETHERITE_BLOCK)
            .ingot(Items.NETHERITE_INGOT)
            .storage(Blocks.NETHERITE_BLOCK)
            .diamondMineable()
            .beaconCompatible().register();

    public static MetalOreEntry ALUMINUM = new MetalOreEntry("Aluminum", MapColor.METAL, SoundType.COPPER).register();
    public static MetalOreEntry TIN = new MetalOreEntry("Tin", MapColor.METAL, SoundType.METAL).beaconCompatible().register();
    public static MetalOreEntry OSMIUM = new MetalOreEntry("Osmium", MapColor.METAL, AllSoundEvents.COPPER_HEAVY).beaconCompatible().register();
    public static MetalOreEntry ZINC = new MetalOreEntry("Zinc", MapColor.GLOW_LICHEN, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalOreEntry LEAD = new MetalOreEntry("Lead", MapColor.COLOR_PURPLE, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalOreEntry NICKEL = new MetalOreEntry("Nickel", MapColor.COLOR_YELLOW, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalOreEntry SILVER = new MetalOreEntry("Silver", MapColor.METAL, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalOreEntry PLATINUM = new MetalOreEntry("Platinum", MapColor.ICE, SoundType.METAL).ironMineable().beaconCompatible().register();
    //public static MetalOreEntry PALLADIUM = new MetalOreEntry("Palladium", MapColor.TERRACOTTA_PINK, SoundType.METAL).diamondMineable().beaconCompatible().register();
    public static MetalOreEntry TUNGSTEN = new MetalOreEntry("Tungsten", MapColor.TERRACOTTA_LIGHT_GREEN, AllSoundEvents.NETHERITE_HEAVY).diamondMineable().beaconCompatible().register();
    //public static MetalOreEntry TITANIUM = new MetalOreEntry("Titanium", MapColor.TERRACOTTA_CYAN, AllSoundEvents.NETHERITE_HEAVY).diamondMineable().beaconCompatible().register();
    public static MetalOreEntry URANIUM = new MetalOreEntry("Uranium", MapColor.TERRACOTTA_GREEN, AllSoundEvents.METAL_HEAVY).ironMineable().register();
    public static MetalOreEntry THORIUM = new MetalOreEntry("Thorium", MapColor.COLOR_PINK, AllSoundEvents.METAL_HEAVY).diamondMineable().register();

    public static MetalEntry WROUGHT_IRON = new MetalEntry("Wrought Iron", MapColor.COLOR_GRAY, SoundType.NETHERITE_BLOCK).beaconCompatible().register();
    public static MetalEntry TARNISHED_GOLD = new MetalEntry("Tarnished Gold", MapColor.TERRACOTTA_YELLOW, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalEntry CAST_IRON = new MetalEntry("Cast Iron", MapColor.COLOR_BLACK, SoundType.NETHERITE_BLOCK).beaconCompatible().register();
    public static MetalEntry ROSE_GOLD = new MetalEntry("Rose Gold", MapColor.COLOR_PINK, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalEntry CONSTANTAN = new MetalEntry("Constantan", MapColor.COLOR_ORANGE, SoundType.METAL).beaconCompatible().register();
    public static MetalEntry INVAR = new MetalEntry("Invar", MapColor.METAL, AllSoundEvents.COPPER_HEAVY).ironMineable().beaconCompatible().register();
    public static MetalEntry ELECTRUM = new MetalEntry("Electrum", MapColor.COLOR_YELLOW, AllSoundEvents.COPPER_HEAVY).ironMineable().beaconCompatible().register();
    public static MetalEntry BRONZE = new MetalEntry("Bronze", MapColor.COLOR_ORANGE, AllSoundEvents.NETHERITE_HEAVY).ironMineable().beaconCompatible().register();
    public static MetalEntry BRASS = new MetalEntry("Brass", MapColor.COLOR_YELLOW, SoundType.METAL).ironMineable().beaconCompatible().register();
    public static MetalEntry STEEL = new MetalEntry("Steel", MapColor.COLOR_LIGHT_GRAY, AllSoundEvents.NETHERITE_HEAVY).ironMineable().beaconCompatible().register();


    public static void register() {
    }
}
