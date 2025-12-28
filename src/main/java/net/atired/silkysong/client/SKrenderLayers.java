package net.atired.silkysong.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector3f;
import org.joml.sampling.UniformSampling;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL31;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BiFunction;

public class SKrenderLayers {

    public static final RenderPipeline.Snippet ENTITY_SNIPPET_PLAYER = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withUniform("DynamicTESTUniforms",UniformType.UNIFORM_BUFFER)
            .withVertexShader("core/entity")
            .withFragmentShader("core/silk_player")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_PLAYER = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET_PLAYER)
                    .withLocation("pipeline/entity_translucent_player")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_PLAYER = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>)((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false))
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .texturing(new RenderPhase.Texturing("things",
                                () -> {
                                },()->{}))
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("entity_translucent_player", 1536, true, true, ENTITY_TRANSLUCENT_PLAYER, multiPhaseParameters);
            })
    );
    public static RenderLayer getPlayerTranslucentBleed(Identifier texture) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_PLAYER.apply(texture, false);
    }
    public static RenderLayer getPlayerTranslucentBleed(Identifier texture, boolean affectsOutline) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_PLAYER.apply(texture, affectsOutline);
    }
    public static final RenderPipeline.Snippet ENTITY_SNIPPET_DISSOLVE3 = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withVertexShader("core/entity")
            .withFragmentShader("core/silk_entity_dissolve3")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_DISSOLVE3 = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET_DISSOLVE3)
                    .withLocation("pipeline/entity_translucent_dissolve3")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_DISSOLVE3 = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>)((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false))
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .texturing(new RenderPhase.Texturing("things",
                                () -> {
                                },()->{}))
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("entity_translucent_dissolve3", 1536, true, true, ENTITY_TRANSLUCENT_DISSOLVE3, multiPhaseParameters);
            })
    );

    public static RenderLayer getEntityTranslucentDissolve3(Identifier texture, boolean affectsOutline) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_DISSOLVE3.apply(texture, affectsOutline);
    }
    public static final RenderPipeline.Snippet ENTITY_SNIPPET_DISSOLVE2 = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withVertexShader("core/entity")
            .withFragmentShader("core/silk_entity_dissolve2")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_DISSOLVE2 = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET_DISSOLVE2)
                    .withLocation("pipeline/entity_translucent_dissolve2")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_DISSOLVE2 = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>)((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false))
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .texturing(new RenderPhase.Texturing("things",
                                () -> {
                                },()->{}))
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("entity_translucent_dissolve2", 1536, true, true, ENTITY_TRANSLUCENT_DISSOLVE2, multiPhaseParameters);
            })
    );

    public static RenderLayer getEntityTranslucentDissolve2(Identifier texture, boolean affectsOutline) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_DISSOLVE2.apply(texture, affectsOutline);
    }
    public static final RenderPipeline.Snippet ENTITY_SNIPPET_BLOOD = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withUniform("DynamicTESTUniforms",UniformType.UNIFORM_BUFFER)
            .withVertexShader("core/silk_entity_blood")
            .withFragmentShader("core/entity")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_BLOOD = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET_BLOOD)
                    .withLocation("pipeline/silk_entity_blood")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_BLOOD = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>) ((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false))
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .texturing(new RenderPhase.Texturing("things",
                                () -> {}, () -> {}))
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("silk_entity_blood", 1536, true, true, ENTITY_TRANSLUCENT_BLOOD, multiPhaseParameters);
            })
    );
    public static RenderLayer getEntityTranslucentBlood(Identifier texture) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_BLOOD.apply(texture, false);
    }
    public static final RenderPipeline.Snippet ENTITY_SNIPPET_DISSOLVE = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withUniform("DynamicTESTUniforms",UniformType.UNIFORM_BUFFER)
            .withVertexShader("core/entity")
            .withFragmentShader("core/silk_entity_dissolve")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_DISSOLVE = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET_DISSOLVE)
                    .withLocation("pipeline/entity_translucent_dissolve")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_DISSOLVE = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>) ((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false))
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .texturing(new RenderPhase.Texturing("things",
                                () -> {}, () -> {}))
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("entity_translucent_dissolve", 1536, true, true, ENTITY_TRANSLUCENT_DISSOLVE, multiPhaseParameters);
            })
    );

    public static RenderLayer getEntityTranslucentDissolve(Identifier texture, boolean affectsOutline) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_DISSOLVE.apply(texture, affectsOutline);
    }

    public static final RenderPipeline.Snippet ENTITY_SNIPPET = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withUniform("DynamicTESTUniforms",UniformType.UNIFORM_BUFFER)
            .withVertexShader("core/entity")
            .withFragmentShader("core/silk_entity_ambush")
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_AMBUSH = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET)
                    .withLocation("pipeline/entity_translucent_ambush")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_AMBUSH = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>)((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.Texture(texture, false))
                        .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("entity_translucent_ambush", 1536, true, true, ENTITY_TRANSLUCENT_AMBUSH, multiPhaseParameters);
            })
    );

    public static RenderLayer getEntityTranslucentAmbush(Identifier texture, boolean affectsOutline) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_AMBUSH.apply(texture, affectsOutline);
    }
    public static final RenderPipeline.Snippet ENTITY_INTERSECT_SNIPPET = RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET)
            .withVertexShader("core/entity_intersect")
            .withFragmentShader("core/entity_intersect")
            .withSampler("Sampler0")
            .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS)
            .withShaderDefine("EMISSIVE")
            .buildSnippet();
    public static final RenderPipeline ENTITY_TRANSLUCENT_INTERSECT = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_INTERSECT_SNIPPET)
                    .withLocation("pipeline/entity_translucent_intersect")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .withDepthWrite(false)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> LAYER_ENTITY_TRANSLUCENT_INTERSECT = Util.memoize(
            (BiFunction<Identifier, Boolean, RenderLayer>)((texture, affectsOutline) -> {
                RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder()
                        .texture(new RenderPhase.TextureBase(()->{
                            if(MinecraftClient.getInstance().getFramebuffer()!=null)
                                RenderSystem.setShaderTexture(0,MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView());
                            else{
                                TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                                AbstractTexture abstractTexture = textureManager.getTexture(EndPortalBlockEntityRenderer.SKY_TEXTURE);
                                abstractTexture.setUseMipmaps(false);
                                RenderSystem.setShaderTexture(0, abstractTexture.getGlTextureView());
                            }
                            },()->{}))
                        .overlay(RenderLayer.ENABLE_OVERLAY_COLOR)
                        .build(affectsOutline);
                return RenderLayer.of("entity_translucent_intersect", 1536, true, true, ENTITY_TRANSLUCENT_INTERSECT, multiPhaseParameters);
            })

    );


    public static RenderLayer getEntityTranslucentEmissive2(Identifier texture, boolean affectsOutline) {
        return (RenderLayer) LAYER_ENTITY_TRANSLUCENT_INTERSECT.apply(texture, affectsOutline);
    }

}
