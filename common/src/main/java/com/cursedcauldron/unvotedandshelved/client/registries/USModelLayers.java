package com.cursedcauldron.unvotedandshelved.client.registries;

import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class USModelLayers {
    public static final ModelLayerLocation COPPER_GOLEM = create("copper_golem");
    public static final ModelLayerLocation OXIDIZED_COPPER_GOLEM = create("oxidized_copper_golem");

    public static final ModelLayerLocation GLARE = create("glare");

    public static ModelLayerLocation create(String key) {
        return new ModelLayerLocation(new ResourceLocation(UnvotedAndShelved.MOD_ID, key), "main");
    }

    public static ModelLayerLocation create(String key, String layer) {
        return new ModelLayerLocation(new ResourceLocation(UnvotedAndShelved.MOD_ID, key), layer);
    }
}