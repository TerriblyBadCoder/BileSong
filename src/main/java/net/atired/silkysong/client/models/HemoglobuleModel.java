package net.atired.silkysong.client.models;

import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.renderstates.HemoGlobuleRenderState;
import net.atired.silkysong.entities.HemoGlobuleEntity;
import net.minecraft.client.model.ModelPart;

// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
public class HemoglobuleModel extends EntityModel<HemoGlobuleRenderState> {
	private final ModelPart core;
	public HemoglobuleModel(ModelPart root) {
		super(root,SKrenderLayers::getEntityTranslucentBlood);
		this.core = root.getChild("core");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData core = modelPartData.addChild("core", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 16).cuboid(0.0F, -10.0F, -4.0F, 0.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(HemoGlobuleRenderState state) {
		super.setAngles(state);
	}
}