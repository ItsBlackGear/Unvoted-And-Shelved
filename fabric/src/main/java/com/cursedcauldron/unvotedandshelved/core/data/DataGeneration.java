package com.cursedcauldron.unvotedandshelved.core.data;

import com.cursedcauldron.unvotedandshelved.core.data.common.tag.BlockTagGenerator;
import com.cursedcauldron.unvotedandshelved.core.data.common.tag.EntityTagGenerator;
import com.cursedcauldron.unvotedandshelved.core.data.common.tag.ItemTagGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(EntityTagGenerator::new);
        BlockTagGenerator blockTagGenerator = generator.addProvider(BlockTagGenerator::new);
        generator.addProvider(new ItemTagGenerator(generator, blockTagGenerator));
    }
}