package dev.lopyluna.unify.helper;

import dev.lopyluna.unify.Unify;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static dev.lopyluna.unify.Unify.REG;
import static dev.lopyluna.unify.content.AllUtils.pickaxeOnly;
import static dev.lopyluna.unify.register.AllTags.*;

@SuppressWarnings({"unused", "all"})
public class MetalEntry extends MaterialEntry {
    public Supplier<Item> ingot = null;
    public Supplier<Item> nugget = null;
    public Supplier<Item> sheet = null;
    public Supplier<Item> gear = null;
    public Supplier<Item> wire = null;
    public Supplier<Item> rod = null;
    public Supplier<Item> dust = null;
    public Supplier<Block> storage = null;

    private TagKey<Item> itemTag = null;

    protected List<Supplier<Item>> itemEntries = new ArrayList<>();
    protected List<Supplier<Block>> blockEntries = new ArrayList<>();
    protected List<TagKey<Item>> tagKeys = new ArrayList<>();

    protected TagKey<Block> mineable = null;
    protected boolean beaconCompatible = false;
    protected boolean blockOf = true;

    public MetalEntry(String type, MapColor color, SoundType sound) {
        super(type, color, sound);
    }

    public MetalEntry mineable(TagKey<Block> mineable) {
        this.mineable = mineable;
        return this;
    }

    public MetalEntry woodenMineable() {
        return mineable(Tags.Blocks.NEEDS_WOOD_TOOL);
    }

    public MetalEntry stoneMineable() {
        return mineable(BlockTags.NEEDS_STONE_TOOL);
    }

    public MetalEntry goldMineable() {
        return mineable(Tags.Blocks.NEEDS_GOLD_TOOL);
    }

    public MetalEntry ironMineable() {
        return mineable(BlockTags.NEEDS_IRON_TOOL);
    }

    public MetalEntry diamondMineable() {
        return mineable(BlockTags.NEEDS_DIAMOND_TOOL);
    }

    public MetalEntry netheriteMineable() {
        return mineable(Tags.Blocks.NEEDS_NETHERITE_TOOL);
    }

    public MetalEntry beaconCompatible() {
        beaconCompatible = true;
        return this;
    }

    public MetalEntry smallBlock() {
        blockOf = false;
        return this;
    }

    private MetalEntry itemTag(TagKey<Item> itemTag) {
        this.itemTag = itemTag;
        return this;
    }

    public MetalEntry ingot(ItemLike item) {
        ingot = item::asItem;
        return this;
    }

    public MetalEntry nugget(ItemLike item) {
        nugget = item::asItem;
        return this;
    }

    public MetalEntry sheet(ItemLike item) {
        sheet = item::asItem;
        return this;
    }

    public MetalEntry gear(ItemLike item) {
        gear = item::asItem;
        return this;
    }

    public MetalEntry rod(ItemLike item) {
        rod = item::asItem;
        return this;
    }

    public MetalEntry wire(ItemLike item) {
        wire = item::asItem;
        return this;
    }

    public MetalEntry dust(ItemLike item) {
        dust = item::asItem;
        return this;
    }

    public MetalEntry storage(ItemLike item) {
        if (item.asItem() instanceof BlockItem block) storage = block::getBlock;
        return this;
    }

    public MetalEntry storage(Block block) {
        storage = () -> block;
        return this;
    }

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
            case RAW, RAW_CRUSHED, RAW_STORAGE, ORE_STONE, ORE_DEEPSLATE, NA -> null;
        };
    }

    public Supplier<Block> getBlock(MetalType type) {
        return switch (type) {
            case STORAGE -> storage;
            case INGOT, NUGGET, SHEET, GEAR, WIRE, ROD, DUST, RAW, RAW_CRUSHED, RAW_STORAGE, ORE_STONE, ORE_DEEPSLATE,
                 NA -> null;
        };
    }

    @Override
    public MetalEntry register() {
        var ingotTag = itemC("ingots/" + id);
        if (ingot == null) ingot = REG.item(id + "_ingot", Item::new)
                .lang(type + " Ingot")
                .tag(beaconCompatible ? ItemTags.BEACON_PAYMENT_ITEMS : item("raw_beacon"))
                .tag(ingotTag, itemC("ingots"))
                .register();
        itemEntries.add(ingot);
        tagKeys.add(ingotTag);

        var nuggetTag = itemC("nuggets/" + id);
        if (nugget == null) nugget = REG.item(id + "_nugget", Item::new)
                .lang(type + " Nugget")
                .tag(nuggetTag, itemC("nuggets"))
                .register();
        itemEntries.add(nugget);
        tagKeys.add(nuggetTag);

        var sheetTag = itemC("plates/" + id);
        if (sheet == null) sheet = REG.item(id + "_sheet", Item::new)
                .lang(type + " Sheet")
                .tag(sheetTag, itemC("plates"))
                .register();
        itemEntries.add(sheet);
        tagKeys.add(sheetTag);

        var gearTag = itemC("gears/" + id);
        if (gear == null) gear = REG.item(id + "_gear", Item::new)
                .lang(type + " Gear")
                .tag(gearTag, itemC("gears"))
                .register();
        itemEntries.add(gear);
        tagKeys.add(gearTag);

        var rodTag = itemC("rods/" + id);
        if (rod == null) rod = REG.item(id + "_rod", Item::new)
                .lang(type + " Rod")
                .model((c, p) -> p.withExistingParent(c.getId().getPath(),
                        ResourceLocation.withDefaultNamespace("item/handheld")).texture("layer0",
                        Unify.loc("item/" + c.getId().getPath())))
                .tag(rodTag, itemC("rods"))
                .register();
        itemEntries.add(rod);
        tagKeys.add(rodTag);

        var wireTag = itemC("wires/" + id);
        if (wire == null) wire = REG.item(id + "_wire", Item::new)
                .lang(type + " Wire")
                .tag(wireTag, itemC("wires"))
                .register();
        itemEntries.add(wire);
        tagKeys.add(wireTag);

        var dustTag = itemC("dusts/" + id);
        if (dust == null) dust = REG.item(id + "_dust", Item::new)
                .lang(type + " Dust")
                .tag(dustTag, itemC("dusts"))
                .register();
        itemEntries.add(dust);
        tagKeys.add(dustTag);

        var storageTag = itemC("storage_blocks/" + id);
        if (storage == null) storage = REG.block(id + "_block", Block::new)
                .lang(blockOf ? "Block of " + type : type + " Block")
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.requiresCorrectToolForDrops().mapColor(color).sound(sound))
                .transform(pickaxeOnly())
                .tag(beaconCompatible ? BlockTags.BEACON_BASE_BLOCKS : block("raw_beacon"))
                .tag(mineable != null ? mineable : BlockTags.NEEDS_STONE_TOOL)
                .tag(blockC("storage_blocks/" + id), blockC("storage_blocks"))
                .item()
                .tag(storageTag, itemC("storage_blocks"))
                .build()
                .register();
        blockEntries.add(storage);
        tagKeys.add(storageTag);

        return this;
    }
}
