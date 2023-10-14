package com.groupid.examplemod.core.platform.common;

import com.google.common.collect.Maps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;

public class IntegrationRegistry {
    public static void flammable(Block block, int flameOdds, int burnOdds) {
        ((FireBlock)Blocks.FIRE).setFlammable(block, flameOdds, burnOdds);
    }

    public static void compostable(ItemLike entry, float chance) {
        ComposterBlock.COMPOSTABLES.put(entry.asItem(), chance);
    }

    public static void strippable(Block source, Block target) {
        AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
        AxeItem.STRIPPABLES.put(source, target);
    }

    @ExpectPlatform
    public static void interaction(Interaction interaction) {
        throw new AssertionError();
    }

    public interface Interaction {
        InteractionResult of(UseOnContext context);
    }
}