package dev.lopyluna.unify.events;

import dev.lopyluna.unify.Unify;
import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Unify.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerEvents {

    @SubscribeEvent
    public static void onServerLoad(ServerAboutToStartEvent event) {
        var server = event.getServer();
        var prov = server.registryAccess();
        UnifyRemapper.register(prov);
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        var server = event.getServer();
        var prov = server.registryAccess();
        UnifyRemapper.register(prov);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        var server = event.getServer();
        var prov = server.registryAccess();
        UnifyRemapper.register(prov);
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        var entity = event.getEntity();
        var level = entity.level();
        if (!level.isClientSide) {
            if (entity instanceof ItemEntity itemEntity && entity.tickCount % 4 == 0) {
                var itemStack = itemEntity.getItem();
                var newStack = UnifyRemapper.remapItemStack(itemStack);
                if (!itemStack.equals(newStack)) itemEntity.setItem(newStack);
            } else if (entity instanceof Player player) {
                if (entity.tickCount % 4 == 0) {
                    var inv = player.hasContainerOpen() ? player.containerMenu : player.inventoryMenu;
                    var carried = inv.getCarried();
                    var newCarried = UnifyRemapper.remapItemStack(carried);
                    if (!carried.equals(newCarried)) inv.setCarried(newCarried);
                    for (var slot : inv.slots) {
                        var itemStack = slot.getItem();
                        var newStack = UnifyRemapper.remapItemStack(itemStack);
                        if (!itemStack.equals(newStack)) slot.set(newStack);
                    }
                }
                if (entity.tickCount % 2 == 0) {
                    var vec3 = player.getEyePosition();
                    var vec31 = vec3.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(64));
                    var hit = level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
                    if (hit.getType() != HitResult.Type.MISS) {
                        var hitPos = hit.getBlockPos();
                        for (var pos : BlockPos.betweenClosed(hitPos.offset(1, 1, 1), hitPos.offset(-1, -1, -1))) {
                            var state = level.getBlockState(pos);
                            var newState = UnifyRemapper.remapBlockState(state);
                            if (!state.equals(newState)) level.setBlock(pos, newState, 2);
                        }
                    }
                }
            }
        }
    }
}
