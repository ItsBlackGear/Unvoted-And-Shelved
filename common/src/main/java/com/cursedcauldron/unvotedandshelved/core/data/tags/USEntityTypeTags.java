package com.cursedcauldron.unvotedandshelved.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class USEntityTypeTags {
    public static final TagRegistry<EntityType<?>> TAGS = TagRegistry.create(Registry.ENTITY_TYPE_REGISTRY, UnvotedAndShelved.MOD_ID);

    public static final TagKey<EntityType<?>> REACHABLE_BY_LIGHTNING = TAGS.register("reachable_by_lightning");
}