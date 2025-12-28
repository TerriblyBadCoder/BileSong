package net.atired.silkysong.client.models;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.renderstates.BileMosqoEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MosqoOverlayFeature<T extends BileMosqoEntityRenderState> extends FeatureRenderer<T, BileMosqoModel<T>> {
    private static final Identifier SKIN = SilkySong.id("textures/entity/mosqo_overlay.png");
    private final BileMosqoModel<T> model;

    public MosqoOverlayFeature(FeatureRendererContext<T, BileMosqoModel<T>> context) {
        super(context);
        this.model =  new BileMosqoModel<>(BileMosqoModel.getTexturedModelData(0.2f).createModel());

    }

    @Override
    public BileMosqoModel<T> getContextModel() {
        return super.getContextModel();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T state, float limbAngle, float limbDistance) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(SKIN));
        this.getContextModel().render(matrices, vertexConsumer, 255, OverlayTexture.DEFAULT_UV);
    }
}