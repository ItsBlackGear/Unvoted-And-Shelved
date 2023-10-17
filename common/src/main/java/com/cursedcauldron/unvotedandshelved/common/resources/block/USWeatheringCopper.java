package com.cursedcauldron.unvotedandshelved.common.resources.block;

import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Supplier;

public interface USWeatheringCopper extends ChangeOverTimeBlock<WeatherState> {
    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(
            () -> ImmutableBiMap.<Block, Block>builder()
                    .put(USBlocks.COPPER_BUTTON.get(), USBlocks.EXPOSED_COPPER_BUTTON.get())
                    .put(USBlocks.EXPOSED_COPPER_BUTTON.get(), USBlocks.WEATHERED_COPPER_BUTTON.get())
                    .put(USBlocks.WEATHERED_COPPER_BUTTON.get(), USBlocks.OXIDIZED_COPPER_BUTTON.get())
                    .build()
    );

    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
    }

    static Block getFirst(Block block) {
        Block a = block;

        for (Block b = PREVIOUS_BY_BLOCK.get().get(block); b != null; b = PREVIOUS_BY_BLOCK.get().get(b)) {
            a = b;
        }

        return a;
    }

    static Optional<BlockState> getPrevious(BlockState state) {
        return getPrevious(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    static Optional<Block> getNext(Block block) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(block));
    }

    static BlockState getFirst(BlockState state) {
        return getFirst(state.getBlock()).withPropertiesOf(state);
    }

    @Override
    default Optional<BlockState> getNext(BlockState state) {
        return getNext(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    @Override
    default float getChanceModifier() {
        return this.getAge() == WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }
}