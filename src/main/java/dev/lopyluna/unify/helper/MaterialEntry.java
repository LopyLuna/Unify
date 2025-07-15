package dev.lopyluna.unify.helper;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialEntry {
    public static List<String> types = new ArrayList<>();
    public static List<MaterialEntry> entries = new ArrayList<>();
    public static Map<String, MaterialEntry> typeEntries = new HashMap<>();

    public final String id;
    public final String type;
    public final MapColor color;
    public final SoundType sound;

    public MaterialEntry(String name, MapColor color, SoundType sound) {
        this.id = name.toLowerCase().replace(" ", "_");
        this.type = name;
        this.color = color;
        this.sound = sound;
        types.add(id);
        typeEntries.put(id, this);
        entries.add(this);
    }

    public MaterialEntry register() {
        return this;
    }
}
