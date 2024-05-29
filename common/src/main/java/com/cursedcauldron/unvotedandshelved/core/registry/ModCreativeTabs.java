package com.cursedcauldron.unvotedandshelved.core.registry;

import com.blackgear.platform.common.CreativeTabs;
import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.registries.USItems;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCreativeTabs {
    public static final CreativeModeTab UNVOTED_AND_SHELVED = CreativeTabs.create(
        new ResourceLocation(UnvotedAndShelved.MOD_ID, UnvotedAndShelved.MOD_ID),
        () -> new ItemStack(USItems.OXIDIZED_COPPER_GOLEM.get()),
        stacks -> {
            stacks.add(new ItemStack(USItems.OXIDIZED_COPPER_GOLEM.get()));
            stacks.add(new ItemStack(USItems.GLOWBERRY_DUST_BOTTLE.get()));
            stacks.add(new ItemStack(USBlocks.COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.EXPOSED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.WEATHERED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.OXIDIZED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get()));
            stacks.add(new ItemStack(USBlocks.COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.EXPOSED_COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.WEATHERED_COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.OXIDIZED_COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get()));
            stacks.add(new ItemStack(USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()));
        }
    );
    
    public static void bootstrap() {
        CreativeTabs.modify(output -> {
            output.addAfter(Items.ARMOR_STAND, USItems.OXIDIZED_COPPER_GOLEM.get());
            output.addBefore(Items.LANTERN, USItems.GLOWBERRY_DUST_BOTTLE.get());
            output.addAfter(
                Items.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                ImmutableList.of(
                    USBlocks.COPPER_PILLAR.get(),
                    USBlocks.EXPOSED_COPPER_PILLAR.get(),
                    USBlocks.WEATHERED_COPPER_PILLAR.get(),
                    USBlocks.OXIDIZED_COPPER_PILLAR.get(),
                    USBlocks.WAXED_COPPER_PILLAR.get(),
                    USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get(),
                    USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get(),
                    USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get()
                )
            );
            output.addAfter(
                Items.WARPED_BUTTON,
                ImmutableList.of(
                    USBlocks.COPPER_BUTTON.get(),
                    USBlocks.EXPOSED_COPPER_BUTTON.get(),
                    USBlocks.WEATHERED_COPPER_BUTTON.get(),
                    USBlocks.OXIDIZED_COPPER_BUTTON.get(),
                    USBlocks.WAXED_COPPER_BUTTON.get(),
                    USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(),
                    USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(),
                    USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()
                )
            );
        });
    }
}