package dev.lopyluna.unify.mixin;

import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "defaultBlockState", at = @At("HEAD"), cancellable = true)
    private void unify$remapDefaultBlockState(CallbackInfoReturnable<BlockState> cir) {
        var self = (Block) (Object) this;
        var remapped = UnifyRemapper.remapBlock(self);
        if (!remapped.equals(self)) cir.setReturnValue(remapped.defaultBlockState());
    }
}
