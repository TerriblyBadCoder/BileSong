package net.atired.silkysong.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.PostEffectPipeline;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PostEffectProcessor.class)
public class PostEffectProcessorMixin {
    @WrapOperation(method = "parsePass",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;withUniform(Ljava/lang/String;Lnet/minecraft/client/gl/UniformType;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;",ordinal =0))
    private static RenderPipeline.Builder test(RenderPipeline.Builder instance, String name, UniformType type, Operation<RenderPipeline.Builder> original, TextureManager textureManager, PostEffectPipeline.Pass pass, Identifier id){
//        for (PostEffectPipeline.Input input : pass.inputs()){
//            System.out.println(input.samplerName());
//        }
        instance=instance.withUniform("DynamicTESTUniforms",UniformType.UNIFORM_BUFFER);

        return original.call(instance,name,type);
    }
}
