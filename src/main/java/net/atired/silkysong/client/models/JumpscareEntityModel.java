package net.atired.silkysong.client.models;

import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.renderstates.JumpscareEntityRenderState;
import net.minecraft.client.model.ModelPart;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.math.MathHelper;

public class JumpscareEntityModel<B extends ArmedEntityRenderState> extends BipedEntityModel<JumpscareEntityRenderState> {
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	public JumpscareEntityModel(ModelPart root) {
		super(root,SKrenderLayers::getEntityTranslucentBlood);
		this.head = root.getChild("head");
		this.hat = this.head.getChild("hat");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 5.5F, 0.0F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(0, 23).cuboid(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData hat = head.addChild("hat", ModelPartBuilder.create(), ModelTransform.origin(0.0F, -9.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(40, 23).cuboid(-4.0F, -1.0F, -3.0F, 8.0F, 11.0F, 6.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 6.0F, 1.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.origin(-4.0F, 7.6F, 1.0F));

		ModelPartData cube_r2 = right_arm.addChild("cube_r2", ModelPartBuilder.create().uv(40, 40).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 1.0F, 0.0F, 0.3463F, 0.0407F, -0.0112F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.origin(4.0F, 7.6F, 1.0F));

		ModelPartData cube_r3 = left_arm.addChild("cube_r3", ModelPartBuilder.create().uv(0, 43).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 1.0F, 0.0F, 0.3463F, -0.0407F, 0.0112F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(16, 43).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(-2.0F, 16.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(28, 43).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(2.0F, 16.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(JumpscareEntityRenderState bipedEntityRenderState) {
		super.setAngles(bipedEntityRenderState);
		float gotten = bipedEntityRenderState.revealedAmount;
		for(ModelPart modelPart : getParts()){
			modelPart.originY*=(gotten*0.9f+0.1f);
			modelPart.pitch*=(gotten*0.9f+0.1f);
			modelPart.yaw+= (float) (Math.PI*2.0f*gotten);
			if(modelPart!=root)
				modelPart.yScale*=(gotten*0.9f+0.1f);
		}
		head.pitch*=0.1f;
		root.originY+=16*1.7f*(1.0f-gotten);
		this.leftArm.pitch+=0.6f;
		this.rightArm.pitch+=0.6f;
		this.leftArm.pitch = MathHelper.lerp(bipedEntityRenderState.handRaise,this.leftArm.pitch,3.14f);
		this.rightArm.pitch = MathHelper.lerp(bipedEntityRenderState.handRaise,this.rightArm.pitch,3.14f);
		if(bipedEntityRenderState.handRaise>0.0){
			float clamped = 1.0f;
			this.leftArm.yaw = MathHelper.lerp(clamped,this.leftArm.yaw,3.14f/2.0f);
			this.rightArm.yaw =MathHelper.lerp(clamped,this.rightArm.yaw,-3.14f/2.0f);
		}
		if(bipedEntityRenderState.handSwingProgress>0.0){
			this.leftArm.pitch = this.rightArm.pitch;
			this.leftArm.yaw = this.rightArm.yaw;
			this.leftArm.roll = this.rightArm.roll;
		}
	}
}