package com.cursedcauldron.unvotedandshelved.client;

import com.cursedcauldron.unvotedandshelved.client.registries.USModelLayers;
import com.cursedcauldron.unvotedandshelved.client.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.CopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.GlareRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.OxidizedCopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.CopperGolemModel;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.common.registries.entity.USEntities;
import com.cursedcauldron.unvotedandshelved.core.platform.client.ParticleRegistry;
import com.cursedcauldron.unvotedandshelved.core.platform.client.RenderRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;

public class ClientSetup {
    public static void onInstance() {
        ParticleRegistry.create(USParticles.GLOWBERRY_DUST, FlameParticle.Provider::new);

        RenderRegistry.entity(USEntities.GLARE, GlareRenderer::new, USModelLayers.GLARE, GlareModel::getLayerDefinition);
        RenderRegistry.entity(USEntities.COPPER_GOLEM, CopperGolemRenderer::new, USModelLayers.COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
        RenderRegistry.entity(USEntities.OXIDIZED_COPPER_GOLEM, OxidizedCopperGolemRenderer::new, USModelLayers.OXIDIZED_COPPER_GOLEM, CopperGolemModel::getLayerDefinition);
    }

    public static void postInstance() {
        RenderRegistry.block(RenderType.cutout(),
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