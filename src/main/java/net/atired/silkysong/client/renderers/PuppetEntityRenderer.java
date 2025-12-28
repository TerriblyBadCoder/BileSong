package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.models.PuppetEntityModel;
import net.atired.silkysong.client.renderstates.PuppetEntityRenderState;
import net.atired.silkysong.entities.PuppetEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.DynamicUniforms;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PuppetEntityRenderer extends BipedEntityRenderer<PuppetEntity, PuppetEntityRenderState, PuppetEntityModel<PuppetEntityRenderState>> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/puppet.png");

    private static final Identifier TEXTURE_STR = SilkySong.id("textures/entity/string.png");
    public PuppetEntityRenderer(EntityRendererFactory.Context context, PuppetEntityModel<PuppetEntityRenderState> entityModel, float f) {
        super(context, entityModel, f);
        this.shadowOpacity = 0.4f;
    }

    @Override
    public void render(PuppetEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);
        if(livingEntityRenderState.deathTime>0){
            return;
        }
        RenderLayer layer = SKrenderLayers.getEntityTranslucentDissolve(TEXTURE_STR,false);
        VertexConsumer consumer = vertexConsumerProvider.getBuffer(layer);
        Quaternionf quaternionf = new Quaternionf().rotateXYZ(0,-MinecraftClient.getInstance().player.getYaw()/180*3.14f,0);
        Vector3f dirProper =new Vector3f(1,0,0).rotate(quaternionf).mul(1/50.0f);
        for (int j = 0; j < 6; j++) {
            matrixStack.push();
            float alphamult = 0.7f;
            Vec3d vec3d = new Vec3d(-Math.cos(-livingEntityRenderState.age/25.0-j/1.2f),0.0,Math.sin(-livingEntityRenderState.age/25.0+j/1.2f)).multiply(0.2);
            if(j<2){
                matrixStack.translate(0,livingEntityRenderState.height*0.95,0);
                matrixStack.translate(vec3d);
            }
            else{
                vec3d = Vec3d.ZERO;
                alphamult = 1.0f;
                if(j==2){
                    matrixStack.translate(new Vec3d(new Vector3f(0.2f,-0.45f,0.38f).rotate(new Quaternionf().rotationZYX((float)Math.sin(livingEntityRenderState.age/20.0f)/10.0f,-livingEntityRenderState.bodyYaw/180*3.14f,this.model.leftLeg.pitch))));
                    matrixStack.translate(0,0.5f,0);
                }
                if(j==3){
                    matrixStack.translate(new Vec3d(new Vector3f(-0.2f,-0.45f,0.38f).rotate(new Quaternionf().rotationZYX((float)Math.cos(livingEntityRenderState.age/20.0f)/10.0f,-livingEntityRenderState.bodyYaw/180*3.14f,this.model.rightLeg.pitch))));
                    matrixStack.translate(0,0.5f,0);
                }
                if(j==4){
                    matrixStack.translate(new Vec3d(new Vector3f(0.4f,-0.45f,-0.03f).rotate(new Quaternionf().rotationZYX(this.model.leftArm.yaw,-livingEntityRenderState.bodyYaw/180*3.14f,this.model.leftArm.pitch))));
                    matrixStack.translate(0,1.0f,0);
                }
                if(j==5){
                    matrixStack.translate(new Vec3d(new Vector3f(-0.4f,-0.45f,-0.03f).rotate(new Quaternionf().rotationZYX(this.model.rightArm.yaw,-livingEntityRenderState.bodyYaw/180*3.14f,this.model.rightArm.pitch))));
                    matrixStack.translate(0,1.0f,0);
                }
                vec3d = new Vec3d(new Vector3f(0,0,0.16f).rotate(new Quaternionf().rotationZYX(this.model.rightArm.yaw,-livingEntityRenderState.bodyYaw/180*3.14f,this.model.rightArm.pitch))).multiply(-14);
                if(j>=4){
                    vec3d = vec3d.multiply(-1.33);
                }
            }
            alphamult*=0.9f;
            MatrixStack.Entry entry = matrixStack.peek();

            vertex(entry,consumer,-dirProper.x,0,-dirProper.z,j/6.0f,alphamult,0,0,1,i,1.0f);
            vertex(entry,consumer,dirProper.x,0,dirProper.z,(j+1)/6.0f,alphamult,0,0,1,i,1.0f);
            vertex(entry,consumer,dirProper.x-(float)vec3d.x*0.1f,3*alphamult,dirProper.z-(float)vec3d.z*0.1f,(j+1)/6.0f,0,0,0,1,i,0.0f);
            vertex(entry,consumer,-dirProper.x-(float)vec3d.x*0.1f,3*alphamult,-dirProper.z-(float)vec3d.z*0.1f,j/6.0f,0,0,0,1,i,0.0f);
            matrixStack.pop();
        }
    }

    @Override
    protected float getShadowRadius(PuppetEntityRenderState livingEntityRenderState) {
        return super.getShadowRadius(livingEntityRenderState);
    }

    @Override
    public void updateRenderState(PuppetEntity mobEntity, PuppetEntityRenderState bipedEntityRenderState, float f) {
        bipedEntityRenderState.velocity = (float) mobEntity.getVelocity().getY();
        super.updateRenderState(mobEntity, bipedEntityRenderState, f);
    }

    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light, float a) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(1,1,1,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalY, (float)normalZ);
    }
    @Override
    protected float getShadowOpacity(PuppetEntityRenderState state) {
        return super.getShadowOpacity(state);
    }

    @Override
    public Identifier getTexture(PuppetEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public PuppetEntityRenderState createRenderState() {
        return new PuppetEntityRenderState();
    }
}
