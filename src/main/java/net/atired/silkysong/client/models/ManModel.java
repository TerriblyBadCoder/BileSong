package net.atired.silkysong.client.models;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
import net.atired.silkysong.client.renderstates.ManEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ManModel<M extends EntityRenderState> extends BipedEntityModel<ManEntityRenderState> {
	private final ModelPart neckpiece;
	public ManModel(ModelPart root) {
		super(root);
		this.neckpiece = this.body.getChild("neckpiece");
	}

	@Override
	public ModelPart getHead() {
		return head;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));

		ModelPartData rleg = modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(32, 46).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(-2.0F, 0.0F, 0.1F));

		ModelPartData lleg = modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(40, 46).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(2.0F, 0.0F, 0.1F));

		ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(32, 32).cuboid(-4.0F, -8.0F, -1.0F, 8.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 0.0F, -0.1F));



		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 15).cuboid(-5.0F, -9.0F, -4.5F, 9.0F, 8.0F, 9.0F, new Dilation(0.0F))
		.uv(0, 32).cuboid(-4.5F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.origin(0.5F, -7.0F, 1.5F));
		ModelPartData neckpiece1 = head.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.origin(0.0F, -1.0F, -2F));
		ModelPartData neckpiece = body.addChild("neckpiece", ModelPartBuilder.create(), ModelTransform.origin(0.0F, -6.0f, -1.1f));


		ModelPartData cube_r1 = neckpiece.addChild("cube_r1", ModelPartBuilder.create().uv(36, 15).cuboid(-4.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, -1.0F, 0.0F, 0.1309F, -1.5708F));

		ModelPartData cube_r2 = neckpiece.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -2.3931F, -2.9218F, 13.0F, 4.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 1.0F, -0.25F, 0.2618F, 0.0F, 0.0F));

		ModelPartData rarm = modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(0, 48).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(-5.0F, -5.0F, 1.5F));

		ModelPartData larm = modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(48, 0).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(5.0F, -4.0F, 1.5F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(ManEntityRenderState state) {
		super.setAngles(state);
		this.rightArm.pitch *= 0.5F;
		this.leftArm.pitch *= 0.5F;
		this.rightLeg.pitch *= 0.5F;
		this.leftLeg.pitch *= 0.5F;
		float f = 0.4F;
		this.rightArm.pitch = MathHelper.clamp(this.rightArm.pitch, -0.4F, 0.4F);
		this.leftArm.pitch = MathHelper.clamp(this.leftArm.pitch, -0.4F, 0.4F);
		this.rightLeg.pitch = MathHelper.clamp(this.rightLeg.pitch, -0.4F, 0.4F);
		this.leftLeg.pitch = MathHelper.clamp(this.leftLeg.pitch, -0.4F, 0.4F);
		this.neckpiece.roll = (float)Math.sin(state.age/8.0f)/16.0f;
		this.neckpiece.yaw = (float)Math.cos(state.age/12.0f)/16.0f;
		this.neckpiece.pitch = (float)-Math.cos(state.age/18.0f)/16.0f;
	}
}