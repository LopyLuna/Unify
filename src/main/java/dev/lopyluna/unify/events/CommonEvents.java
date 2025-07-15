package dev.lopyluna.unify.events;

import dev.lopyluna.unify.content.utils.UnifyRemapper;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@SuppressWarnings("unused")
public class CommonEvents {
    public static void commonSetup(final FMLCommonSetupEvent event) {
        UnifyRemapper.register();
    }
}
