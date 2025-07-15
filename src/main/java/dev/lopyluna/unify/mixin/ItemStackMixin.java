package dev.lopyluna.unify.mixin;

import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    @Final
    @Nullable
    private Item item;

    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    private void unify$remapGetItem(CallbackInfoReturnable<Item> cir) {
        if (this.isEmpty()) {
            cir.setReturnValue(Items.AIR);
        } else {
            var remapped = UnifyRemapper.remapItem(this.item);
            cir.setReturnValue(remapped);
        }
    }
}
