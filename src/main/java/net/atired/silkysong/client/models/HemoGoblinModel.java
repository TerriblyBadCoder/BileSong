package net.atired.silkysong.client.models;

import net.atired.silkysong.client.SKrenderLayers;
import net.minecraft.client.model.ModelPart;

// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

public class HemoGoblinModel<B extends ArmedEntityRenderState> extends BipedEntityModel<BipedEntityRenderState> {
	public HemoGoblinModel(ModelPart root) {
		super(root, SKrenderLayers::getEntityTranslucentBlood);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 52).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(-3.0F, 13.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 52).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(3.0F, 12.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(42, 27).cuboid(-1.0F, -1.0F, -2.0F, 4.0F, 19.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(6.4F, -1.0F, -5.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(42, 50).cuboid(-3.0F, -1.0F, -2.0F, 4.0F, 19.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(-6.4F, -1.0F, -5.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.origin(1.0F, -2.0F, -1.0F));

		ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(7, 60).cuboid(0.0F, -8.9162F, -2.1823F, 0.0F, 10.0F, 19.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.9F, 3.3F, -1.3526F, 0.0F, 0.0F));

		ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(0, 27).cuboid(-7.0F, -16.0F, -4.5F, 12.0F, 16.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 15.0F, 1.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.origin(0.0F, -2.5427F, -0.9015F));

		ModelPartData hat = head.addChild("hat", ModelPartBuilder.create().uv(4, 56).cuboid(0.0F, -11.0F, -17.0F, 0.0F, 12.0F, 23.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.4573F, 1.9015F, -0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r3 = head.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -6.8181F, -15.3333F, 10.0F, 10.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(BipedEntityRenderState bipedEntityRenderState) {
		super.setAngles(bipedEntityRenderState);
		this.head.yaw*=0.4f;
		this.head.roll+= (float) (Math.sin(bipedEntityRenderState.age/24.0f)/48.0f);
		this.head.pitch+= (float) (Math.sin(bipedEntityRenderState.age/8.0f)/24.0f);
		this.body.pitch+= (float) (Math.cos(bipedEntityRenderState.age/8.0f)/24.0f);
		this.leftArm.yaw+= (float) (Math.sin(bipedEntityRenderState.age/1.0f)/24.0f);
		this.rightArm.yaw+= (float) (Math.cos(bipedEntityRenderState.age/1.0f)/24.0f);
		this.body.yaw-= (float) (Math.cos(bipedEntityRenderState.age/1.5f)/32.0f);
		this.rightLeg.yaw+= (float) (Math.sin(bipedEntityRenderState.age/1.0f)/24.0f);
		this.leftLeg.yaw+= (float) (Math.cos(bipedEntityRenderState.age/1.0f)/24.0f);
		if(bipedEntityRenderState.handSwingProgress>0.0){
			this.leftArm.pitch = this.rightArm.pitch;
			this.leftArm.yaw = this.rightArm.yaw;
			this.leftArm.roll = this.rightArm.roll;
		}
		else{
			this.rightArm.pitch *=0.5f;
			this.leftArm.pitch *=0.5f;
		}
	}
}