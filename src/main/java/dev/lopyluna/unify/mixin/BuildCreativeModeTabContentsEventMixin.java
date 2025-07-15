package dev.lopyluna.unify.mixin;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuildCreativeModeTabContentsEvent.class)
public class BuildCreativeModeTabContentsEventMixin {

    @Inject(method = "accept", at = @At("HEAD"), cancellable = true)
    private void unify$preventDuplicateItemStacks(ItemStack newEntry, CreativeModeTab.TabVisibility visibility, CallbackInfo ci) {
        var self = (BuildCreativeModeTabContentsEvent) (Object) this;
        var list = visibility == CreativeModeTab.TabVisibility.PARENT_TAB_ONLY || visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS ? self.getParentEntries() : self.getSearchEntries();
        if (list.contains(newEntry)) ci.cancel();
    }
}
