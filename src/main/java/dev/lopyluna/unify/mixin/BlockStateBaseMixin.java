package dev.lopyluna.unify.mixin;

import com.mojang.serialization.MapCodec;
import dev.lopyluna.unify.content.utils.UnifyRemapper;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin extends StateHolder<Block, BlockState> {

    protected BlockStateBaseMixin(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
        super(owner, values, propertiesCodec);
    }

    @Inject(method = "getBlock", at = @At("HEAD"), cancellable = true)
    private void unify$remapBlock(CallbackInfoReturnable<Block> cir) {
        var original = this.owner;
        var remapped = UnifyRemapper.remapBlock(original);
        if (!remapped.equals(original)) cir.setReturnValue(remapped);
    }
}
