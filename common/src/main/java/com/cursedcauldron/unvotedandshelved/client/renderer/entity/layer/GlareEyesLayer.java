package com.cursedcauldron.unvotedandshelved.client.renderer.entity.layer;

import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.GlareModel;
import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class GlareEyesLayer<T extends Glare> extends RenderLayer<T, GlareModel<T>> {
    private static final Function<Glare, ResourceLocation> FUNCTION_EYES = glare -> new ResourceLocation(UnvotedAndShelved.MOD_ID, "textures/entity/glare/glare_eyes_lit" + (glare.isFlowering() ? "_flowering" : "") + ".png");

    public GlareEyesLayer(RenderLayerParent<T, GlareModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource source, int light, T glare, float angle, float distance, float tickDelta, float animationProgress, float yaw, float pitch) {
        if (!glare.isInvisible() && glare.getHeldGlowberries() != 0) {
            VertexConsumer vertices = source.getBuffer(RenderType.entityCutout(this.getTexture(glare)));
            this.getParentModel().renderToBuffer(matrices, vertices, light, LivingEntityRenderer.getOverlayCoords(glare, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private ResourceLocation getTexture(T entity) {
        return FUNCTION_EYES.apply(entity);
    }
}