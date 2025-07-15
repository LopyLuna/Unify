package dev.lopyluna.unify.content;

import com.tterrag.registrate.providers.ProviderType;
import dev.lopyluna.unify.Unify;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.BiConsumer;

public class UnifyDatagen {
    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(Unify.MOD_ID)) addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(Unify.MOD_ID)) return;
        var generator = event.getGenerator();
        generator.addProvider(event.includeClient(), dev.lopyluna.unify.register.AllSoundEvents.provider(generator));

    }

    private static void addExtraRegistrateData() {
        Unify.REG.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            dev.lopyluna.unify.register.AllSoundEvents.provideLang(langConsumer);
        });
    }
}
