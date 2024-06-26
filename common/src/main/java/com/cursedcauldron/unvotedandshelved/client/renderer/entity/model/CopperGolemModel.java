package com.cursedcauldron.unvotedandshelved.client.renderer.entity.model;

import com.cursedcauldron.unvotedandshelved.client.renderer.entity.animations.CopperGolemAnimations;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import com.cursedcauldron.unvotedandshelved.core.mixin.access.HierarchicalModelAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.block.WeatheringCopper;

@Environment(EnvType.CLIENT)
public class CopperGolemModel<T extends AbstractGolem> extends HierarchicalModel<T> {
    private static final float WALK_ANIMATION_SCALE_FACTOR = 100.0F;
    private static final float WALK_ANIMATION_SPEED = 3.0F;
    private static final float WEATHERED_WALK_ANIMATION_SPEED = 4.0F;
    private final ModelPart head;
    private final ModelPart root;

    public CopperGolemModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("body").getChild("head");
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-5.0F, -3.0F, -3.5F, 10.0F, 7.0F, 7.0F),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        PartDefinition head = body.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-6.0F, -7.0F, -4.0F, 12.0F, 7.0F, 9.0F)
                        .texOffs(32, 16).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, -3.0F, -0.5F)
        );

         head.addOrReplaceChild(
                 "antenna",
                 CubeListBuilder.create()
                         .texOffs(0, 0)
                         .addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F)
                         .texOffs(28, 40).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                 PartPose.offset(0.0F, -7.0F, 0.0F)
         );

        body.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(0, 34).mirror()
                        .addBox(-1.0F, -1.0F, -1.5F, 3.0F, 11.0F, 3.0F)
                        .mirror(false),
                PartPose.offset(6.0F, -2.0F, 0.0F)
        );

        body.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-2.0F, -1.0F, -1.5F, 3.0F, 11.0F, 3.0F),
                PartPose.offset(-6.0F, -2.0F, 0.0F)
        );

        body.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(12, 40)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(2.5F, 4.0F, 0.0F)
        );

        body.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(12, 40)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(-2.5F, 4.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 48, 48);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof CopperGolem golem && !golem.isOxidized()) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            this.head.yRot = netHeadYaw * (float)(Math.PI / 180.0F);

            if (golem.getWeatherState() == WeatheringCopper.WeatherState.WEATHERED) {
                this.animateWalk(CopperGolemAnimations.WALKING, limbSwing, limbSwingAmount, WEATHERED_WALK_ANIMATION_SPEED, WALK_ANIMATION_SCALE_FACTOR);
            } else {
                this.animateWalk(CopperGolemAnimations.WALKING, limbSwing, limbSwingAmount, WALK_ANIMATION_SPEED, WALK_ANIMATION_SCALE_FACTOR);
            }

            this.animate(golem.headSpinAnimation, this.getHeadSpinAnimation(golem), ageInTicks);

            this.animate(golem.interactingAnimation, CopperGolemAnimations.BUTTON_PRESS, ageInTicks);
            this.animate(golem.interactingAboveAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, ageInTicks);
            this.animate(golem.interactingBelowAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, ageInTicks);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    protected void animateWalk(AnimationDefinition animationDefinition, float limbSwing, float limbSwingAmount, float speed, float scaleFactor) {
        long accumulatedTime = (long)(limbSwing * 50.0f * speed);
        float scale = Math.min(limbSwingAmount * scaleFactor, 1.0f);
        KeyframeAnimations.animate(this, animationDefinition, accumulatedTime, scale, HierarchicalModelAccessor.animationVectorCache());
    }

    private AnimationDefinition getHeadSpinAnimation(CopperGolem golem) {
        return switch (golem.getWeatherState()) {
            case EXPOSED -> CopperGolemAnimations.HEAD_SPIN_SLOWER;
            case WEATHERED -> CopperGolemAnimations.HEAD_SPIN_SLOWEST;
            default -> CopperGolemAnimations.HEAD_SPIN;
        };
    }
}