package net.atired.silkysong.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.silkysong.SilkySong;
import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.accessors.LivingRenderStateStringBoostAccessor;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import net.atired.silkysong.init.SKstatusEffectInit;
import net.atired.silkysong.statuseffects.HemoRageEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Unique
    private static final Identifier TEXTURE_STR = SilkySong.id("textures/entity/string.png");
    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",at=@At("HEAD"))
    private void renderstateEvil(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci){
        if(livingEntity instanceof HostileEntityStringBoostAccessor accessor){
            if(livingEntityRenderState instanceof LivingRenderStateStringBoostAccessor livingRenderStateStringBoostAccessor){
                livingRenderStateStringBoostAccessor.silkySong$setStringBoost(accessor.getStringBoost()+accessor.getStringBoost2()*5.0f);
                livingRenderStateStringBoostAccessor.silkySong$setHemorrhage(accessor.getHemorrhage());
                livingRenderStateStringBoostAccessor.silkySong$setStrings(accessor.silkySong$getDunnoStringCount());
            }
        }
    }
    @ModifyReturnValue(method = "getRenderLayer",at=@At("RETURN"))
    private RenderLayer renderLayerEh(@Nullable RenderLayer original,S state, boolean showBody, boolean translucent, boolean showOutline){
        Identifier identifier = this.getTexture(state);
        if(state instanceof LivingRenderStateStringBoostAccessor accessor&&accessor.silkySong$getHemorrhage()>0.2){
            return SKrenderLayers.getPlayerTranslucentBleed(identifier,false);
        }
        return original;
    }
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",at=@At("HEAD"))
    private void preRender(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        if(livingEntityRenderState instanceof LivingRenderStateStringBoostAccessor accessor&&accessor.silkySong$getHemorrhage()>0.2) {
            DynamicTESTUniforms.set(new Vector3f(accessor.silkySong$getHemorrhage(), accessor.silkySong$getHemorrhage() * 10.0f, accessor.silkySong$getHemorrhage()), accessor.silkySong$getHemorrhage()*20.0f-1.0f);
            SilkySongClient.PROXY.buffer = DynamicTESTUniforms.BUFFER;
        }
    }
    @Shadow public abstract Identifier getTexture(S state);
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",at=@At("TAIL"))
    private void renderEvil(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){

        RenderLayer layer = SKrenderLayers.getEntityTranslucentDissolve(TEXTURE_STR,false);
        VertexConsumer consumer = vertexConsumerProvider.getBuffer(layer);
        LivingRenderStateStringBoostAccessor livingRenderStateStringBoostAccessor = (LivingRenderStateStringBoostAccessor)livingEntityRenderState;
        Quaternionf quaternionf = new Quaternionf().rotateXYZ(0,-MinecraftClient.getInstance().player.getYaw()/180*3.14f,0);
        Vector3f dirProper =new Vector3f(1,0,0).rotate(quaternionf).mul(1/50.0f);
        for (int j = 0; j < livingRenderStateStringBoostAccessor.silkySong$getStrings(); j++) {
            matrixStack.push();
            Vec3d vec3d = new Vec3d(-Math.cos(-livingEntityRenderState.age/25.0-j/1.2f),0.0,Math.sin(-livingEntityRenderState.age/25.0+j/1.2f)).multiply(0.2);
            matrixStack.translate(0,livingEntityRenderState.height*0.95,0);
            matrixStack.translate(vec3d);
            MatrixStack.Entry entry = matrixStack.peek();

            vertex(entry,consumer,-dirProper.x,0,-dirProper.z,j/6.0f,1,0,0,1,i,1.0f,new Vec3d(1.0,0.5,0.2));
            vertex(entry,consumer,dirProper.x,0,dirProper.z,(j+1)/6.0f,1,0,0,1,i,1.0f,new Vec3d(1.0,0.5,0.2));
            vertex(entry,consumer,dirProper.x-(float)vec3d.x*0.1f,2*1.0f,dirProper.z-(float)vec3d.z*0.1f,(j+1)/6.0f,0,0,0,1,i,0.0f,new Vec3d(1.0,0.0,0.0));
            vertex(entry,consumer,-dirProper.x-(float)vec3d.x*0.1f,2*1.0f,-dirProper.z-(float)vec3d.z*0.1f,j/6.0f,0,0,0,1,i,0.0f,new Vec3d(1.0,0.0,0.0));
            matrixStack.pop();
        }
        float strung = livingRenderStateStringBoostAccessor.silkySong$getStringBoost();
        if(strung<0.1f) return;
        stringyRender(consumer,matrixStack,strung,dirProper,livingEntityRenderState,i);
        if(strung>1.0f){
            strung-=1.0f;
            if(strung<0.1f) return;
            stringyRender(consumer,matrixStack,strung,dirProper,livingEntityRenderState,i);
        }

    }
    private void stringyRender(VertexConsumer consumer,MatrixStack matrixStack,float strung,Vector3f dirProper,LivingEntityRenderState livingEntityRenderState,int i){
        for (int j = 0; j < 3; j++) {
            matrixStack.push();
            Vec3d vec3d = new Vec3d(-Math.cos(-livingEntityRenderState.age/25.0-j/1.2f),0.0,Math.sin(-livingEntityRenderState.age/25.0+j/1.2f)).multiply(0.2);
            matrixStack.translate(0,livingEntityRenderState.height*0.95,0);
            matrixStack.translate(vec3d);
            MatrixStack.Entry entry = matrixStack.peek();

            vertex(entry,consumer,-dirProper.x,0,-dirProper.z,j/6.0f,1,0,0,1,i,Math.min(strung,1.0f),new Vec3d(1,1,1));
            vertex(entry,consumer,dirProper.x,0,dirProper.z,(j+1)/6.0f,1,0,0,1,i,Math.min(strung,1.0f),new Vec3d(1,1,1));
            vertex(entry,consumer,dirProper.x-(float)vec3d.x*0.1f,2*strung,dirProper.z-(float)vec3d.z*0.1f,(j+1)/6.0f,0,0,0,1,i,0.0f,new Vec3d(1,1,1));
            vertex(entry,consumer,-dirProper.x-(float)vec3d.x*0.1f,2*strung,-dirProper.z-(float)vec3d.z*0.1f,j/6.0f,0,0,0,1,i,0.0f,new Vec3d(1,1,1));
            matrixStack.pop();
        }
    }
    @Unique
    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light,float a,Vec3d colours) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color((float)colours.x,(float)colours.y,(float)colours.z,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalY, (float)normalZ);
    }
}
