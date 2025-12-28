
package net.atired.silkysong.client.models;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
import net.atired.silkysong.client.renderstates.BileMosqoEntityRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.joml.Vector3f;

public class BileMosqoModel<B extends LivingEntityRenderState> extends EntityModel<BileMosqoEntityRenderState> {
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart wingright;
	private final ModelPart wingleft;
	private final ModelPart bodier;
	private final ModelPart head;
	private final ModelPart bone;
	public BileMosqoModel(ModelPart root) {
        super(root);

        this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.tail = this.body.getChild("tail");
		this.wingright = this.body.getChild("wingright");
		this.wingleft = this.body.getChild("wingleft");
		this.bodier = this.body.getChild("bodier");
		this.head = this.root.getChild("head");
		this.bone = this.head.getChild("bone");
	}
	public static TexturedModelData getTexturedModelData(float dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));

		ModelPartData body = root.addChild("body", ModelPartBuilder.create(), ModelTransform.of(0.0F, -6.15F, 2.6F, 0.1745F, 0.0F, 0.0F));

		ModelPartData bodier = body.addChild("bodier", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = bodier.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0555F, -8.8943F, 4.0F, 4.0F, 9.0F, new Dilation(dilation)), ModelTransform.of(0.0F, -0.1F, 0.9F, 0.7854F, 0.0F, 0.0F));


		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 4.5F, -4.5F));

		ModelPartData cube_r2 = tail.addChild("cube_r2", ModelPartBuilder.create().uv(29, 33).cuboid(0.0F, -3.8155F, -9.1287F, 0.0F, 5.0F, 9.0F, new Dilation(dilation)), ModelTransform.of(0.0F, -0.25F, 0.0F, -0.3491F, 0.0F, -3.1416F));

		ModelPartData wingright = body.addChild("wingright", ModelPartBuilder.create(), ModelTransform.origin(1.9F, -1.15F, 0.4F));

		ModelPartData wingright_r1 = wingright.addChild("wingright_r1", ModelPartBuilder.create().uv(-5, 41).mirrored().cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 6.0F, new Dilation(dilation)).mirrored(false), ModelTransform.of(-0.05F, 2.0F, -0.75F, -0.2887F, -0.0433F, -1.6118F));

		ModelPartData wingleft = body.addChild("wingleft", ModelPartBuilder.create(), ModelTransform.origin(-1.9F, -1.15F, 0.4F));

		ModelPartData wingleft_r1 = wingleft.addChild("wingleft_r1", ModelPartBuilder.create().uv(-5, 41).cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 6.0F, new Dilation(dilation)), ModelTransform.of(-0.05F, 2.0F, -0.75F, -0.2887F, 0.0433F, 1.6118F));


		ModelPartData head = root.addChild("head", ModelPartBuilder.create(), ModelTransform.origin(0.0F, -5.9F, 2.5F));

		ModelPartData cube_r3 = head.addChild("cube_r3", ModelPartBuilder.create().uv(18, 22).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(dilation)), ModelTransform.of(0.0F, -0.35F, -0.25F, 0.2182F, 0.0F, 0.0F));

		ModelPartData bone = head.addChild("bone", ModelPartBuilder.create().uv(0, 13).cuboid(-1.0F, -3.0F, -5.0F, 3.0F, 4.0F, 6.0F, new Dilation(dilation)), ModelTransform.origin(-0.5F, -4.6F, -1.2F));

		ModelPartData cube_r4 = bone.addChild("cube_r4", ModelPartBuilder.create().uv(32, 29).cuboid(0.0F, 1.0F, -6.0F, 0.0F, 2.0F, 6.0F, new Dilation(dilation)), ModelTransform.of(0.5F, -2.4F, -4.8F, 0.3491F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 48, 48);
	}

	@Override
	public void setAngles(BileMosqoEntityRenderState state) {
		super.setAngles(state);
		float ageInTicks = state.age;
		if(state.health<6.0f){
			this.root.setOrigin(0,20.0f,0);
				this.root.pitch = (float) Math.cos(ageInTicks/3.0f)*3.0f*state.fallticks/20;
				this.root.roll =(float) Math.cos(ageInTicks/3.0f)*3.0f*state.fallticks/20;
				this.root.yaw = (float) Math.cos(ageInTicks/3.0f)*3.0f*state.fallticks/20;

		}

		this.head.pitch  = state.pitch * (float) (Math.PI / 180.0)/3.0f;
		this.bone.pitch  = state.pitch * (float) (Math.PI / 180.0)/3.0f;
		this.root.pitch += this.bone.pitch/1.5f;
		this.bone.roll = (float) -Math.cos(ageInTicks/4.0f)/12.0f;
		this.bodier.resetTransform();
		this.bodier.roll = (float) (Math.sin(state.age/4.0)/7.0f);
		this.bodier.scale(new Vector3f(0.1f,0.1f,0.1f).mul( (float) (Math.sin(state.age/2.0))+0.25f));
		this.body.roll = (float) (Math.cos(state.age/4.0)/20.0f);
		float clampedvel = (float) Math.clamp(Math.pow(state.velocity,0.5f),0.0f,2.0f)/3.0f;
		this.body.pitch = 1.2f+this.body.roll*3.0f+clampedvel;
		float angle = (float) (Math.sin(ageInTicks*2f)*0.6f);
		float angle2 = (float) (Math.cos(ageInTicks*2f)*0.6f);
		this.wingright.roll = -1.0F+angle;
		this.wingleft.roll = 1.0F+angle2;
		this.wingright.pitch =  (float) (Math.sin(state.age/4.0)/12.0f);
		this.wingleft.pitch =  (float) (Math.sin(state.age/4.0)/12.0f);

	}
}