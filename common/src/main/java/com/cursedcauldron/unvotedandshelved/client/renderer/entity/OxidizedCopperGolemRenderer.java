package com.cursedcauldron.unvotedandshelved.client.renderer.entity;

import com.cursedcauldron.unvotedandshelved.client.registries.USModelLayers;
import com.cursedcauldron.unvotedandshelved.client.renderer.entity.model.OxidizedCopperGolemModel;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.OxidizedCopperGolem;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class OxidizedCopperGolemRenderer extends MobRenderer<OxidizedCopperGolem, OxidizedCopperGolemModel<OxidizedCopperGolem>> {
    public OxidizedCopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new OxidizedCopperGolemModel<>(context.bakeLayer(USModelLayers.COPPER_GOLEM)), 0.5F);
    }

    @Override
    protected void setupRotations(OxidizedCopperGolem golem, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        float timeSinceLastHit = (float)(golem.level.getGameTime() - golem.lastHit) + partialTicks;

        if (timeSinceLastHit < 5.0F) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(timeSinceLastHit / 1.5F * (float)Math.PI) * 3.0F));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(OxidizedCopperGolem entity) {
        return new ResourceLocation(UnvotedAndShelved.MOD_ID, "textures/entity/copper_golem/oxidized_copper_golem.png");
    }
}