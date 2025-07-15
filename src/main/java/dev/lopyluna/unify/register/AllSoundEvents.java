package dev.lopyluna.unify.register;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.lopyluna.unify.Unify;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class AllSoundEvents {
    public static final Map<ResourceLocation, SoundEntry> ALL = new HashMap<>();

    //public static final BlockSoundProvider SILT = new BlockSoundProvider("silt",
    //        "break", 4,
    //        "step", 5,
    //        "break", 4,
    //        "step", 5,
    //        "step", 5
    //);

    public static SoundType METAL_HEAVY = new DeferredSoundType(0.9f, 0.5F,
            () -> SoundEvents.POLISHED_DEEPSLATE_BREAK,
            () -> SoundEvents.POLISHED_DEEPSLATE_STEP,
            () -> SoundEvents.POLISHED_DEEPSLATE_PLACE,
            () -> SoundEvents.POLISHED_DEEPSLATE_HIT,
            () -> SoundEvents.POLISHED_DEEPSLATE_FALL
    );

    public static SoundType NETHERITE_HEAVY = new DeferredSoundType(0.9f, 0.5F,
            () -> SoundEvents.NETHERITE_BLOCK_BREAK,
            () -> SoundEvents.NETHERITE_BLOCK_STEP,
            () -> SoundEvents.NETHERITE_BLOCK_PLACE,
            () -> SoundEvents.NETHERITE_BLOCK_HIT,
            () -> SoundEvents.NETHERITE_BLOCK_FALL
    );

    public static SoundType COPPER_HEAVY = new DeferredSoundType(0.9f, 0.5F,
            () -> SoundEvents.COPPER_BREAK,
            () -> SoundEvents.COPPER_STEP,
            () -> SoundEvents.COPPER_PLACE,
            () -> SoundEvents.COPPER_HIT,
            () -> SoundEvents.COPPER_FALL
    );

    //Mostly Copied from Create

    private static SoundEntryBuilder create(String name) {
        return create(Unify.loc(name));
    }

    public static SoundEntryBuilder create(ResourceLocation id) {
        return new SoundEntryBuilder(id);
    }

    public static void prepare() {
        for (SoundEntry entry : ALL.values()) entry.prepare();
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> ALL.values().forEach(entry -> entry.register(helper)));
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (SoundEntry entry : ALL.values())
            if (entry.hasSubtitle()) consumer.accept(entry.getSubtitleKey(), entry.getSubtitle());
    }

    public static SoundEntryProvider provider(DataGenerator generator) {
        return new SoundEntryProvider(generator);
    }

    public static void playItemPickup(Player player) {
        var level = player.level();
        level.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f, 1f + level.random.nextFloat());
    }

    public static SoundEntry createBlock(String name, String type, String id, int var) {
        var builder = create(name + "_" + type).noSubtitle();
        for (var i = 0; (Math.max(var, 0)) > i; i++)
            builder = builder.addVariant(name + "/" + id + (var - 1 == 0 ? "" : i + 1));
        return builder.category(SoundSource.BLOCKS).build();
    }

    public static class BlockSoundProvider {
        public final SoundEntry BREAK;
        public final SoundEntry STEP;
        public final SoundEntry PLACE;
        public final SoundEntry HIT;
        public final SoundEntry FALL;
        private final DeferredSoundType soundType;

        public BlockSoundProvider(String name, String bID, int bVar, String sID, int sVar, String pID, int pVar, String hID, int hVar, String fID, int fVar) {
            BREAK = createBlock(name, "break", bID, bVar);
            STEP = createBlock(name, "step", sID, sVar);
            PLACE = createBlock(name, "place", pID, pVar);
            HIT = createBlock(name, "hit", hID, hVar);
            FALL = createBlock(name, "fall", fID, fVar);
            soundType = new DeferredSoundType(
                    0.9F,
                    1.0F,
                    BREAK::getMainEvent,
                    STEP::getMainEvent,
                    PLACE::getMainEvent,
                    HIT::getMainEvent,
                    FALL::getMainEvent
            );
        }

        public DeferredSoundType get() {
            return soundType;
        }
    }

    public static class SoundEntryProvider implements DataProvider {

        private final PackOutput output;

        public SoundEntryProvider(DataGenerator generator) {
            output = generator.getPackOutput();
        }

        @Override
        public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
            return generate(output.getOutputFolder(), cache);
        }

        @Override
        public @NotNull String getName() {
            return "Unify's Custom Sounds";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            path = path.resolve("assets/unify");
            JsonObject json = new JsonObject();
            ALL.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> entry.getValue().write(json));
            return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
        }

    }

    public record ConfiguredSoundEvent(Supplier<SoundEvent> event, float volume, float pitch) {
    }

    public static class SoundEntryBuilder {
        protected ResourceLocation id;
        protected String subtitle = "unregistered";
        protected SoundSource category = SoundSource.BLOCKS;
        protected List<ConfiguredSoundEvent> wrappedEvents;
        protected List<ResourceLocation> variants;
        protected int attenuationDistance;

        public SoundEntryBuilder(ResourceLocation id) {
            wrappedEvents = new ArrayList<>();
            variants = new ArrayList<>();
            this.id = id;
        }

        public SoundEntryBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public SoundEntryBuilder attenuationDistance(int distance) {
            this.attenuationDistance = distance;
            return this;
        }

        public SoundEntryBuilder noSubtitle() {
            this.subtitle = null;
            return this;
        }

        public SoundEntryBuilder category(SoundSource category) {
            this.category = category;
            return this;
        }

        public SoundEntryBuilder addVariant(String name) {
            return addVariant(Unify.loc(name));
        }

        public SoundEntryBuilder addVariant(ResourceLocation id) {
            variants.add(id);
            return this;
        }

        public SoundEntryBuilder playExisting(Supplier<SoundEvent> event, float volume, float pitch) {
            wrappedEvents.add(new ConfiguredSoundEvent(event, volume, pitch));
            return this;
        }

        public SoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
            return playExisting(() -> event, volume, pitch);
        }

        public SoundEntryBuilder playExisting(SoundEvent event) {
            return playExisting(event, 1, 1);
        }

        public SoundEntryBuilder playExisting(Holder<SoundEvent> event) {
            return playExisting(event::value, 1, 1);
        }

        public SoundEntry build() {
            var entry = wrappedEvents.isEmpty() ? new CustomSoundEntry(id, variants, subtitle, category, attenuationDistance) : new WrappedSoundEntry(id, subtitle, wrappedEvents, category, attenuationDistance);
            ALL.put(entry.getId(), entry);
            return entry;
        }

    }

    public static abstract class SoundEntry {
        protected ResourceLocation id;
        protected String subtitle;
        protected SoundSource category;
        protected int attenuationDistance;

        public SoundEntry(ResourceLocation id, String subtitle, SoundSource category, int attenuationDistance) {
            this.id = id;
            this.subtitle = subtitle;
            this.category = category;
            this.attenuationDistance = attenuationDistance;
        }

        public abstract void prepare();

        public abstract void register(RegisterEvent.RegisterHelper<SoundEvent> registry);

        public abstract void write(JsonObject json);

        public abstract Holder<SoundEvent> getMainEventHolder();

        public abstract SoundEvent getMainEvent();

        public String getSubtitleKey() {
            return id.getNamespace() + ".subtitle." + id.getPath();
        }

        public ResourceLocation getId() {
            return id;
        }

        public boolean hasSubtitle() {
            return subtitle != null;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void playOnServer(Level world, Vec3i pos) {
            playOnServer(world, pos, 1, 1);
        }

        public void playOnServer(Level world, Vec3i pos, float volume, float pitch) {
            play(world, null, pos, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3i pos) {
            play(world, entity, pos, 1, 1);
        }

        public void playFrom(Entity entity) {
            playFrom(entity, 1, 1);
        }

        public void playFrom(Entity entity, float volume, float pitch) {
            if (!entity.isSilent())
                play(entity.level(), null, entity.blockPosition(), volume, pitch);
        }

        public void play(Level world, Player entity, Vec3i pos, float volume, float pitch) {
            play(world, entity, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3 pos, float volume, float pitch) {
            play(world, entity, pos.x(), pos.y(), pos.z(), volume, pitch);
        }

        public abstract void play(Level world, Player entity, double x, double y, double z, float volume, float pitch);

        public void playAt(Level world, Vec3i pos, float volume, float pitch, boolean fade) {
            playAt(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, volume, pitch, fade);
        }

        public void playAt(Level world, Vec3 pos, float volume, float pitch, boolean fade) {
            playAt(world, pos.x(), pos.y(), pos.z(), volume, pitch, fade);
        }

        public abstract void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade);

    }

    private static class WrappedSoundEntry extends SoundEntry {
        private final List<ConfiguredSoundEvent> wrappedEvents;
        private final List<CompiledSoundEvent> compiledEvents;

        public WrappedSoundEntry(ResourceLocation id, String subtitle, List<ConfiguredSoundEvent> wrappedEvents, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.wrappedEvents = wrappedEvents;
            compiledEvents = new ArrayList<>();
        }

        @Override
        public void prepare() {
            for (int i = 0; i < wrappedEvents.size(); i++) {
                ConfiguredSoundEvent wrapped = wrappedEvents.get(i);
                ResourceLocation location = getIdOf(i);
                DeferredHolder<SoundEvent, SoundEvent> event = DeferredHolder.create(Registries.SOUND_EVENT, location);
                compiledEvents.add(new CompiledSoundEvent(event, wrapped.volume(), wrapped.pitch()));
            }
        }

        @Override
        public void register(RegisterEvent.RegisterHelper<SoundEvent> helper) {
            for (CompiledSoundEvent compiledEvent : compiledEvents) {
                ResourceLocation location = compiledEvent.event().getId();
                helper.register(location, SoundEvent.createVariableRangeEvent(location));
            }
        }

        @Override
        public Holder<SoundEvent> getMainEventHolder() {
            return compiledEvents.getFirst().event();
        }

        @Override
        public SoundEvent getMainEvent() {
            return compiledEvents.getFirst().event().get();
        }

        protected ResourceLocation getIdOf(int i) {
            return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), i == 0 ? id.getPath() : id.getPath() + "_compounded_" + i);
        }

        @Override
        public void write(JsonObject json) {
            for (int i = 0; i < wrappedEvents.size(); i++) {
                ConfiguredSoundEvent event = wrappedEvents.get(i);
                JsonObject entry = new JsonObject();
                JsonArray list = new JsonArray();
                JsonObject s = new JsonObject();
                s.addProperty("name", event.event().get().getLocation().toString());
                s.addProperty("type", "event");
                if (attenuationDistance != 0) s.addProperty("attenuation_distance", attenuationDistance);
                list.add(s);
                entry.add("sounds", list);
                if (i == 0 && hasSubtitle()) entry.addProperty("subtitle", getSubtitleKey());
                json.add(getIdOf(i).getPath(), entry);
            }
        }

        @Override
        public void play(Level level, Player player, double x, double y, double z, float volume, float pitch) {
            for (CompiledSoundEvent event : compiledEvents)
                level.playSound(player, x, y, z, event.event().get(), category, event.volume() * volume, event.pitch() * pitch);
        }

        @Override
        public void playAt(Level level, double x, double y, double z, float volume, float pitch, boolean fade) {
            for (CompiledSoundEvent event : compiledEvents)
                level.playLocalSound(x, y, z, event.event().get(), category, event.volume() * volume, event.pitch() * pitch, fade);
        }

        private record CompiledSoundEvent(DeferredHolder<SoundEvent, SoundEvent> event, float volume, float pitch) {
        }
    }

    private static class CustomSoundEntry extends SoundEntry {
        protected List<ResourceLocation> variants;
        protected DeferredHolder<SoundEvent, SoundEvent> event;

        public CustomSoundEntry(ResourceLocation id, List<ResourceLocation> variants, String subtitle, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.variants = variants;
        }

        @Override
        public void prepare() {
            event = DeferredHolder.create(Registries.SOUND_EVENT, id);
        }

        @Override
        public void register(RegisterEvent.RegisterHelper<SoundEvent> helper) {
            ResourceLocation location = event.getId();
            helper.register(location, SoundEvent.createVariableRangeEvent(location));
        }

        @Override
        public Holder<SoundEvent> getMainEventHolder() {
            return event;
        }

        @Override
        public SoundEvent getMainEvent() {
            return event.get();
        }

        @Override
        public void write(JsonObject json) {
            JsonObject entry = new JsonObject();
            JsonArray list = new JsonArray();
            JsonObject s;
            for (ResourceLocation variant : variants) {
                s = new JsonObject();
                s.addProperty("name", variant.toString());
                s.addProperty("type", "file");
                if (attenuationDistance != 0) s.addProperty("attenuation_distance", attenuationDistance);
                list.add(s);
            }
            entry.add("sounds", list);
            if (hasSubtitle()) entry.addProperty("subtitle", getSubtitleKey());
            json.add(id.getPath(), entry);
        }

        @Override
        public void play(Level level, Player player, double x, double y, double z, float volume, float pitch) {
            level.playSound(player, x, y, z, event.get(), category, volume, pitch);
        }

        @Override
        public void playAt(Level level, double x, double y, double z, float volume, float pitch, boolean fade) {
            level.playLocalSound(x, y, z, event.get(), category, volume, pitch, fade);
        }

    }
}
