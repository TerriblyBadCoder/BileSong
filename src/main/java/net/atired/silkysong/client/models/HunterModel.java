package net.atired.silkysong.client.models;

import net.atired.silkysong.client.renderstates.HunterRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.util.Arm;

public class HunterModel<S extends HunterRenderState> extends SkeletonEntityModel<S> {

    public final ModelPart cloak;

    public final ModelPart cloakLower;
    public HunterModel(ModelPart modelPart) {
        super(modelPart);
        this.cloak = modelPart.getChild("cloak");
        this.cloakLower = this.cloak.getChild("cloaklower");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        addLimbs(modelPartData);
        ModelPartData cloak = modelPartData.addChild(
                "cloak",
                ModelPartBuilder.create().uv(0, 45).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 13.0F, 5.0F,new Dilation(0.1f)),
                ModelTransform.origin(0.0F, 0.0F + 0.0F, 0.0F)
        );
        cloak.addChild(
                "cloaklower",
                ModelPartBuilder.create().uv(32, 45).cuboid(-4.5F, 10.2F, -3F, 9.0F, 4.0F, 6.0F,new Dilation(0.1f)),
                ModelTransform.origin(0.0F, 0.0F + 0.0F, 0.0F)
        );
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(S skeletonEntityRenderState) {
        super.setAngles(skeletonEntityRenderState);
        if(skeletonEntityRenderState.leftArmPose==ArmPose.BOW_AND_ARROW||skeletonEntityRenderState.rightArmPose==ArmPose.BOW_AND_ARROW){
            float progress = skeletonEntityRenderState.handSwingProgress*1.2f;
            if(skeletonEntityRenderState.mainArm== Arm.RIGHT){
                this.leftArm.pitch += progress+0.2f;
                this.leftArm.yaw += progress/2.0f-0.1f;
            }
            else{

                this.rightArm.pitch += progress+0.2f;
                this.rightArm.yaw += progress/2.0f+0.1f;
            }
            this.head.pitch-=progress/3.2f;
        }


        this.head.roll += (float)Math.sin(skeletonEntityRenderState.age/13.0f)/18.0f;
        this.head.yaw += (float)Math.cos(skeletonEntityRenderState.age/23.0f)/18.0f;
        this.cloak.copyTransform(this.body);
        this.cloak.yaw += (float)Math.sin(skeletonEntityRenderState.age/4.0f)/24.0f;
        this.cloakLower.yaw += (float)Math.cos(skeletonEntityRenderState.age/4.0f)/16.0f;
    }

}
