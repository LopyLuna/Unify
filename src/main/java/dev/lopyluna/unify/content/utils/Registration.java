package dev.lopyluna.unify.content.utils;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import dev.lopyluna.unify.register.AllTags;

public class Registration extends AbstractRegistrate<Registration> {
    public Registration(String modid) {
        super(modid);
    }

    public void generateTags() {
        addDataGenerator(ProviderType.BLOCK_TAGS, AllTags::genBlockTags);
        addDataGenerator(ProviderType.ITEM_TAGS, AllTags::genItemTags);
    }
}