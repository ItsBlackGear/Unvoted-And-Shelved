package com.cursedcauldron.unvotedandshelved.common.block;

import com.cursedcauldron.unvotedandshelved.common.resources.block.USWeatheringCopper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperButtonBlock extends CopperButtonBlock implements USWeatheringCopper {
    public WeatheringCopperButtonBlock(WeatherState weatherState, Properties properties) {
        super(weatherState, properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return USWeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}