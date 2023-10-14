package com.cursedcauldron.unvotedandshelved.core.platform.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface BiomeContext {
    boolean is(TagKey<Biome> tag);

    boolean is(ResourceKey<Biome> biome);
}