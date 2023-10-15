package com.cursedcauldron.unvotedandshelved.common.registries.entity;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.WeatheringCopper;

public class USDataSerializers {
    public static final EntityDataSerializer<WeatheringCopper.WeatherState> WEATHER_STATE = EntityDataSerializer.simpleEnum(WeatheringCopper.WeatherState.class);

    public static void bootstrap() {
        EntityDataSerializers.registerSerializer(WEATHER_STATE);
    }
}