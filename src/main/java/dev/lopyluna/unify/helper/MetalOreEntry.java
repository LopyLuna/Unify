package dev.lopyluna.unify.helper;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;

import java.util.function.Supplier;

import static dev.lopyluna.unify.Unify.REG;
import static dev.lopyluna.unify.content.AllUtils.pickaxeOnly;
import static dev.lopyluna.unify.register.AllTags.*;

@SuppressWarnings("unused")
public class MetalOreEntry extends MetalEntry {
    public Supplier<Item> rawMaterial;
    public Supplier<Item> crushedOre;
    public Supplier<Block> rawStorage;
    public Supplier<Block> oreStone;
    public Supplier<Block> oreDeepslate;

    public final String rawID;
    public final String rawType;
    public final MapColor rawColor;
    public final SoundType rawSound;

    public MetalOreEntry(String type, MapColor color, SoundType sound) {
        this(type, color, sound, type, color, SoundType.STONE);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, MapColor rawColor) {
        this(type, color, sound, type, rawColor, SoundType.STONE);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, SoundType rawSound) {
        this(type, color, sound, type, color, rawSound);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, MapColor rawColor, SoundType rawSound) {
        this(type, color, sound, type, rawColor, rawSound);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, String rawType) {
        this(type, color, sound, rawType, color, SoundType.STONE);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, String rawType, MapColor rawColor) {
        this(type, color, sound, rawType, rawColor, SoundType.STONE);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, String rawType, SoundType rawSound) {
        this(type, color, sound, rawType, color, rawSound);
    }

    public MetalOreEntry(String type, MapColor color, SoundType sound, String rawType, MapColor rawColor, SoundType rawSound) {
        super(type, color, sound);
        this.rawID = rawType.toLowerCase().replace(" ", "_");
        this.rawType = rawType;
        this.rawColor = rawColor;
        this.rawSound = rawSound;
    }

    @Override
    public MetalOreEntry mineable(TagKey<Block> mineable) {
        super.mineable(mineable);
        return this;
    }

    @Override
    public MetalOreEntry beaconCompatible() {
        super.beaconCompatible();
        return this;
    }

    @Override
    public MetalOreEntry smallBlock() {
        super.smallBlock();
        return this;
    }

    @Override
    public MetalOreEntry woodenMineable() {
        super.woodenMineable();
        return this;
    }

    @Override
    public MetalOreEntry stoneMineable() {
        super.stoneMineable();
        return this;
    }

    @Override
    public MetalOreEntry ironMineable() {
        super.ironMineable();
        return this;
    }

    @Override
    public MetalOreEntry goldMineable() {
        super.goldMineable();
        return this;
    }

    @Override
    public MetalOreEntry diamondMineable() {
        super.diamondMineable();
        return this;
    }

    @Override
    public MetalOreEntry netheriteMineable() {
        super.netheriteMineable();
        return this;
    }

    @Override
    public MetalOreEntry ingot(ItemLike item) {
        super.ingot(item);
        return this;
    }

    @Override
    public MetalOreEntry nugget(ItemLike item) {
        super.nugget(item);
        return this;
    }

    @Override
    public MetalOreEntry sheet(ItemLike item) {
        super.sheet(item);
        return this;
    }

    @Override
    public MetalOreEntry gear(ItemLike item) {
        super.gear(item);
        return this;
    }

    @Override
    public MetalOreEntry rod(ItemLike item) {
        super.rod(item);
        return this;
    }

    @Override
    public MetalOreEntry wire(ItemLike item) {
        super.wire(item);
        return this;
    }

    @Override
    public MetalOreEntry dust(ItemLike item) {
        super.dust(item);
        return this;
    }

    @Override
    public MetalOreEntry storage(ItemLike item) {
        super.storage(item);
        return this;
    }

    @Override
    public MetalOreEntry storage(Block block) {
        super.storage(block);
        return this;
    }

    public MetalOreEntry rawMaterial(ItemLike item) {
        rawMaterial = item::asItem;
        return this;
    }

    public MetalOreEntry crushedOre(ItemLike item) {
        crushedOre = item::asItem;
        return this;
    }

    public MetalOreEntry rawStorage(ItemLike item) {
        if (item.asItem() instanceof BlockItem block) rawStorage = block::getBlock;
        return this;
    }

    public MetalOreEntry rawStorage(Block block) {
        rawStorage = () -> block;
        return this;
    }

    public MetalOreEntry oreStone(ItemLike item) {
        if (item.asItem() instanceof BlockItem block) oreStone = block::getBlock;
        return this;
    }

    public MetalOreEntry oreStone(Block block) {
        oreStone = () -> block;
        return this;
    }

    public MetalOreEntry oreDeepslate(ItemLike item) {
        if (item.asItem() instanceof BlockItem block) oreDeepslate = block::getBlock;
        return this;
    }

    public MetalOreEntry oreDeepslate(Block block) {
        oreDeepslate = () -> block;
        return this;
    }

    @Override
    public Supplier<ItemLike> get(MetalType type) {
        return switch (type) {
            case INGOT -> ingot::get;
            case NUGGET -> nugget::get;
            case SHEET -> sheet::get;
            case GEAR -> gear::get;
            case WIRE -> wire::get;
            case ROD -> rod::get;
            case DUST -> dust::get;
            case STORAGE -> storage::get;
            case RAW -> rawMaterial::get;
            case RAW_CRUSHED -> crushedOre::get;
            case RAW_STORAGE -> rawStorage::get;
            case ORE_STONE -> oreStone::get;
            case ORE_DEEPSLATE -> oreDeepslate::get;
            case NA -> null;
        };
    }

    @Override
    public Supplier<Block> getBlock(MetalType type) {
        return switch (type) {
            case STORAGE -> storage;
            case RAW_STORAGE -> rawStorage;
            case ORE_STONE -> oreStone;
            case ORE_DEEPSLATE -> oreDeepslate;
            case INGOT, NUGGET, SHEET, GEAR, WIRE, ROD, DUST, RAW, RAW_CRUSHED, NA -> null;
        };
    }

    @Override
    public MetalOreEntry register() {
        super.register();

        var rawMaterialTag = itemC("raw_materials/" + rawID);
        if (rawMaterial == null) rawMaterial = REG.item("raw_" + rawID, Item::new)
                .lang("Raw " + rawType)
                .tag(rawMaterialTag, itemC("raw_materials"))
                .register();
        itemEntries.add(rawMaterial);
        tagKeys.add(rawMaterialTag);

        var rawDust = itemC("dusts/raw_" + rawID);
        if (crushedOre == null) crushedOre = REG.item("crushed_raw_" + rawID, Item::new)
                .lang("Crushed Raw " + rawType)
                .tag(rawDust, itemC("dusts"))
                .register();
        itemEntries.add(crushedOre);
        tagKeys.add(rawDust);

        var storageTag = itemC("storage_blocks/raw_" + rawID);
        if (rawStorage == null) rawStorage = REG.block("raw_" + rawID + "_block", Block::new)
                .lang(blockOf ? "Block of Raw " + rawType : "Raw " + rawType + " Block")
                .initialProperties(() -> Blocks.RAW_IRON_BLOCK)
                .properties(p -> p.requiresCorrectToolForDrops().mapColor(rawColor).sound(rawSound))
                .transform(pickaxeOnly())
                .tag(mineable != null ? mineable : BlockTags.NEEDS_STONE_TOOL)
                .tag(blockC("storage_blocks/raw_" + rawID), blockC("storage_blocks"))
                .item()
                .tag(storageTag, itemC("storage_blocks"))
                .build().register();
        blockEntries.add(rawStorage);
        tagKeys.add(storageTag);

        var oresTag = itemC("ores/" + rawID);
        if (oreStone == null) oreStone = REG.block(rawID + "_ore", Block::new)
                .lang(rawType + " Ore")
                .initialProperties(() -> Blocks.IRON_ORE)
                .properties(p -> p.requiresCorrectToolForDrops().mapColor(rawColor))
                .transform(pickaxeOnly())
                .loot((lt, b) -> lt.add(b, lt.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(rawMaterial.get())
                        .apply(ApplyBonusCount.addOreBonusCount(lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE)))))))
                .tag(mineable != null ? mineable : BlockTags.NEEDS_STONE_TOOL)
                .tag(blockC("ores/" + rawID), blockC("ores"), blockC("ores_in_ground/stone"))
                .item()
                .tag(oresTag, itemC("ores"), itemC("ores_in_ground/stone"))
                .build().register();
        blockEntries.add(oreStone);
        if (oreDeepslate == null) oreDeepslate = REG.block("deepslate_" + rawID + "_ore", Block::new)
                .lang("Deepslate " + rawType + " Ore")
                .initialProperties(() -> Blocks.DEEPSLATE_IRON_ORE)
                .properties(p -> p.requiresCorrectToolForDrops().mapColor(rawColor))
                .transform(pickaxeOnly())
                .loot((lt, b) -> lt.add(b, lt.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(rawMaterial.get())
                        .apply(ApplyBonusCount.addOreBonusCount(lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE)))))))
                .tag(mineable != null ? mineable : BlockTags.NEEDS_STONE_TOOL)
                .tag(blockC("ores/" + rawID), blockC("ores"), blockC("ores_in_ground/deepslate"))
                .item()
                .tag(oresTag, itemC("ores"), itemC("ores_in_ground/deepslate"))
                .build().register();
        blockEntries.add(oreDeepslate);
        tagKeys.add(oresTag);

        return this;
    }
}
