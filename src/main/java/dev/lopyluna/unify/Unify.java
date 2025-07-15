package dev.lopyluna.unify;

import com.mojang.logging.LogUtils;
import dev.lopyluna.unify.content.UnifyDatagen;
import dev.lopyluna.unify.content.utils.UnifyRegistration;
import dev.lopyluna.unify.content.utils.Registration;
import dev.lopyluna.unify.events.CommonEvents;
import dev.lopyluna.unify.register.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import static dev.lopyluna.unify.register.AllCreativeTab.BASE_TAB;

@SuppressWarnings("unused")
@Mod(Unify.MOD_ID)
public class Unify {
    public static final String MOD_ID = "unify";
    public static final String NAME = "Unify";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static UnifyRegistration REGISTER = new UnifyRegistration(MOD_ID);
    public static Registration REG = new Registration(MOD_ID);

    public Unify(IEventBus modEventBus, ModContainer modContainer) {
        REGISTER.register(modEventBus);
        AllCreativeTab.register();
        REG.registerEventListeners(modEventBus);
        AllSoundEvents.prepare();
        REG.defaultCreativeTab(BASE_TAB, "base_tab");

        AllMaterials.register();
        AllMenuTypes.register();
        AllTags.addGenerators();

        modEventBus.addListener(CommonEvents::commonSetup);
        modEventBus.addListener(AllCreativeTab::modifyTabContents);
        modEventBus.addListener(AllSoundEvents::register);
        modEventBus.addListener(EventPriority.HIGHEST, UnifyDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, UnifyDatagen::gatherData);
    }

    public static ResourceLocation loc(String loc) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, loc);
    }

    public static ResourceLocation empty() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "empty");
    }
}
