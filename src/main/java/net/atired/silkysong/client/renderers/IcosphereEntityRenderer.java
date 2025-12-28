package net.atired.silkysong.client.renderers;

import net.atired.silkysong.client.renderstates.IcosphereEntityRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class IcosphereEntityRenderer extends AbstractIcosphereEntityRenderer {
    public IcosphereEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
    @Override
    public Vec3d getcolours(){
        return new Vec3d(0.6,1.0,0.6);
    }
}
