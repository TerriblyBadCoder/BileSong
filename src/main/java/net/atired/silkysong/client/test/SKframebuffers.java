package net.atired.silkysong.client.test;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.texture.GlTextureView;
import net.minecraft.client.util.Handle;

public class SKframebuffers {

    private static Framebuffer framebufferSky;
    public static Framebuffer getFramebufferSky() {

        if (framebufferSky == null) {
            framebufferSky = new SimpleFramebuffer("evilSky",MinecraftClient.getInstance().getFramebuffer().viewportWidth, MinecraftClient.getInstance().getFramebuffer().viewportHeight,true);

        }
        else if(framebufferSky.viewportHeight != MinecraftClient.getInstance().getFramebuffer().viewportHeight||framebufferSky.viewportWidth!=MinecraftClient.getInstance().getFramebuffer().viewportWidth){
            framebufferSky = new SimpleFramebuffer("evilSky",MinecraftClient.getInstance().getFramebuffer().viewportWidth, MinecraftClient.getInstance().getFramebuffer().viewportHeight,true);

        }
        Handle<Framebuffer> test = null;
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, ((GlTextureView)framebufferSky.getColorAttachmentView()).texture().getGlId());
        return framebufferSky;
    }
}
