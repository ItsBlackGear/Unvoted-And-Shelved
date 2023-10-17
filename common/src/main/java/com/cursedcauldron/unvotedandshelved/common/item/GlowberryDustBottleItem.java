package com.cursedcauldron.unvotedandshelved.common.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class GlowberryDustBottleItem extends BlockItem {
    public GlowberryDustBottleItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        Player player = context.getPlayer();
        if (result.consumesAction() && player != null && !player.isCreative()) {
            player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        }

        return result;
    }
}