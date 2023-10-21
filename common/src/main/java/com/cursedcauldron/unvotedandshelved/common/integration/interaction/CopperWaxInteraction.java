package com.cursedcauldron.unvotedandshelved.common.integration.interaction;

import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.block.resource.USWeatheringCopper;
import com.cursedcauldron.unvotedandshelved.core.platform.common.IntegrationRegistry;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class CopperWaxInteraction implements IntegrationRegistry.Interaction {
    public static final Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(USBlocks.COPPER_BUTTON.get(), USBlocks.WAXED_COPPER_BUTTON.get())
                .put(USBlocks.EXPOSED_COPPER_BUTTON.get(), USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get())
                .put(USBlocks.WEATHERED_COPPER_BUTTON.get(), USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get())
                .put(USBlocks.OXIDIZED_COPPER_BUTTON.get(), USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get())
                .put(USBlocks.COPPER_PILLAR.get(), USBlocks.WAXED_COPPER_PILLAR.get())
                .put(USBlocks.EXPOSED_COPPER_PILLAR.get(), USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get())
                .put(USBlocks.WEATHERED_COPPER_PILLAR.get(), USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get())
                .put(USBlocks.OXIDIZED_COPPER_PILLAR.get(), USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK = Suppliers.memoize(() -> WAXABLES.get().inverse());

    @Override
    public InteractionResult of(UseOnContext context) {
        ItemStack stack = context.getItemInHand();

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        // Waxing on with Honeycomb
        if (stack.is(Items.HONEYCOMB)) {
            return getWaxed(state).map(waxed -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }

                stack.shrink(1);
                level.setBlock(pos, waxed, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, waxed));
                level.levelEvent(player, 3003, pos, 0);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }).orElse(InteractionResult.PASS);
        }

        if (stack.getItem() instanceof AxeItem) {
            Optional<BlockState> previousState = USWeatheringCopper.getPrevious(state);
            Optional<BlockState> waxedOffState = Optional.ofNullable(WAX_OFF_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));

            Optional<BlockState> finalState = Optional.empty();

            if (previousState.isPresent()) {
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, pos, 0);
                finalState = previousState;
            } else if (waxedOffState.isPresent()) {
                level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3004, pos, 0);
                finalState = waxedOffState;
            }

            if (finalState.isPresent()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }

                level.setBlock(pos, finalState.get(), 11);
                if (player != null) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    return InteractionResult.PASS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    public static Optional<BlockState> getWaxed(BlockState state) {
        return Optional.ofNullable(WAXABLES.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }
}