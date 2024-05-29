package com.cursedcauldron.unvotedandshelved.core.registry;

import com.cursedcauldron.unvotedandshelved.core.api.DataSerializerRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.WeatheringCopper;

public class ModEntityDataSerializers {
    public static final DataSerializerRegistry SERIALIZERS = new DataSerializerRegistry();
    
    public static final EntityDataSerializer<WeatheringCopper.WeatherState> WEATHER_STATE = SERIALIZERS.simpleEnum(WeatheringCopper.WeatherState.class);
}