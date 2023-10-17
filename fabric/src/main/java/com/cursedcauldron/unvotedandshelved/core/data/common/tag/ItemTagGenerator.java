package com.cursedcauldron.unvotedandshelved.core.data.common.tag;

import com.cursedcauldron.unvotedandshelved.core.data.tags.USBlockTags;
import com.cursedcauldron.unvotedandshelved.core.data.tags.USItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import org.jetbrains.annotations.Nullable;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataGenerator generator, @Nullable BlockTagProvider blockTags) {
        super(generator, blockTags);
    }

    @Override
    protected void generateTags() {
        this.copy(USBlockTags.COPPER_BUTTONS, USItemTags.COPPER_BUTTONS);
        this.copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
    }
}