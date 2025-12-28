package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.models.ManModel;
import net.atired.silkysong.client.renderstates.ManEntityRenderState;
import net.atired.silkysong.entities.ManEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ManEntityRenderer  extends BipedEntityRenderer<ManEntity, ManEntityRenderState, ManModel<ManEntityRenderState>> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/man.png");
    public ManEntityRenderer(EntityRendererFactory.Context context, ManModel<ManEntityRenderState> entityModel, float f) {
        super(context, entityModel, f);
        this.shadowOpacity = 0.4f;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ManEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return SKrenderLayers.getEntityTranslucentAmbush(TEXTURE,false);
    }

    @Override
    protected float getShadowRadius(ManEntityRenderState livingEntityRenderState) {
        return super.getShadowRadius(livingEntityRenderState);
    }

    @Override
    protected float getShadowOpacity(ManEntityRenderState state) {
        return super.getShadowOpacity(state);
    }

    @Override
    public Identifier getTexture(ManEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public ManEntityRenderState createRenderState() {
        return new ManEntityRenderState();
    }
}
