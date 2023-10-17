package com.cursedcauldron.unvotedandshelved.client.renderer.entity.model;

import com.cursedcauldron.unvotedandshelved.client.renderer.entity.animations.CopperGolemAnimations;
import com.cursedcauldron.unvotedandshelved.common.entity.coppergolem.CopperGolem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.animal.AbstractGolem;

@Environment(EnvType.CLIENT)
public class CopperGolemModel<T extends AbstractGolem> extends HierarchicalModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart root;

    public CopperGolemModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -3.0F, -3.5F, 10.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.0F, -4.0F, 12.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(32, 16).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -0.5F));
         PartDefinition lightingRod = head.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 40).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition leftArm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -1.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -2.0F, 0.0F));
        PartDefinition rightArm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, -1.0F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -2.0F, 0.0F));
        PartDefinition leftLeg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(12, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 4.0F, 0.0F));
        PartDefinition rightLeg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(12, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 4.0F, 0.0F));

        return LayerDefinition.create(mesh, 48, 48);
    }


    @Override
    public void setupAnim(T entity, float angle, float distance, float animationProgress, float yaw, float pitch) {
        if (entity instanceof CopperGolem golem && !golem.isOxidized()) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            this.head.yRot = yaw * 0.017453292F;
            float speed = Math.min((float)entity.getDeltaMovement().lengthSqr() * 125.0F, 1.0F);
            this.animate(golem.walkingAnimation, CopperGolemAnimations.walkingAnimation(golem.getWeatherState()), animationProgress, speed);

            this.animate(golem.headSpinAnimation, CopperGolemAnimations.HEAD_SPIN, animationProgress);
    //        this.animate(entity.headSpinSlowerAnimation, CopperGolemAnimations.HEAD_SPIN_SLOWER, animationProgress);
    //        this.animate(entity.headSpinSlowestAnimation, CopperGolemAnimations.HEAD_SPIN_SLOWEST, animationProgress);

            this.animate(golem.interactingAnimation, CopperGolemAnimations.BUTTON_PRESS, animationProgress);
    //        this.animate(entity.buttonSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS, animationProgress);
    //        this.animate(entity.buttonSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS, animationProgress);

            this.animate(golem.interactingAboveAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, animationProgress);
    //        this.animate(entity.buttonUpSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, animationProgress);
    //        this.animate(entity.buttonUpSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS_UP, animationProgress);

            this.animate(golem.interactingBelowAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, animationProgress);
    //        this.animate(entity.buttonDownSlowerAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, animationProgress);
    //        this.animate(entity.buttonDownSlowestAnimation, CopperGolemAnimations.BUTTON_PRESS_DOWN, animationProgress);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}