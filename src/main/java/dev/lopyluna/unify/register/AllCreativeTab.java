package dev.lopyluna.unify.register;

import dev.lopyluna.unify.Unify;
import dev.lopyluna.unify.content.utils.UnifyRemapper;
import dev.lopyluna.unify.helper.MetalOreEntries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import static dev.lopyluna.unify.Unify.REGISTER;
import static dev.lopyluna.unify.config.CommonConfig.DISPLAY_ALL_ITEMS;

public class AllCreativeTab {
    public static void modifyTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!DISPLAY_ALL_ITEMS.get()) for (var entry : Unify.REG.getAll(Registries.ITEM)) {
            var item = entry.get();
            if (UnifyRemapper.ITEM_DISPLAY.contains(item)) continue;
            event.remove(item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        //for (var item : UnifyRemapper.ITEM_HIDDEN) event.remove(item.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_TAB = REGISTER.creativeTab().register("base_tab", () -> CreativeModeTab.builder()
            .title(Component.translatableWithFallback("itemGroup." + Unify.MOD_ID + ".base", Unify.NAME))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(MetalOreEntries.TIN.entry.ingot.get()::getDefaultInstance)
            .build());

    public static void register() {
    }
}
