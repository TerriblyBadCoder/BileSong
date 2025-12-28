package net.atired.silkysong.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "bindDefaultUniforms",at = @At("HEAD"))
    private static void mixinUniforms(RenderPass pass, CallbackInfo ci){
        GpuBuffer gpuBufferSlice = DynamicTESTUniforms.BUFFER;
        if (gpuBufferSlice != null) {
            pass.setUniform("DynamicTESTUniforms", gpuBufferSlice);
        }
    }
}
