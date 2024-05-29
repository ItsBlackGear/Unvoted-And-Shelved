package com.cursedcauldron.unvotedandshelved.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class USItemTags {
    public static final TagRegistry<Item> TAGS = TagRegistry.create(Registry.ITEM_REGISTRY, UnvotedAndShelved.MOD_ID);

    public static final TagKey<Item> COPPER_BUTTONS = TAGS.register("copper_buttons");
}