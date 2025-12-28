package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.models.HemoGoblinModel;
import net.atired.silkysong.client.models.MissileToadModel;
import net.atired.silkysong.client.renderstates.ManEntityRenderState;
import net.atired.silkysong.client.renderstates.MissileToadEntityRenderState;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import net.atired.silkysong.entities.HemogoblinEntity;
import net.atired.silkysong.entities.MissileToadEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

public class MissileToadEntityRenderer extends MobEntityRenderer<MissileToadEntity, MissileToadEntityRenderState, MissileToadModel<MissileToadEntityRenderState>> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/missiletoad.png");
    private static final Identifier TEXTURE_TONGUE = SilkySong.id("textures/entity/tongue.png");
    public MissileToadEntityRenderer(EntityRendererFactory.Context context,MissileToadModel<MissileToadEntityRenderState> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(MissileToadEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(state, showBody, translucent, showOutline);
    }

    @Override
    public void render(MissileToadEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);
        RenderLayer layer = RenderLayer.getEntityTranslucent(TEXTURE_TONGUE,false);
        VertexConsumer consumer = vertexConsumerProvider.getBuffer(layer);
        if(livingEntityRenderState.tonguePos==Vec3d.ZERO){
            return;
        }
        Vec3d dir = livingEntityRenderState.tonguePos.subtract(livingEntityRenderState.x,livingEntityRenderState.y,livingEntityRenderState.z);
        matrixStack.push();
        MatrixStack.Entry entry = matrixStack.peek();
        float yawd = livingEntityRenderState.bodyYaw/180*3.14f;
        Vec3d vec3d = new Vec3d(0,0,0.33).rotateY(-yawd);

        Vec3d dir3 = new Vec3d(0,0,0.32).rotateY(-yawd).rotateY(-3.14f/2f).multiply(0.4);

        Vec3d dir4 = new Vec3d(0,0,0.32).rotateY(-yawd).rotateY(3.14f/2f).multiply(0.4);
        Vec3d dir1 = dir.multiply(1,0,1).normalize().rotateY(-3.14f/2f).multiply(0.2);
        Vec3d dir2 = dir.multiply(1,0,1).normalize().rotateY(3.14f/2f).multiply(0.2);
        vertex(entry,consumer,(float)(vec3d.x+dir4.x*2),0.38f,(float)(vec3d.z+dir4.z*2),0,0,0,0,1,i,1.0f);
        vertex(entry,consumer,(float)(vec3d.x+dir3.x*2),0.38f,(float)(vec3d.z+dir3.z*2),1f,0,0,0,1,i,1.0f);
        vertex(entry,consumer,(float)(dir1.x*0.6f+vec3d.x*1.6f+dir3.x),0.45f,(float)(dir1.z*0.6f+vec3d.z*1.6f+dir3.z),1,0.1f,0,0,1,i,1.0f);
        vertex(entry,consumer,(float)(dir2.x*0.6f+vec3d.x*1.6f+dir4.x),0.45f,(float)(dir2.z*0.6f+vec3d.z*1.6f+dir4.z),0,0.1f,0,0,1,i,1.0f);
        vertex(entry,consumer,(float)(dir2.x*0.6f+vec3d.x*1.6f+dir4.x),0.45f,(float)(dir2.z*0.6f+vec3d.z*1.6f+dir4.z),0,0.1f,0,0,1,i,1.0f);
        vertex(entry,consumer,(float)(dir1.x*0.6f+vec3d.x*1.6f+dir3.x),0.45f,(float)(dir1.z*0.6f+vec3d.z*1.6f+dir3.z),1,0.1f,0,0,1,i,1.0f);
        Vec3d dirTemp = dir;
        dir = dir.add(Math.sin(livingEntityRenderState.age+0.5)/4.0f,0,Math.cos(livingEntityRenderState.age+0.5)/4.0f);
        vertex(entry,consumer,(float)(dir1.x+dir.x*0.2),(float)dir.y*0.09f+0.45f,(float)(dir1.z+dir.z*0.2),1,0.2f,0,0,1,i,1.0f);
        vertex(entry,consumer,(float)(dir2.x+dir.x*0.2),(float)dir.y*0.09f+0.45f,(float)(dir2.z+dir.z*0.2),0,0.2f,0,0,1,i,1.0f);
        float offsetY = 0.0f;
        for (int j = 1; j < 9; j++) {
            dir = dirTemp;
            dir = dir.add(Math.sin(livingEntityRenderState.age+j/2.0f)/4.0f,0,Math.cos(livingEntityRenderState.age+j/2.0f)/4.0f);
            offsetY = (float) Math.pow((j+1)/10.0f,1.5f);
            vertex(entry,consumer,(float)(dir2.x+dir.x*(j+1)/10.0f),(float)dir.y*offsetY+0.45f,(float)(dir2.z+dir.z*(j+1)/10.0f),0,(j+1)/10.0f,0,0,1,i,1.0f);
            vertex(entry,consumer,(float)(dir1.x+dir.x*(j+1)/10.0f),(float)dir.y*offsetY+0.45f,(float)(dir1.z+dir.z*(j+1)/10.0f),1,(j+1)/10.0f,0,0,1,i,1.0f);
            dir = dirTemp;
            dir = dir.add(Math.sin(livingEntityRenderState.age+(j+1)/2.0f)/4.0f,0,Math.cos(livingEntityRenderState.age+(j+1)/2.0f)/4.0f);
            offsetY = (float) Math.pow((j+2)/10.0f,1.5f);
            vertex(entry,consumer,(float)(dir1.x+dir.x*(j+2)/10.0f),(float)dir.y*offsetY+0.45f,(float)(dir1.z+dir.z*(j+2)/10.0f),1,(j+2)/10.0f,0,0,1,i,1.0f);
            vertex(entry,consumer,(float)(dir2.x+dir.x*(j+2)/10.0f),(float)dir.y*offsetY+0.45f,(float)(dir2.z+dir.z*(j+2)/10.0f),0,(j+2)/10.0f,0,0,1,i,1.0f);

        }
        matrixStack.pop();
    }

    @Override
    public void updateRenderState(MissileToadEntity livingEntity, MissileToadEntityRenderState livingEntityRenderState, float f) {
        livingEntityRenderState.tonguePos = livingEntity.targetPos;
        livingEntityRenderState.bloat = livingEntity.bloatLerped;
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
    }

    @Override
    protected float getShadowRadius(MissileToadEntityRenderState livingEntityRenderState) {
        return super.getShadowRadius(livingEntityRenderState);
    }

    @Override
    protected float getShadowOpacity(MissileToadEntityRenderState state) {
        return super.getShadowOpacity(state);
    }

    @Override
    public Identifier getTexture(MissileToadEntityRenderState state) {
        return TEXTURE;
    }
    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int normalX, int normalZ, int normalY, int light, float a) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(1,1,1,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix, (float)normalX, (float)normalY, (float)normalZ);
    }
    @Override
    public MissileToadEntityRenderState createRenderState() {
        return new MissileToadEntityRenderState();
    }
}
