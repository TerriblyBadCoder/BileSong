package net.atired.silkysong.client.renderers;

import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.renderstates.IcosphereEntityRenderState;
import net.atired.silkysong.entities.DecoagulatorEntity;
import net.atired.silkysong.entities.IcosphereEntity;
import net.atired.silkysong.misc.IcoSphere;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class
AbstractIcosphereEntityRenderer<T extends IcosphereEntity, S extends IcosphereEntityRenderState> extends EntityRenderer<IcosphereEntity, IcosphereEntityRenderState> {
    private final ItemModelManager itemModelManager;
    public AbstractIcosphereEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemModelManager = context.getItemModelManager();
    }

    public Vec3d getcolours() {
        return new Vec3d(1.0f,1.0f,1.0f);
    }
    @Override
    public void render(IcosphereEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float alpha = 0.2f;
        float alphasin = 1.0f;
        float r=0.6f;
        float g = 1.0f;
        float b = 0.6f;
        if(state.entity!=null){
            alpha = MathHelper.clamp((state.entity.age-45.0f)/10.0f,0.2f,1.0f);
            alphasin = (float) Math.sin((MathHelper.clamp(state.age-70.0f,0.0f,8.0f))/5.0f*1.8f+MathHelper.PI/9.0f)-0.5f+1.0f;
        }

        if(alphasin>=0.8f&&state.age<70){
            matrices.push();
            matrices.multiply(this.dispatcher.getRotation());
            state.itemRenderState.render(matrices, vertexConsumers, 254, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        }

        RenderLayer layer = SKrenderLayers.getEntityTranslucentEmissive2(EndPortalBlockEntityRenderer.SKY_TEXTURE,false);
        VertexConsumer consumer = vertexConsumers.getBuffer(layer);
        IcoSphere sphere = SilkySongClient.SPHERE;
        float time = 0;
        if(MinecraftClient.getInstance().world!=null){
            time=(MinecraftClient.getInstance().world.getTime()%8000)/800f+alphasin*20.0f;
        }
        for (int i = 0; i < sphere.index.size(); i+=3) {
            Vec3d one = sphere.pos.get(sphere.index.get(i)).multiply(1.8*(alpha*0.8+0.2)*alphasin);
            Vec3d two = sphere.pos.get(sphere.index.get(i + 1)).multiply(1.8*(alpha*0.8+0.2)*alphasin);
            Vec3d three = sphere.pos.get(sphere.index.get(i + 2)).multiply(1.8*(alpha*0.8+0.2)*alphasin);

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
            matrices.push();
            matrices.translate(0,0.15,0);
            MatrixStack.Entry peek = matrices.peek();
            vertex(peek,consumer, (float) one.getX(),(float)one.getY(),(float)one.getZ(),0.0f,0.0f,(float)normalised.get(0).getX(),(float)normalised.get(0).getY(),(float)normalised.get(0).getZ(), 255,alpha,r,g,b);
            vertex(peek,consumer, (float) two.getX(),(float)two.getY(),(float)two.getZ(),0.0f,0.0f,(float)normalised.get(1).getX(),(float)normalised.get(1).getY(),(float)normalised.get(1).getZ(), 255,alpha,r,g,b);
            vertex(peek,consumer, (float) three.getX(),(float)three.getY(),(float)three.getZ(),0.0f,0.0f,(float)normalised.get(2).getX(),(float)normalised.get(2).getY(),(float)normalised.get(2).getZ(),255, alpha,r,g,b);
            vertex(peek,consumer, (float) one.getX(),(float)one.getY(),(float)one.getZ(),0.0f,0.0f,(float)normalised.get(0).getX(),(float)normalised.get(0).getY(),(float)normalised.get(0).getZ(), 255,alpha,r,g,b);
            matrices.pop();
        }
        super.render(state, matrices, vertexConsumers, light);
    }
    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light,float a,float r, float g,float b) {
        a/=2.0f;
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color((float)getcolours().x,(float)getcolours().y,(float)getcolours().z,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalY, (float)normalZ);
    }
    public void updateRenderState(IcosphereEntity entity, IcosphereEntityRenderState flyingItemEntityRenderState, float f) {
        super.updateRenderState(entity, flyingItemEntityRenderState, f);
        flyingItemEntityRenderState.entity = entity;
        this.itemModelManager.updateForNonLivingEntity(flyingItemEntityRenderState.itemRenderState, entity.getStack(), ItemDisplayContext.GROUND, entity);
    }
    @Override
    public IcosphereEntityRenderState createRenderState() {
        return new IcosphereEntityRenderState();
    }
}
