package com.cursedcauldron.unvotedandshelved.client.renderer.entity;

import com.cursedcauldron.unvotedandshelved.client.registries.USModelLayers;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.layer.GlareEyesLayer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.layer.GlareGlowberriesLayer;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class GlareRenderer extends MobRenderer<Glare, GlareModel<Glare>> {
    public GlareRenderer(EntityRendererProvider.Context context) {
        super(context, new GlareModel<>(context.bakeLayer(USModelLayers.GLARE)), 0.6F);
        this.addLayer(new GlareGlowberriesLayer<>(this));
        this.addLayer(new GlareEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Glare entity) {
        return new ResourceLocation(UnvotedAndShelved.MOD_ID, "textures/entity/glare/glare" + (entity.isFlowering() ? "_flowering" : "") + ".png");
    }

    @Override
    protected boolean isShaking(Glare entity) {
        return entity.isGrumpy();
    }
}