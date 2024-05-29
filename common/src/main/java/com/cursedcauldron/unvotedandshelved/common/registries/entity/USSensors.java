package com.cursedcauldron.unvotedandshelved.common.registries.entity;

import com.blackgear.platform.core.CoreRegistry;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.GlareBrain;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.core.mixin.access.SensorTypeAccessor;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;

import java.util.function.Supplier;

public class USSensors {
    public static final CoreRegistry<SensorType<?>> SENSORS = CoreRegistry.create(Registry.SENSOR_TYPE, UnvotedAndShelved.MOD_ID);

    public static final Supplier<SensorType<TemptingSensor>> GLARE_TEMPTATIONS = create("glare_temptations", () -> new TemptingSensor(GlareBrain.getTemptations()));

    private static <T extends Sensor<?>> Supplier<SensorType<T>> create(String key, Supplier<T> supplier) {
        return SENSORS.register(key, () -> SensorTypeAccessor.create(supplier));
    }
}