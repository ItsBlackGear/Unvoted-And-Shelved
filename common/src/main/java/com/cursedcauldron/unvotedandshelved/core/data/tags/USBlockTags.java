package com.cursedcauldron.unvotedandshelved.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class USBlockTags {
    public static final TagRegistry<Block> TAGS = TagRegistry.create(Registry.BLOCK_REGISTRY, UnvotedAndShelved.MOD_ID);

    public static final TagKey<Block> COPPER_GOLEM_INTERACTABLES = TAGS.register("copper_golem_interactables");
    public static final TagKey<Block> COPPER_BUTTONS = TAGS.register("copper_buttons");
    public static final TagKey<Block> COPPER_PILLARS = TAGS.register("copper_pillars");
}