package com.cursedcauldron.unvotedandshelved.client;

import com.cursedcauldron.unvotedandshelved.client.registries.USModelLayers;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.CopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.OxidizedCopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.OxidizedCopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import com.cursedcauldron.unvotedandshelved.core.platform.client.RenderRegistry;

public class ClientSetup {
    public static void onInstance() {
        RenderRegistry.renderer(USEntities.GLARE, GlareRenderer::new);
        RenderRegistry.layerDefinition(USModelLayers.GLARE, GlareModel::getLayerDefinition);

        RenderRegistry.renderer(USEntities.COPPER_GOLEM, CopperGolemRenderer::new);
        RenderRegistry.layerDefinition(USModelLayers.COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
        RenderRegistry.renderer(USEntities.OXIDIZED_COPPER_GOLEM, OxidizedCopperGolemRenderer::new);
        RenderRegistry.layerDefinition(USModelLayers.OXIDIZED_COPPER_GOLEM, OxidizedCopperGolemModel::getLayerDefinition);
    }

    public static void postInstance() {
    }
}