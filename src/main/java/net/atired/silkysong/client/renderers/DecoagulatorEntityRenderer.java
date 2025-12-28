package net.atired.silkysong.client.renderers;

import net.atired.silkysong.client.renderstates.DecoagulatorEntityRenderState;
import net.atired.silkysong.client.renderstates.IcosphereEntityRenderState;
import net.atired.silkysong.entities.DecoagulatorEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HuskEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class DecoagulatorEntityRenderer extends AbstractIcosphereEntityRenderer<DecoagulatorEntity, DecoagulatorEntityRenderState> {
    public DecoagulatorEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Vec3d getcolours() {
        return new Vec3d(1.0f,0.1f,0.1f);
    }

    @Override
    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light, float a, float r, float g, float b) {
        a = Math.min(a*1.8f+0.2f,2.0f);
        super.vertex(matrix, vertexConsumer, x, y, z, u, v, normalX, normalY, normalZ, light, a, r, g, b);
    }
}
