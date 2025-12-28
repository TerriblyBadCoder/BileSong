package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.models.BileMosqoModel;
import net.atired.silkysong.client.models.MosqoOverlayFeature;
import net.atired.silkysong.client.renderstates.BileMosqoEntityRenderState;
import net.atired.silkysong.entities.BileMosqoEntity;
import net.atired.silkysong.misc.IcoSphere;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BileMosqoEntityRenderer extends MobEntityRenderer<BileMosqoEntity, BileMosqoEntityRenderState, BileMosqoModel<BileMosqoEntityRenderState>> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/bilemosqo.png");
    public BileMosqoEntityRenderer(EntityRendererFactory.Context context, BileMosqoModel<BileMosqoEntityRenderState> entityModel, float f) {
        super(context, entityModel, f);
        this.addFeature(new MosqoOverlayFeature<>(this));
        this.shadowOpacity = 0.9f;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(BileMosqoEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return RenderLayer.getEntityTranslucent(TEXTURE,false);
    }

    @Override
    public void render(BileMosqoEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int i2) {
        super.render(livingEntityRenderState, matrixStack, vertexConsumers, i2);
        if(livingEntityRenderState.health<=6.0f&&livingEntityRenderState.fallticks>5){
            float alphasin = (livingEntityRenderState.fallticks+(float)(-Math.floor(livingEntityRenderState.age)+livingEntityRenderState.age))/20.0f;
            alphasin = Math.min(alphasin,1.0f);
            RenderLayer layer = SKrenderLayers.getEntityTranslucentEmissive2(EndPortalBlockEntityRenderer.SKY_TEXTURE,false);
            VertexConsumer consumer = vertexConsumers.getBuffer(layer);
            IcoSphere sphere = SilkySongClient.SPHERE;
            float time = 0;
            if(MinecraftClient.getInstance().world!=null){
                time=(MinecraftClient.getInstance().world.getTime()%8000)/800f;
            }
            for (int i = 0; i < sphere.index.size(); i+=3) {
                Vec3d one = sphere.pos.get(sphere.index.get(i)).multiply(1.8*alphasin);
                Vec3d two = sphere.pos.get(sphere.index.get(i + 1)).multiply(1.8*alphasin);
                Vec3d three = sphere.pos.get(sphere.index.get(i + 2)).multiply(1.8*alphasin);

                one = one.rotateY(time*20).rotateX(0.4f);
                two = two.rotateY(time*20).rotateX(0.4f);
                three = three.rotateY(time*20).rotateX(0.4f);
                float multed1 = 1.1f+(float) (Math.sin(one.getX()*3.0f)*Math.sin(one.getY()*4.0f)*Math.sin(one.getZ()*3.0f+time))/3.5f;
                float multed2 = 1.1f+(float) (Math.sin(two.getX()*3.0f)*Math.sin(two.getY()*4.0f)*Math.sin(two.getZ()*3.0f+time))/3.5f;
                float multed3 = 1.1f+(float) (Math.sin(three.getX()*3.0f)*Math.sin(three.getY()*4.0f)*Math.sin(three.getZ()*3.0f+time))/3.5f;
                one = one.multiply(multed1);
                two = two.multiply(multed2);
                three = three.multiply(multed3);
                List<Vec3d> normalised = List.of(one.normalize(),two.normalize(),three.normalize());
                matrixStack.push();
                matrixStack.translate(0,0.15,0);
                MatrixStack.Entry peek = matrixStack.peek();
                vertex(peek,consumer, (float) one.getX(),(float)one.getY(),(float)one.getZ(),0.0f,0.0f,(float)normalised.get(0).getX(),(float)normalised.get(0).getY(),(float)normalised.get(0).getZ(), 255,1.0f);
                vertex(peek,consumer, (float) two.getX(),(float)two.getY(),(float)two.getZ(),0.0f,0.0f,(float)normalised.get(1).getX(),(float)normalised.get(1).getY(),(float)normalised.get(1).getZ(), 255,1.0f);
                vertex(peek,consumer, (float) three.getX(),(float)three.getY(),(float)three.getZ(),0.0f,0.0f,(float)normalised.get(2).getX(),(float)normalised.get(2).getY(),(float)normalised.get(2).getZ(),255, 1.0f);
                vertex(peek,consumer, (float) one.getX(),(float)one.getY(),(float)one.getZ(),0.0f,0.0f,(float)normalised.get(0).getX(),(float)normalised.get(0).getY(),(float)normalised.get(0).getZ(), 255,1.0f);
                matrixStack.pop();
            }
        }
    }

    @Override
    protected float getShadowRadius(BileMosqoEntityRenderState livingEntityRenderState) {
        return super.getShadowRadius(livingEntityRenderState);
    }

    @Override
    public void updateRenderState(BileMosqoEntity livingEntity, BileMosqoEntityRenderState livingEntityRenderState, float f) {
        livingEntityRenderState.velocity = (float) livingEntity.getVelocity().length();
        livingEntityRenderState.health = livingEntity.getHealth();
        livingEntityRenderState.fallticks = livingEntity.fallTicks;
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
    }

    @Override
    protected float getShadowOpacity(BileMosqoEntityRenderState state) {
        return super.getShadowOpacity(state);
    }
    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalZ, float normalY, int light,float a) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(0.6f,1,0.6f,1.0f).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix, (float)normalX, (float)normalY, (float)normalZ);
    }
    @Override
    public Identifier getTexture(BileMosqoEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public BileMosqoEntityRenderState createRenderState() {
        return new BileMosqoEntityRenderState();
    }
}
