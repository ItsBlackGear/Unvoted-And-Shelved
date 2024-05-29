package com.cursedcauldron.unvotedandshelved.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.item.GlowberryDustBottleItem;
import com.cursedcauldron.unvotedandshelved.common.item.OxidizedCopperGolemItem;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Supplier;

public class USItems {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.create(Registry.ITEM, UnvotedAndShelved.MOD_ID);

    public static final Supplier<Item> GLOWBERRY_DUST_BOTTLE = ITEMS.register(
        "glowberry_dust_bottle",
        () -> new GlowberryDustBottleItem(
            USBlocks.GLOWBERRY_DUST.get(),
            new Properties()
        ));
    public static final Supplier<Item> OXIDIZED_COPPER_GOLEM = ITEMS.register(
        "oxidized_copper_golem",
        () -> new OxidizedCopperGolemItem(
            new Properties()
        ));
}