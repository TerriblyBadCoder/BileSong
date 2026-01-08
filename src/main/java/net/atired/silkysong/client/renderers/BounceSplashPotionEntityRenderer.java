package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.renderstates.BouncyPotionRenderState;
import net.atired.silkysong.entities.BounceSplashPotionEntity;
import net.atired.silkysong.misc.IcoSphere;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;

public class BounceSplashPotionEntityRenderer<T extends BounceSplashPotionEntity> extends EntityRenderer<T, BouncyPotionRenderState> {
    private final ItemModelManager itemModelManager;

    public BounceSplashPotionEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemModelManager = context.getItemModelManager();
    }

    @Override
    public void render(BouncyPotionRenderState bouncyPotionRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(bouncyPotionRenderState, matrixStack, vertexConsumerProvider, light);
        matrixStack.push();
        matrixStack.scale(1.0f, 1.0f, 1.0f);
        matrixStack.multiply(this.dispatcher.getRotation());
        bouncyPotionRenderState.itemRenderState.render(matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
            
            
            
            
        float alphasin = bouncyPotionRenderState.lerpThingie;
        RenderLayer layer = SKrenderLayers.getEntityTranslucentEmissive2(EndPortalBlockEntityRenderer.SKY_TEXTURE,false);
        VertexConsumer consumer = vertexConsumerProvider.getBuffer(layer);
        IcoSphere sphere = SilkySongClient.SPHERE;
        float time = 0;
        if(MinecraftClient.getInstance().world!=null){
            time=(MinecraftClient.getInstance().world.getTime()%8000)/800f;
        }
        Vector3f col =bouncyPotionRenderState.colours==null?new Vector3f(1,1,1):bouncyPotionRenderState.colours;
        float yaw = 0.0f;
        for (int i = 0; i < sphere.index.size(); i+=3) {
            Vec3d one = sphere.pos.get(sphere.index.get(i)).multiply(1.8);
            Vec3d two = sphere.pos.get(sphere.index.get(i + 1)).multiply(1.8);
            Vec3d three = sphere.pos.get(sphere.index.get(i + 2)).multiply(1.8);

            one = one.rotateY(time*20).rotateX(0.4f);
            two = two.rotateY(time*20).rotateX(0.4f);
            three = three.rotateY(time*20).rotateX(0.4f);
            float multed1 = 0.8f;
            float multed2 = 0.8f;
            float multed3 = 0.8f;
            one = one.multiply(multed1);
            two = two.multiply(multed2);
            three = three.multiply(multed3);
            List<Vec3d> normalised = List.of(one.normalize().rotateY(yaw),two.normalize().rotateY(yaw),three.normalize().rotateY(yaw));
            matrixStack.push();
            matrixStack.translate(0,0.15,0);
            MatrixStack.Entry peek = matrixStack.peek();

            vertex(peek,consumer, (float) one.getX(),(float)one.getY(),(float)one.getZ(),0.0f,0.0f,(float)normalised.get(0).getX(),(float)normalised.get(0).getY(),(float)normalised.get(0).getZ(), 255,alphasin,col.x,col.y,col.z);
            vertex(peek,consumer, (float) two.getX(),(float)two.getY(),(float)two.getZ(),0.0f,0.0f,(float)normalised.get(1).getX(),(float)normalised.get(1).getY(),(float)normalised.get(1).getZ(), 255,alphasin,col.x,col.y,col.z);
            vertex(peek,consumer, (float) three.getX(),(float)three.getY(),(float)three.getZ(),0.0f,0.0f,(float)normalised.get(2).getX(),(float)normalised.get(2).getY(),(float)normalised.get(2).getZ(),255, alphasin,col.x,col.y,col.z);
            vertex(peek,consumer, (float) one.getX(),(float)one.getY(),(float)one.getZ(),0.0f,0.0f,(float)normalised.get(0).getX(),(float)normalised.get(0).getY(),(float)normalised.get(0).getZ(), 255,alphasin,col.x,col.y,col.z);
            matrixStack.pop();
        }
    }
    public void updateRenderState(T entity, BouncyPotionRenderState bouncyPotionRenderState, float f) {
        super.updateRenderState(entity, bouncyPotionRenderState, f);
        if(entity.getStack().get(DataComponentTypes.POTION_CONTENTS).getColor()!=-1){
            int col = entity.getStack().get(DataComponentTypes.POTION_CONTENTS).getColor();
            bouncyPotionRenderState.colours = new Vector3f((col & 0xFF0000)>>16,(col & 0x00FF00)>>8,(col & 0x0000FF)).div(255.0f);
        }
        bouncyPotionRenderState.lerpThingie = entity.getLerp();
        this.itemModelManager.updateForNonLivingEntity(bouncyPotionRenderState.itemRenderState, ((FlyingItemEntity)entity).getStack(), ItemDisplayContext.GROUND, entity);
    }
    @Override
    public BouncyPotionRenderState createRenderState() {
        return new BouncyPotionRenderState();
    }

    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalZ, float normalY, int light, float a, float r, float g, float b) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(r,g,b,a*0.5f).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalZ, (float)normalY);
    }
}
