package net.atired.silkysong.client;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.accessors.GameRendererPoolAccessor;
import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.client.models.*;
import net.atired.silkysong.client.renderers.*;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import net.atired.silkysong.init.SKblockInit;
import net.atired.silkysong.init.SKentityTypes;
import net.atired.silkysong.init.SKparticlesInit;
import net.atired.silkysong.misc.IcoSphere;
import net.atired.silkysong.misc.SKgetDatNoise;
import net.atired.silkysong.networking.payloads.*;
import net.atired.silkysong.particles.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.awt.*;

public class SilkySongClient implements ClientModInitializer {
    public static SKclientProxy PROXY = new SKclientProxy();
    public static IcoSphere SPHERE= IcoSphere.MakeIcosphere(1);
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new AmadunnoDrawCallback());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex)->{
            float noisy = Math.min(state.get(Properties.AGE_7)/9.0f,1.0f);
            return Color.HSBtoRGB(1.0f,(1.0f-noisy)*0.7f,1.0f);
        },SKblockInit.HEMOGLOBIN_BLOCK);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.FROGSPEW_PARTICLE,FrogSpewParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.PARRY_PARTICLE,ParryParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.SIGIL_PARTICLE, SigilParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.STARRY_PARTICLE, StarsParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.SILK_PARTICLE, SilkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.BLOOD_PARTICLE, BloodParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.JUMP_GLOB_PARTICLE, GlobParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SKparticlesInit.MIRROR_PARTICLE, MirrorParticle.Factory::new);
        WorldRenderEvents.END.register((context)->{
            
            if(MinecraftClient.getInstance()!=null&&MinecraftClient.getInstance().player instanceof HostileEntityStringBoostAccessor accessor){
                SilkySongClient.PROXY.silky = MathHelper.lerp(0.1f,SilkySongClient.PROXY.silky,accessor.getStringBoost());
                float silk = SilkySongClient.PROXY.silky;
                DynamicTESTUniforms.set(new Vector3f(accessor.getHemorrhage(), silk,PROXY.slopped), accessor.getHemorrhage());
                SilkySongClient.PROXY.buffer = DynamicTESTUniforms.BUFFER;
                if(context.gameRenderer() instanceof GameRendererPoolAccessor accessor2){
                    PostEffectProcessor postEffectProcessor = MinecraftClient.getInstance().getShaderLoader().loadPostEffect(SilkySong.id("evil_blur") , DefaultFramebufferSet.MAIN_ONLY);
                    if (postEffectProcessor != null) {

                        postEffectProcessor.render(MinecraftClient.getInstance().getFramebuffer(), accessor2.getPool());
                    }
                    if(silk>0.1f){
                        postEffectProcessor = MinecraftClient.getInstance().getShaderLoader().loadPostEffect(SilkySong.id("silken") , DefaultFramebufferSet.MAIN_ONLY);
                        if (postEffectProcessor != null) {
                            postEffectProcessor.render(MinecraftClient.getInstance().getFramebuffer(), accessor2.getPool());
                        }
                    }

                    if(SilkySongClient.PROXY.slopped>0.01f){

                        postEffectProcessor = MinecraftClient.getInstance().getShaderLoader().loadPostEffect(SilkySong.id("curled_child") , DefaultFramebufferSet.MAIN_ONLY);
                        if (postEffectProcessor != null) {
                            postEffectProcessor.render(MinecraftClient.getInstance().getFramebuffer(), accessor2.getPool());
                        }
                    }

                }
            }
        });
        BlockRenderLayerMap.putBlock(SKblockInit.HEMOGLOBIN_BLOCK,BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(SKblockInit.TRASH_CUBE,BlockRenderLayer.CUTOUT);
        EntityRendererRegistry.register(SKentityTypes.TRASHCUBE, TrashCubeEntityRenderer::new);
        EntityRendererRegistry.register(SKentityTypes.BLOOD_ARROW, BloodArrowRenderer::new);
        EntityRendererRegistry.register(SKentityTypes.SIGIL, SigilEntityRenderer::new);
        EntityRendererRegistry.register(SKentityTypes.DECOAGULATOR, DecoagulatorEntityRenderer::new);
        EntityRendererRegistry.register(SKentityTypes.ICOSPHERE, IcosphereEntityRenderer::new);
        EntityRendererRegistry.register(SKentityTypes.BOUNCING_POTION, BounceSplashPotionEntityRenderer::new);
        EntityRendererRegistry.register(SKentityTypes.BOY, (context)->{return new PuppetEntityRenderer(context,new PuppetEntityModel<>(PuppetEntityModel.getTexturedModelData().createModel()),0.4f);});
        EntityRendererRegistry.register(SKentityTypes.HUNTER, (context)->{return new HunterEntityRenderer(context, EntityModelLayers.SKELETON_INNER_ARMOR, EntityModelLayers.SKELETON_OUTER_ARMOR);});
        EntityRendererRegistry.register(SKentityTypes.HEMOGOBLIN, (context)->{return new HemogoblinEntityRenderer(context,new HemoGoblinModel<>(HemoGoblinModel.getTexturedModelData().createModel()),0.6f);});
        EntityRendererRegistry.register(SKentityTypes.HEMOGLOBULE, (context)->{return new HemoGlobuleRenderer(context,new HemoglobuleModel(HemoglobuleModel.getTexturedModelData().createModel()),0.6f);});
        EntityRendererRegistry.register(SKentityTypes.MISSILETOAD, (context)->{return new MissileToadEntityRenderer(context,new MissileToadModel<>(MissileToadModel.getTexturedModelData().createModel()),0.6f);});
        EntityRendererRegistry.register(SKentityTypes.JUMPSCARER, (context)->{return new JumpscareEntityRenderer(context,new JumpscareEntityModel<>(JumpscareEntityModel.getTexturedModelData().createModel()),0.6f);});

        EntityRendererRegistry.register(SKentityTypes.MAN, (context)->{return new ManEntityRenderer(context,new ManModel<>(ManModel.getTexturedModelData().createModel()),0.2f);});
        EntityRendererRegistry.register(SKentityTypes.BILEMOSQO, (context)->{return new BileMosqoEntityRenderer(context,new BileMosqoModel<>(BileMosqoModel.getTexturedModelData(0.0f).createModel()),0.3f);});
        ClientPlayNetworking.registerGlobalReceiver(ParryPayload.ID, new ParryPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(HoisterPayload.ID, new HoisterPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(HemoRagePayload.ID, new HemoRagePayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(SigilPayload.ID, new SigilPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(VelSyncPayload.ID, new VelSyncPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(SilkenCloakPayload.ID, new SilkenCloakPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(CubePayload.ID, new CubePayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(MissiletoadPayload.ID, new MissiletoadPayload.Receiver());
    }
}
