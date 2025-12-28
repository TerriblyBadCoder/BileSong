package net.atired.silkysong.client.models;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
import net.atired.silkysong.client.renderstates.PuppetEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.MathHelper;
public class PuppetEntityModel<M extends EntityRenderState> extends BipedEntityModel<PuppetEntityRenderState> {
	public PuppetEntityModel(ModelPart root) {
        super(root);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 42).cuboid(-1.3F, -0.4F, -2.0F, 3.0F, 8.0F, 3.0F, new Dilation(0.0F))
		.uv(40, 14).cuboid(-1.3F, 7.6F, -7.0F, 3.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.origin(2.1F, 13.4F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(26, 40).cuboid(-0.8F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(4.8F, 6.0F, -0.5F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(38, 40).cuboid(-2.2F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(-4.8F, 6.0F, -0.5F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(26, 29).cuboid(-1.7F, 7.6F, -7.0F, 3.0F, 3.0F, 8.0F, new Dilation(0.0F))
		.uv(12, 42).cuboid(-1.7F, -0.4F, -2.0F, 3.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(-2.1F, 13.4F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 14).cuboid(-5.5F, -6.0F, -4.6F, 11.0F, 6.0F, 9.0F, new Dilation(0.0F))
		.uv(48, 0).cuboid(-2.0F, -3.0F, -6.6F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 5.0F, -0.4F));

		ModelPartData hat = head.addChild("hat", ModelPartBuilder.create(), ModelTransform.origin(0.5F, -4.5F, -0.2F));

		ModelPartData cube_r1 = hat.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -2.0F, -7.0F, 13.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.1F, 1.6F, 0.0F, 0.0F, -0.0873F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 29).cuboid(-4.0F, -3.0F, -3.0F, 8.0F, 8.0F, 5.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 8.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(PuppetEntityRenderState bipedEntityRenderState) {
		super.setAngles(bipedEntityRenderState);

		this.rightLeg.pitch*=0.3f;
		this.leftLeg.pitch*=0.3f;

		this.rightLeg.roll+=0.05f;
		this.leftLeg.roll-=0.05f;

		this.rightLeg.pitch-=0.2f;
		this.leftLeg.pitch-=0.2f;


		this.rightArm.pitch*=0.4f;
		this.leftArm.pitch*=0.4f;

		this.rightArm.pitch+=0.27f;
		this.leftArm.pitch+=0.27f;
		this.head.pitch*=0.75f;
		this.body.roll -= (float)Math.sin(bipedEntityRenderState.age/24.0f)*0.2f;
		this.head.roll += (float)Math.sin(bipedEntityRenderState.age/16.0f)*0.1f;
		this.head.pitch += (float)Math.cos(bipedEntityRenderState.age/16.0f)*0.1f;
		for(ModelPart a : getParts()){
			a.pitch -= MathHelper.fractionalPart(a.pitch*16.0f)/16.0f;
			a.roll -= MathHelper.fractionalPart(a.roll*16.0f)/16.0f;
			a.yaw -= MathHelper.fractionalPart(a.yaw*16.0f)/16.0f;
		}
	}
}