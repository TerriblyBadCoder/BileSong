package net.atired.silkysong.client.models;

import net.atired.silkysong.client.renderstates.BileMosqoEntityRenderState;
import net.atired.silkysong.client.renderstates.MissileToadEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.FrogEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.joml.Vector3f;

public class MissileToadModel<T extends MissileToadEntityRenderState> extends EntityModel<MissileToadEntityRenderState> {
	private final ModelPart core;
	private final ModelPart bone;
	private final ModelPart hindlegright;
	private final ModelPart frontlegleft;
	private final ModelPart hindlegleft;
	private final ModelPart frontlegright;

	private final ModelPart wart;
	public MissileToadModel(ModelPart root) {
		super(root);
		this.core = root.getChild("core");
		this.bone = this.core.getChild("bone");
		this.hindlegright = this.core.getChild("hindlegright");
		this.frontlegleft = this.core.getChild("frontlegleft");
		this.hindlegleft = this.core.getChild("hindlegleft");
		this.frontlegright = this.core.getChild("frontlegright");

		this.wart = this.core.getChild("wart");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData core = modelPartData.addChild("core", ModelPartBuilder.create(), ModelTransform.origin(-0.5F, 22.0F, 0.0F));

		ModelPartData bone = core.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -2.0F, -6.0F, 13.0F, 7.0F, 17.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -5.0F, 0.0F));

		ModelPartData hindlegright = core.addChild("hindlegright", ModelPartBuilder.create().uv(26, 24).cuboid(-7.0F, 2.6F, -1.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.origin(-4.5F, -1.6F, 8.0F));

		ModelPartData cube_r1 = hindlegright.addChild("cube_r1", ModelPartBuilder.create().uv(26, 32).cuboid(-2.0F, -3.0F, -3.0F, 7.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.0F, 2.0F, 0.0F, 0.2618F, 0.0F));

		ModelPartData frontlegleft = core.addChild("frontlegleft", ModelPartBuilder.create().uv(0, 38).mirrored().cuboid(-3.5F, 1.0F, -9.75F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
				.uv(24, 44).mirrored().cuboid(-4.0F, 4.6F, -12.75F, 7.0F, 0.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(7.0F, -3.6F, 2.75F));

		ModelPartData cube_r2 = frontlegleft.addChild("cube_r2", ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(-1.0F, 1.0F, -1.0F, 5.0F, 5.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.0F, -2.0F, -6.0F, 0.0F, 0.0F, -0.0873F));

		ModelPartData hindlegleft = core.addChild("hindlegleft", ModelPartBuilder.create().uv(26, 24).mirrored().cuboid(-1.0F, 2.6F, -1.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.origin(5.5F, -1.6F, 8.0F));

		ModelPartData cube_r3 = hindlegleft.addChild("cube_r3", ModelPartBuilder.create().uv(26, 32).mirrored().cuboid(-5.0F, -3.0F, -3.0F, 7.0F, 6.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.0F, 0.0F, 2.0F, 0.0F, -0.2618F, 0.0F));

		ModelPartData frontlegright = core.addChild("frontlegright", ModelPartBuilder.create().uv(0, 38).cuboid(-2.5F, 1.0F, -9.75F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F))
				.uv(24, 44).cuboid(-3.0F, 4.6F, -12.75F, 7.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.origin(-6.0F, -3.6F, 2.75F));

		ModelPartData cube_r4 = frontlegright.addChild("cube_r4", ModelPartBuilder.create().uv(0, 24).cuboid(-4.0F, 1.0F, -1.0F, 5.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -2.0F, -6.0F, 0.0F, 0.0F, 0.0873F));
		ModelPartData wart = core.addChild("wart", ModelPartBuilder.create().uv(0, 48).cuboid(-4.0F, -4.0F, -2.0F, 8.0F, 4.0F, 10.0F, new Dilation(0.0F)), ModelTransform.origin(0.5F, -2.0F, 2.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(MissileToadEntityRenderState state) {
		super.setAngles(state);
		this.wart.originY-=(float)Math.pow(state.bloat,0.5f)*1.3f;
		this.wart.xScale = (float)Math.pow(state.bloat,0.5f)/4.0f+0.5f;
		this.wart.zScale =this.wart.xScale;
		this.wart.yaw = (float)Math.sin(state.age/4.0f)/12.0f;
		this.frontlegleft.pitch = -(float)Math.sin(state.age/25.0f)/18.0f;
		this.frontlegright.pitch = (float)Math.sin(state.age/25.0f)/18.0f;
		this.frontlegleft.yaw = -(float)Math.sin(state.age/15.0f)/12.0f;
		this.frontlegright.yaw = (float)Math.sin(state.age/15.0f)/12.0f;
		this.hindlegleft.yaw = -(float)Math.cos(state.age/15.0f)/19.0f;
		this.hindlegright.yaw = (float)Math.cos(state.age/15.0f)/19.0f;
		this.frontlegleft.pitch += -(float)Math.sin(state.age/5.0f)/4.0f*(state.limbSwingAmplitude);
		this.frontlegright.pitch += (float)Math.sin(state.age/5.0f)/4.0f*(state.limbSwingAmplitude);
		this.frontlegleft.originY += Math.min(-(float)Math.sin(state.age/5.0f)*3f*(state.limbSwingAmplitude),0);
		this.frontlegright.originY += Math.min((float)Math.sin(state.age/5.0f)*3f*(state.limbSwingAmplitude),0);
		this.bone.roll -= (float)Math.sin(state.age/5.0f)/2f*(state.limbSwingAmplitude);
		this.hindlegleft.pitch = (float)Math.cos(state.age/5.0f)/7.0f*(state.limbSwingAmplitude);
		this.hindlegright.pitch = -(float)Math.cos(state.age/5.0f)/7.0f*(state.limbSwingAmplitude);
	}
}