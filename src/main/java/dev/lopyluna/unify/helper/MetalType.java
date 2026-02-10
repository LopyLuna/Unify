package dev.lopyluna.unify.helper;

import java.util.List;

import static dev.lopyluna.unify.content.AllUtils.array;

public enum MetalType {
    INGOT("ingot", "bar"),
    NUGGET("nugget"),
    SHEET("plate", "sheet"),
    GEAR("gear"),
    WIRE("wire"),
    ROD("rod"),
    BOLT("bolt", "nail"),
    DUST("dust", "crushed"),
    GRIT("tiny_dust", "dust_tiny", "grit", "tiny_{}_dust", "dust_{}_tiny"),
    STORAGE("storage", "block", "storage_block", "block_storage", "storage_{}_block", "block_{}_storage"),
    CASING("casing"),

    RAW_CHUNK("raw", "raw_chunk", "chunk_raw"),
    RAW_NUGGET("nugget_raw", "raw_nugget", "raw_{}_nugget", "nugget_{}_raw"),
    RAW_CRUSHED("crushed_raw", "raw_crushed", "dust_raw", "raw_dust", "raw_{}_crushed", "crushed_{}_raw", "raw_{}_dust", "dust_{}_raw"),
    RAW_KERN("kern_raw", "raw_kern", "raw_{}_kern", "kern_{}_raw", "raw_tiny_dust", "raw_dust_tiny", "tiny_dust_raw", "dust_tiny_raw", "raw_grit", "grit_raw", "tiny_{}_raw_dust", "dust_{}_raw_tiny", "tiny_raw_{}_dust", "dust_raw_{}_tiny", "raw_tiny_{}_dust", "raw_dust_{}_tiny", "tiny_{}_dust_raw", "dust_{}_tiny_raw"),
    RAW_STORAGE("raw_storage", "raw_block", "raw_storage_block", "raw_block_storage", "raw_storage_{}_block", "raw_block_{}_storage", "storage_raw", "block_raw", "storage_block_raw", "block_storage_raw", "storage_{}_block_raw", "block_{}_storage_raw", "storage_raw_block", "block_raw_storage", "storage_{}_raw_block", "block_{}_raw_storage", "storage_raw_{}_block", "block_raw_{}_storage"),

    ORE_STONE("ore", "stone_ore", "ore_stone", "ore_{}_stone", "stone_{}_ore"),
    ORE_DEEP("deepslate_ore", "ore_deepslate", "ore_{}_deepslate", "deepslate_{}_ore"),
    ORE_NETHER("nether_ore", "ore_nether", "ore_{}_nether", "nether_{}_ore"),
    ORE_END("end_ore", "ore_end", "ore_{}_end", "end_{}_ore"),

    MOLTEN("molten"),

    NA();

    public final List<String> targets;
    MetalType(String... targets) {
        this.targets = array(targets);
    }
}
