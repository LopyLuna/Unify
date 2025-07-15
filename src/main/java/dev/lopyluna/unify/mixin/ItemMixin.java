package dev.lopyluna.unify.mixin;

import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getDefaultInstance", at = @At("RETURN"), cancellable = true)
    private void unify$remapDefaultInstance(CallbackInfoReturnable<ItemStack> cir) {
        var original = cir.getReturnValue();
        var remapped = UnifyRemapper.remapItemStack(original);
        cir.setReturnValue(remapped);
    }
}
