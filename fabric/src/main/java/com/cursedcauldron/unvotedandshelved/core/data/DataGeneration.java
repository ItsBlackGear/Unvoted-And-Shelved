package com.cursedcauldron.unvotedandshelved.core.data;

import com.cursedcauldron.unvotedandshelved.core.data.common.tag.EntityTagGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(EntityTagGenerator::new);
    }
}