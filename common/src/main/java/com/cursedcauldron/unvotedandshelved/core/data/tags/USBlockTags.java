package com.cursedcauldron.unvotedandshelved.core.data.tags;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.common.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class USBlockTags {
    public static final TagRegistry<Block> TAGS = TagRegistry.of(Registry.BLOCK_REGISTRY, UnvotedAndShelved.MOD_ID);

    public static final TagKey<Block> COPPER_BUTTONS = TAGS.create("copper_buttons");
}