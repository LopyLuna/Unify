package dev.lopyluna.unify.mixin;

import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin {
    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"), cancellable = true)
    private void onSetBlockState(int x, int y, int z, BlockState state, boolean useLocks, CallbackInfoReturnable<BlockState> cir) {
        var self = (LevelChunkSection) (Object) this;
        var remapped = UnifyRemapper.remapBlockState(state);
        if (!remapped.equals(state)) cir.setReturnValue(self.setBlockState(x, y, z, remapped, useLocks));
    }
}
