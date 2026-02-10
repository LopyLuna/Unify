package dev.lopyluna.unify.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class CommonConfig {
    static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static final Supplier<Boolean> DISPLAY_ALL_ITEMS = create("Show all unify items in the creative menu", "DisplayAllItems", false);
    //public static final Supplier<Boolean> DISABLE_RECIPE_REMAP = create("Disable the Recipe Remapping", "DisableRecipeRemap", false);
    //public static final Supplier<Boolean> DISABLE_ITEM_REMAP = create("Disable the Item Remapping", "DisableItemRemap", false);
    //public static final Supplier<Boolean> DISABLE_BLOCK_REMAP = create("Disable the Block Remapping", "DisableBlockRemap", false);

    public static final ModConfigSpec SPEC = builder.build();

    @SuppressWarnings("unchecked")
    public static <T> Supplier<T> create(String comment, String title, T value, T min, T max) {
        if (value instanceof Integer) return (ModConfigSpec.ConfigValue<T>) builder.comment(comment + " (default = " + value + ")")
                .defineInRange(title, (int) value, (int) min, (int) max);
        if (value instanceof Long) return (ModConfigSpec.ConfigValue<T>) builder.comment(comment + " (default = " + value + ")")
                .defineInRange(title, (long) value, (long) min, (long) max);
        if (value instanceof Float) return (ModConfigSpec.ConfigValue<T>) builder.comment(comment + " (default = " + value + ")")
                .defineInRange(title, (float) value, (float) min, (float) max);
        if (value instanceof Double) return (ModConfigSpec.ConfigValue<T>) builder.comment(comment + " (default = " + value + ")")
                .defineInRange(title, (double) value, (double) min, (double) max);
        return builder.comment(comment + " (default = " + value + ")")
                .define(title, value);
    }

    public static <T> Supplier<T> create(String comment, String title, T value) {
        if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) return () -> value;
        return builder.comment(comment + " (default = " + value + ")")
                .define(title, value);
    }
}
