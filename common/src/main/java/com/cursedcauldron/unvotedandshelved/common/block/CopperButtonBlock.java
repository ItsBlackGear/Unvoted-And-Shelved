package com.cursedcauldron.unvotedandshelved.common.block;

import com.cursedcauldron.unvotedandshelved.common.registries.USSoundEvents;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CopperButtonBlock extends ButtonBlock {
    protected final WeatherState weatherState;
    private static final Object2IntMap<WeatherState> PRESS_DURATION_BY_STATE = Object2IntMaps.unmodifiable(
        Util.make(new Object2IntLinkedOpenHashMap<>(), map -> {
            map.put(WeatherState.UNAFFECTED, 20);
            map.put(WeatherState.EXPOSED, 30);
            map.put(WeatherState.WEATHERED, 40);
            map.put(WeatherState.OXIDIZED, 50);
        })
    );

    public CopperButtonBlock(WeatherState weatherState, Properties properties) {
        super(false, properties);
        this.weatherState = weatherState;
        this.registerDefaultState(
            this.getStateDefinition().any()
                .setValue(POWERED, false)
        );
    }

    @Override
    protected SoundEvent getSound(boolean isOn) {
        return USSoundEvents.COPPER_CLICK.get();
    }

    @Override
    protected void playSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean hitByArrow) {
        level.playSound(hitByArrow ? player : null, pos, this.getSound(hitByArrow), SoundSource.BLOCKS, 0.3F, hitByArrow ? 1.0F : 0.9F);
    }

    @Override
    public void press(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, state.setValue(POWERED, true));
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
        level.scheduleTick(pos, this, this.getPressDuration());
    }

    private int getPressDuration() {
        return PRESS_DURATION_BY_STATE.get(this.weatherState);
    }
}