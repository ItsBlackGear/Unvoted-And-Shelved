package com.cursedcauldron.unvotedandshelved.core.data.common.tag;

import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class EntityTagGenerator extends FabricTagProvider.EntityTypeTagProvider {
    public EntityTagGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateTags() {
        this.tag(USEntityTypeTags.REACHABLE_BY_LIGHTNING)
                .add(USEntities.COPPER_GOLEM.get())
                .add(USEntities.OXIDIZED_COPPER_GOLEM.get());
    }
}