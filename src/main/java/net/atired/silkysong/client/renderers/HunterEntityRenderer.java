package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.models.HunterModel;
import net.atired.silkysong.client.renderstates.HunterRenderState;
import net.atired.silkysong.entities.HunterEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class HunterEntityRenderer extends BipedEntityRenderer<HunterEntity, HunterRenderState, HunterModel<HunterRenderState>> {

    private static final Identifier TEXTURE = SilkySong.id("textures/entity/cloakedtest.png");;
    public HunterEntityRenderer(EntityRendererFactory.Context context,EntityModelLayer armorInnerLayer, EntityModelLayer armorOuterLayer) {
        super(context, new HunterModel<>(HunterModel.getTexturedModelData().createModel()), 0.5F);
    }


    @Override
    public HunterRenderState createRenderState() {
        return new HunterRenderState();
    }
    protected BipedEntityModel.ArmPose getArmPose(HunterEntity abstractSkeletonEntity, Arm arm) {
        return abstractSkeletonEntity.getMainArm() == arm && abstractSkeletonEntity.isAttacking() && abstractSkeletonEntity.getMainHandStack().isOf(Items.BOW)
                ? BipedEntityModel.ArmPose.BOW_AND_ARROW
                : BipedEntityModel.ArmPose.EMPTY;
    }
    @Override
    public void updateRenderState(HunterEntity mobEntity, HunterRenderState bipedEntityRenderState, float f) {
        super.updateRenderState(mobEntity, bipedEntityRenderState, f);
        bipedEntityRenderState.attacking = mobEntity.isAttacking();
        bipedEntityRenderState.shaking = mobEntity.isShaking();
        bipedEntityRenderState.holdingBow = mobEntity.getMainHandStack().isOf(Items.BOW);
    }

    @Override
    public Identifier getTexture(HunterRenderState state) {
        return TEXTURE;
    }
}
