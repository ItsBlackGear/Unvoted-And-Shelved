package com.cursedcauldron.unvotedandshelved.client.renderer.entity.model;

import com.cursedcauldron.unvotedandshelved.common.entity.glare.Glare;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class GlareModel<T extends Glare> extends HierarchicalModel<T> {
    private final ModelPart body;
    private final ModelPart eyes;
    private final ModelPart happyEyes;
    private final ModelPart frowningEyes;
    private final ModelPart leaves;
    private final ModelPart bottomLeaves;

    public GlareModel(ModelPart root) {
        this.body = root.getChild("body");
        this.eyes = this.body.getChild("eyes");
        this.happyEyes = this.eyes.getChild("happy_eyes");
        this.frowningEyes = this.eyes.getChild("frowning_eyes");
        this.leaves = this.body.getChild("leaves");
        this.bottomLeaves = this.leaves.getChild("bottom_leaves");
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-7.0F, -13.0F, -7.0F, 14.0F, 13.0F, 14.0F)
                        .texOffs(0, 27)
                        .addBox(-7.0F, -10.0F, -7.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(-0.01F)),
                PartPose.offset(0.0F, 17.0F, 0.0F)
        );

        PartDefinition eyes = body.addOrReplaceChild(
                "eyes",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -7.0F, -1.0F)
        );

        eyes.addOrReplaceChild(
                "happy_eyes",
                CubeListBuilder.create()
                        .texOffs(42, 0)
                        .addBox(-6.0F, -10.0F, 0.0F, 12.0F, 6.0F, 0.0F),
                PartPose.offset(0.0F, 7.0F, 0.0F)
        );

        eyes.addOrReplaceChild(
                "frowning_eyes",
                CubeListBuilder.create()
                        .texOffs(42, 6)
                        .addBox(-6.0F, -10.0F, 0.0F, 12.0F, 6.0F, 0.0F),
                PartPose.offset(0.0F, 7.0F, 0.0F)
        );

        PartDefinition leaves = body.addOrReplaceChild(
                "leaves",
                CubeListBuilder.create()
                        .texOffs(0, 51)
                        .addBox(-6.0F, 0.0F, -5.0F, 12.0F, 7.0F, 10.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        leaves.addOrReplaceChild(
                "bottom_leaves",
                CubeListBuilder.create()
                        .texOffs(44, 51)
                        .addBox(-4.0F, -1.0F, -3.0F, 8.0F, 11.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 80, 80);
    }

    @Override
    public ModelPart root() {
        return this.body;
    }

    @Override
    public void setupAnim(T glare, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        boolean isGrumpy = glare.isGrumpy();
        float speed = isGrumpy ? 1.25F : 1.0F;
        float degree = 2.0F;
        this.frowningEyes.visible = isGrumpy;
        this.happyEyes.visible = !isGrumpy;

        this.body.y = Mth.cos(limbAngle * speed * 0.3F) * degree * (isGrumpy ? 0.3F : 0.2F) * limbDistance + 10.0F;
        this.leaves.z = Mth.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
        this.leaves.xRot = Mth.cos(-1.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
        this.eyes.y = Mth.cos(animationProgress * speed * 0.1F) * degree * 0.125F - 7.0F;
        this.leaves.y = Mth.cos(limbDistance - 0.05F) - 1.5f;
        this.body.xRot = Mth.cos(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance;
        this.body.zRot = Mth.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.2F * limbDistance;
        this.bottomLeaves.z = Mth.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.1F * limbDistance + 0.05F;
        this.bottomLeaves.xRot = Mth.cos(-2.0F + limbAngle * speed * 0.3F) * degree * 0.3F * limbDistance + 0.1F;
        this.bottomLeaves.y = Mth.cos(limbDistance - 0.05F);

        if (isGrumpy) {
            this.body.x = Mth.cos(-1.0F + limbAngle * speed * 0.15F) * degree * 0.6F * limbDistance;
        }
    }
}