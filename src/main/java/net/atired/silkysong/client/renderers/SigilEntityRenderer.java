package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.renderstates.SigilEntityRenderState;
import net.atired.silkysong.entities.SigilEntity;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class SigilEntityRenderer extends EntityRenderer<SigilEntity, SigilEntityRenderState> {

    private static final Identifier TEXTURE_STR = SilkySong.id("textures/entity/ring.png");

    private static final Vector3f ROTATION_VECTOR = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private final ItemModelManager itemModelManager;
    public SigilEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemModelManager = context.getItemModelManager();
    }

    @Override
    public boolean shouldRender(SigilEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    private void renderTrail(SigilEntity entityIn, float partialTicks, MatrixStack poseStack, VertexConsumerProvider bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn, float addedrot) {
        float multA = 1.0f-entityIn.getSigilDisappear();
        int sampleSize = 30;
        float trailHeight = 1F;
        float sumdist=0.0f;
        Vec3d drawFrom = entityIn.getTrailPosition(0, partialTicks);
        RenderLayer layer = SKrenderLayers.getEntityTranslucentDissolve2(TEXTURE_STR,false);
        VertexConsumer consumer = bufferIn.getBuffer(layer);
        Vec3d dir =entityIn.getVelocity().normalize();
        float yRot = (float) Math.atan2(dir.x, dir.z);
        Consumer<Quaternionf> OLDquaternionfConsumer = rot(yRot,0,0);
        for(int samples = 0; samples < sampleSize; samples++) {
            Vec3d sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            sumdist += (float) sample.distanceTo(drawFrom);
            drawFrom = sample;
        }
        float lowerA = 0.03f;
        drawFrom = entityIn.getTrailPosition(0, partialTicks);
        for(int samples = 0; samples < sampleSize; samples++) {
            Vec3d sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            dir = drawFrom.subtract(sample);
            yRot = (float) Math.atan2(dir.x, dir.z);
            Consumer<Quaternionf> quaternionfConsumer = rot(yRot,0,0);
            Quaternionf $$8 = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
            quaternionfConsumer.accept($$8);
            $$8.transform(TRANSFORM_VECTOR);
            Quaternionf $$9 = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
            OLDquaternionfConsumer.accept($$9);
            $$9.transform(TRANSFORM_VECTOR);
            Vec3d topAngleVec = new Vec3d(new Vector3f(trailHeight, 0, 0).rotate($$8));
            Vec3d bottomAngleVec = new Vec3d(new Vector3f(-trailHeight, 0, 0).rotate($$8));
            Vec3d OLDtopAngleVec = new Vec3d(new Vector3f(trailHeight, 0, 0).rotate($$9));
            Vec3d OLDbottomAngleVec = new Vec3d(new Vector3f(-trailHeight, 0, 0).rotate($$9));

            float u1 = 1.0f-((samples + 1)/ (float) sampleSize);
            float u2 = 1.0f-(samples  / (float) sampleSize);
            Vec3d draw1 = drawFrom;
            Vec3d draw2 = sample;

            MatrixStack.Entry posestack$pose = poseStack.peek();
            Matrix4f matrix4f = posestack$pose.getPositionMatrix();
            consumer.vertex(matrix4f, (float) draw1.x + (float) OLDbottomAngleVec.x, (float) draw1.y + (float) OLDbottomAngleVec.y, (float) draw1.z + (float) OLDbottomAngleVec.z).color(trailR, trailG, trailB, trailA*multA).texture(1.0f,u2).overlay(OverlayTexture.DEFAULT_UV).light(packedLightIn).normal(posestack$pose, 0F, 1.0f, 0.0F);
            consumer.vertex(matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB,  (trailA-lowerA)*multA).texture(1.0f,u1).overlay(OverlayTexture.DEFAULT_UV).light(packedLightIn).normal(posestack$pose, .0F, 1.0f, 0.0F);
            consumer.vertex(matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(trailR, trailG, trailB,  (trailA-lowerA)*multA).texture(0, u1).overlay(OverlayTexture.DEFAULT_UV).light(packedLightIn).normal(posestack$pose, .0F, 1.0f, 0.0F);
            consumer.vertex(matrix4f, (float) draw1.x + (float) OLDtopAngleVec.x, (float) draw1.y + (float) OLDtopAngleVec.y, (float) draw1.z + (float) OLDtopAngleVec.z).color(trailR, trailG, trailB,  trailA*multA).texture(0, u2).overlay(OverlayTexture.DEFAULT_UV).light(packedLightIn).normal(posestack$pose, 0F, 1.0f, 0.0F);
            drawFrom = sample;
            trailA-=lowerA;
            OLDquaternionfConsumer = quaternionfConsumer;
        }
    }
    private Consumer<Quaternionf> rot(float yRot, float xRot,float zRot)
    {
        return (p_253347_) -> {
            p_253347_.mul(((new Quaternionf()).rotationYXZ(yRot,zRot,xRot)));
        };
    }
    @Override
    public void render(SigilEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (state.entity.hasTrail()) {
            float tickDelta = state.tickDelta;
            Vec3d vec3d = state.entity.getLerpedPos(tickDelta);

            matrices.push();
            matrices.translate(-vec3d.x,-vec3d.y,-vec3d.z);
            Vec3d delta = Vec3d.ZERO;
            matrices.translate(delta.x, delta.y, delta.z);
            renderTrail(state.entity,0,matrices,vertexConsumers,state.entity.getId()/1000.0f,1,1,1f,light,0);
            matrices.pop();
        }
        super.render(state, matrices, vertexConsumers, light);
    }
//    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light,float a,float r, float g, float b) {
//        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(r,1,b,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalY, (float)normalZ);
//    }
    public void updateRenderState(SigilEntity entity, SigilEntityRenderState flyingItemEntityRenderState, float f) {
        flyingItemEntityRenderState.entity = entity;
        flyingItemEntityRenderState.tickDelta = f;
        super.updateRenderState(entity, flyingItemEntityRenderState, f);
    }
    @Override
    public SigilEntityRenderState createRenderState() {
        return new SigilEntityRenderState();
    }
}
