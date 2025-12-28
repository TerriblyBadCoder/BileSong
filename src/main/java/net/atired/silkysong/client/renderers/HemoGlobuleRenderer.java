package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.models.HemoglobuleModel;
import net.atired.silkysong.client.renderstates.HemoGlobuleRenderState;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import net.atired.silkysong.entities.HemoGlobuleEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MagmaCubeEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.model.MagmaCubeEntityModel;
import net.minecraft.client.render.entity.state.SlimeEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HemoGlobuleRenderer extends MobEntityRenderer<HemoGlobuleEntity, HemoGlobuleRenderState, HemoglobuleModel> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/hemoglobule.png");

    public HemoGlobuleRenderer(EntityRendererFactory.Context context, HemoglobuleModel entityModel, float f) {
        super(context, entityModel, f);
    }

    protected float getShadowRadius(HemoGlobuleRenderState slimeEntityRenderState) {
        return slimeEntityRenderState.size * 0.25F;
    }

    protected void scale(HemoGlobuleRenderState slimeEntityRenderState, MatrixStack matrixStack) {
        float f = 0.999F;
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0F, 0.001F, 0.0F);
        float g = slimeEntityRenderState.size;
        float h = slimeEntityRenderState.stretch / (g * 0.5F + 1.0F);
        float i = 1.0F / (h + 1.0F);
        matrixStack.scale(i * g, 1.0F / i * g, i * g);
    }

    @Override
    public void render(HemoGlobuleRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if(MinecraftClient.getInstance().gameRenderer.getCamera()!=null){
            DynamicTESTUniforms.set(new Vec3d(livingEntityRenderState.x,livingEntityRenderState.y,livingEntityRenderState.z).subtract(MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos()).toVector3f(),(livingEntityRenderState.size-1+Math.abs(livingEntityRenderState.stretch*6f))/2.0f);
            SilkySongClient.PROXY.buffer = DynamicTESTUniforms.BUFFER;
        }
        super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(HemoGlobuleRenderState slimeEntityRenderState) {
        return TEXTURE;
    }

    public HemoGlobuleRenderState createRenderState() {
        return new HemoGlobuleRenderState();
    }

    public void updateRenderState(HemoGlobuleEntity slimeEntity, HemoGlobuleRenderState slimeEntityRenderState, float f) {
        super.updateRenderState(slimeEntity, slimeEntityRenderState, f);
        slimeEntityRenderState.stretch = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch);
        slimeEntityRenderState.size = slimeEntity.getSize();
    }
}
