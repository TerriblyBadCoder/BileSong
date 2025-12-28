package net.atired.silkysong.client.renderers;

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.models.HemoGoblinModel;
import net.atired.silkysong.client.renderstates.ManEntityRenderState;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import net.atired.silkysong.client.test.SKframebuffers;
import net.atired.silkysong.entities.HemogoblinEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.gl.GlCommandEncoder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class HemogoblinEntityRenderer extends BipedEntityRenderer<HemogoblinEntity, BipedEntityRenderState, HemoGoblinModel<BipedEntityRenderState>> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/hemogoblin.png");
    public HemogoblinEntityRenderer(EntityRendererFactory.Context context, HemoGoblinModel<BipedEntityRenderState> entityModel, float f) {
        super(context, entityModel, f);
        this.shadowOpacity = 0.4f;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(BipedEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(state, showBody, translucent, showOutline);
    }

    @Override
    public void render(BipedEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if(MinecraftClient.getInstance().gameRenderer.getCamera()!=null){
            DynamicTESTUniforms.set(new Vec3d(livingEntityRenderState.x,livingEntityRenderState.y,livingEntityRenderState.z).subtract(MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos()).toVector3f(),0.00f);
            SilkySongClient.PROXY.buffer = DynamicTESTUniforms.BUFFER;
        }
        super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);

    }

    @Override
    protected float getShadowRadius(BipedEntityRenderState livingEntityRenderState) {
        return super.getShadowRadius(livingEntityRenderState);
    }

    @Override
    protected float getShadowOpacity(BipedEntityRenderState state) {
        return super.getShadowOpacity(state);
    }

    @Override
    public Identifier getTexture(BipedEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public ManEntityRenderState createRenderState() {
        return new ManEntityRenderState();
    }
}
