package net.atired.silkysong.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.silkysong.SilkySong;
import net.atired.silkysong.init.SKItems;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.awt.*;

public class AmadunnoDrawCallback implements HudRenderCallback {

    private static final Identifier SILK_CD_LOWER = SilkySong.id("textures/gui/sprites/misc/silk_lower_cd.png");
    private static final Identifier SILK_CD = SilkySong.id("textures/gui/sprites/misc/silk_cd.png");
    private static final Identifier VIGNETTE_TEXTURE = Identifier.ofVanilla("textures/misc/vignette.png");
    private static final Identifier TEXTURE_GUARD = SilkySong.id("textures/gui/sprites/misc/guard.png");
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if(MinecraftClient.getInstance().player!=null&&MinecraftClient.getInstance().player.getItemCooldownManager().isCoolingDown(SKItems.SILKEN_MANTLE.getDefaultStack())){
            float cd = (MinecraftClient.getInstance().player.getItemCooldownManager().getCooldownProgress(SKItems.SILKEN_MANTLE.getDefaultStack(),renderTickCounter.getTickProgress(false)));
            if(cd<0.02){

            }
            else{
                int alpha = (int)(cd*255);
                alpha = Math.min(alpha*2,255);
                drawContext.getMatrices().translate(-17f,0);
                int x=drawContext.getScaledWindowWidth()/2;
                int y=drawContext.getScaledWindowHeight()/2-8;
                float sinused = (float) (Math.sin((MinecraftClient.getInstance().world.getTime()%24000)/20.0f)/8.0f);
                drawContext.getMatrices().rotateAbout(sinused,x+17f,y+8);
                drawContext.drawTexture(RenderPipelines.GUI_TEXTURED,SILK_CD_LOWER,x,y,0,0,33,16,33,16, Color.getHSBColor(1.0f,0.0f,1.0f).getRGB()+(alpha/2<<24));

                drawContext.drawTexture(RenderPipelines.GUI_TEXTURED,SILK_CD,x,y,0,0,33,(int)((cd)*8),33,16, Color.getHSBColor(1.0f,0.0f,1.0f).getRGB()+(alpha<<24));

                drawContext.drawTexture(RenderPipelines.GUI_TEXTURED,SILK_CD,x,y+(int)((1.0f-cd)*8)+7,0,(int)((1.0f-cd)*8)+8,33,(int)((cd)*8),33,16, Color.getHSBColor(1.0f,0.0f,1.0f).getRGB()+(alpha<<24));
                drawContext.getMatrices().rotateAbout(-sinused,x+17,y+8);
                drawContext.getMatrices().translate(17f,0);
            }

        }
        Vector3f col = new Vector3f(0.15f,1.0f,1.0f);
        if(SilkySongClient.PROXY.colours.length()>=0.1){
            col=SilkySongClient.PROXY.colours;
        }
        if(SilkySongClient.PROXY.slopped>0.0f) {
            SilkySongClient.PROXY.slopped = SilkySongClient.PROXY.slopped - (1.1f / (float) MinecraftClient.getInstance().getCurrentFps());
            if (SilkySongClient.PROXY.slopped < 0.0f) SilkySongClient.PROXY.slopped = 0.0f;
        }
        if(SilkySongClient.PROXY.vingette>0.0f){
            SilkySongClient.PROXY.vingette = SilkySongClient.PROXY.vingette-(1.4f/(float)MinecraftClient.getInstance().getCurrentFps());
            if(SilkySongClient.PROXY.vingette<0.0f) SilkySongClient.PROXY.vingette = 0.0f;
            int alpha2 = (int) (SilkySongClient.PROXY.vingette*255f)/3;
            if(alpha2>10&&alpha2<100)
                drawContext.drawTexture(RenderPipelines.GUI_TEXTURED,VIGNETTE_TEXTURE,0,0,0,0
                    ,drawContext.getScaledWindowWidth(),drawContext.getScaledWindowHeight(),
                    drawContext.getScaledWindowWidth(),
                    drawContext.getScaledWindowHeight(), Color.getHSBColor(col.x,col.y,col.z).getRGB()+((alpha2)<<24));

        }
        if(MinecraftClient.getInstance().player!=null&&MinecraftClient.getInstance().player.getActiveItem().getItem()== SKItems.AMADUNNO){
            PlayerEntity player = MinecraftClient.getInstance().player;
            int x = drawContext.getScaledWindowWidth()/2;
            int y = drawContext.getScaledWindowHeight()/2;
            int v = 19;
            float usetime = Math.clamp((player.getItemUseTime()-8+renderTickCounter.getDynamicDeltaTicks())*1.7f,0,15);

            float use2=Math.max(15.0f-usetime,5f);
            int alpha = (int) (Math.min(use2,player.getItemUseTime()/4.0f*15.0f)*255f/15f);
            alpha = Math.min(alpha,200);
            float floatyy = (float)Math.sin(usetime/15.0f*2.0f*3.14f)*alpha/55.0f;
            float floatx = -(float)Math.pow(usetime*4.0f,0.5);
            if(usetime<=0){
                v = 38;

            }
            else if(usetime>10){
                v = 0;
            }
            if(usetime>0){
                floatx-=2;
            }
            drawContext.getMatrices().pushMatrix();
            drawContext.getMatrices().translate(floatx,floatyy);
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED,TEXTURE_GUARD,x-23,y-8,0,v,23,19,46,57, Color.getHSBColor(1.0f,0.0f,1.0f).getRGB()+(alpha<<24));
            drawContext.getMatrices().popMatrix();
            drawContext.getMatrices().pushMatrix();
            drawContext.getMatrices().translate(-floatx,floatyy);
            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED,TEXTURE_GUARD,x,y-8,23,v,23,19,46,57,  Color.getHSBColor(1.0f,0.0f,1.0f).getRGB()+(alpha<<24));
            drawContext.getMatrices().popMatrix();

        }
       }
}
