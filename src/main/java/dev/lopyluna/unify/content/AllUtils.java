package dev.lopyluna.unify.content;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.lopyluna.unify.Unify;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

@SuppressWarnings("unused")
public class AllUtils {

    public static ResourceLocation inputFromResult(ItemLike input, ItemLike result) {
        return Unify.loc(safeId(result) + "_from_" + safeId(input));
    }

    public static String safeFullId(ItemLike registryEntry) {
        return safeFullName(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(registryEntry.asItem())));
    }

    public static String safeId(ItemLike registryEntry) {
        return safeName(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(registryEntry.asItem())));
    }

    public static String safeName(ResourceLocation id) {
        return id.getPath().replace('/', '_');
    }

    public static String safeFullName(ResourceLocation id) {
        return id.getNamespace() + ":" + id.getPath().replace('/', '_');
    }

    public static Item getResolvedItem(ResourceLocation loc) {
        var item = BuiltInRegistries.ITEM.get(loc);
        if (item == Items.AIR) System.out.println("⚠ Item not yet registered: " + loc + " is " + item);
        return item;
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> blockTags(NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> tags) {
        return tags;
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> pickaxeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> shovelOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> hoeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_HOE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> swordOnly() {
        return b -> b.tag(BlockTags.SWORD_EFFICIENT);
    }
}
