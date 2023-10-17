package com.cursedcauldron.unvotedandshelved.core.data.common.tag;

import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tags.BlockTags;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateTags() {
        this.tag(USBlockTags.COPPER_BUTTONS)
                .add(
                        USBlocks.COPPER_BUTTON.get(),
                        USBlocks.EXPOSED_COPPER_BUTTON.get(),
                        USBlocks.WEATHERED_COPPER_BUTTON.get(),
                        USBlocks.OXIDIZED_COPPER_BUTTON.get(),
                        USBlocks.WAXED_COPPER_BUTTON.get(),
                        USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(),
                        USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(),
                        USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()
                );
        this.tag(BlockTags.BUTTONS)
                .addTag(USBlockTags.COPPER_BUTTONS);
    }
}