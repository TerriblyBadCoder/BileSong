package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.util.Identifier;

public class BloodArrowRenderer extends ArrowEntityRenderer {

    public static final Identifier TEXTURE = SilkySong.id("textures/entity/blood_arrow.png");

    public BloodArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTexture(ArrowEntityRenderState arrowEntityRenderState) {
        return TEXTURE;
    }
}
