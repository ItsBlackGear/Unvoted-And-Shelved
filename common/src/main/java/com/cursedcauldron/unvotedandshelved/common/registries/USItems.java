package com.cursedcauldron.unvotedandshelved.common.registries;

import com.cursedcauldron.unvotedandshelved.common.item.OxidizedCopperGolemItem;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class USItems {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.create(Registry.ITEM, UnvotedAndShelved.MOD_ID);

    public static final Supplier<Item> OXIDIZED_COPPER_GOLEM = create("oxidized_copper_golem", () -> new OxidizedCopperGolemItem(new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    private static <T extends Item> Supplier<T> create(String key, Supplier<T> item) {
        return ITEMS.register(key, item);
    }
}