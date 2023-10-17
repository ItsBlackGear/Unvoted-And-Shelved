package com.cursedcauldron.unvotedandshelved.core.data.tags;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.common.TagRegistry;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class USItemTags {
    public static final TagRegistry<Item> TAGS = TagRegistry.of(Registry.ITEM_REGISTRY, UnvotedAndShelved.MOD_ID);

    public static final TagKey<Item> COPPER_BUTTONS = TAGS.create("copper_buttons");
}