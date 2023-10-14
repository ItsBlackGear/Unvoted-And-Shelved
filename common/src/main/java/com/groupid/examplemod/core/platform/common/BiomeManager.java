package com.groupid.examplemod.core.platform.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.BiConsumer;

public class BiomeManager {
    private static final List<BiConsumer<BiomeWriter, BiomeContext>> MODIFICATIONS = Lists.newArrayList();

    public static final BiomeManager INSTANCE = new BiomeManager();

    @ExpectPlatform
    public static void bootstrap() {
        throw new AssertionError();
    }

    public void register(BiomeWriter writer) {
        MODIFICATIONS.forEach(writer::add);
    }

    public static void add(BiConsumer<BiomeWriter, BiomeContext> modifier) {
        MODIFICATIONS.add(modifier);
    }
}