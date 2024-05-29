package com.cursedcauldron.unvotedandshelved.client;

import com.blackgear.platform.client.ParticleRegistry;
import com.blackgear.platform.client.RendererHandler;
import com.blackgear.platform.core.ParallelDispatch;
import com.cursedcauldron.unvotedandshelved.client.registries.USModelLayers;
import com.cursedcauldron.unvotedandshelved.client.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.CopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.OxidizedCopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;

public class ClientSetup {
    public static void onInstance() {
        ParticleRegistry.create(USParticles.GLOWBERRY_DUST, FlameParticle.Provider::new);

        RendererHandler.addEntityRenderer(USEntities.GLARE, GlareRenderer::new);
        RendererHandler.addLayerDefinition(USModelLayers.GLARE, GlareModel::getLayerDefinition);
        
        RendererHandler.addEntityRenderer(USEntities.COPPER_GOLEM, CopperGolemRenderer::new);
        RendererHandler.addLayerDefinition(USModelLayers.COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
        
        RendererHandler.addEntityRenderer(USEntities.OXIDIZED_COPPER_GOLEM, OxidizedCopperGolemRenderer::new);
        RendererHandler.addLayerDefinition(USModelLayers.OXIDIZED_COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
    }

    public static void postInstance(ParallelDispatch dispatch) {
        RendererHandler.addBlockRenderType(
            RenderType.cutout(),
            USBlocks.COPPER_PILLAR.get(),
            USBlocks.EXPOSED_COPPER_PILLAR.get(),
            USBlocks.WEATHERED_COPPER_PILLAR.get(),
            USBlocks.OXIDIZED_COPPER_PILLAR.get(),
            USBlocks.WAXED_COPPER_PILLAR.get(),
            USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get(),
            USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get(),
            USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get()
        );
    }
}