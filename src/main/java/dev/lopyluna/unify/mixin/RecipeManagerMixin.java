package dev.lopyluna.unify.mixin;

import com.google.gson.JsonElement;
import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void injectAfterApply(Map<ResourceLocation, JsonElement> jsons, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        var outputs = List.of("result", "item_output", "secondary", "output", "results", "item_outputs", "secondaries", "outputs", "value", "values");
        var inputs = List.of(
                "ingredient", "input0", "input1", "input2", "input3",
                "extra_input", "main_input", "item_inputs", "item_input",
                "input", "additive", "addition", "base", "ingredients",
                "extra_inputs", "main_inputs", "inputs", "additives",
                "additions", "catalyst", "catalysts"
        );
        var inner = List.of("basePredicate", "output", "value");

        for (var entry : jsons.entrySet()) {
            var json = entry.getValue();
            if (!json.isJsonObject()) continue;
            var obj = json.getAsJsonObject();

            UnifyRemapper.handleRemapping(entry, json, obj, inputs, inner);
            UnifyRemapper.handleRemapping(entry, json, obj, outputs, inner);
        }
    }
}
