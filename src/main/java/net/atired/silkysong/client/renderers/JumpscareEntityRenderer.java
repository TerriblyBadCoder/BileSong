package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.client.models.JumpscareEntityModel;
import net.atired.silkysong.client.renderstates.JumpscareEntityRenderState;
import net.atired.silkysong.client.test.DynamicTESTUniforms;
import net.atired.silkysong.entities.JumpscarerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JumpscareEntityRenderer extends BipedEntityRenderer<JumpscarerEntity, JumpscareEntityRenderState, JumpscareEntityModel<JumpscareEntityRenderState>> {
    private static final Identifier TEXTURE = SilkySong.id("textures/entity/jumpscarer.png");

    private final BlockRenderManager blockRenderManager;
    public JumpscareEntityRenderer(EntityRendererFactory.Context context, JumpscareEntityModel<JumpscareEntityRenderState> entityModel, float f) {
        super(context, entityModel, f);
        this.shadowOpacity = 0.4f;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(JumpscareEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(state, showBody, translucent, showOutline);
    }

    @Override
    public void render(JumpscareEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if(MinecraftClient.getInstance().gameRenderer.getCamera()!=null){
            DynamicTESTUniforms.set(new Vec3d(state.x,state.y,state.z).subtract(MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos()).toVector3f(),Math.max(((state.handRaise+(state.hurt?1.0f:0.0f))*5.0f)-1.1f,-0.5f));
            SilkySongClient.PROXY.buffer = DynamicTESTUniforms.BUFFER;
        }
        if(state.deathTime<=0){
            BlockState state1 = state.blockState;
            matrixStack.push();
            matrixStack.translate(0, (state.height-0.2f)*state.revealedAmount, 0);
            Vec3d translated = new Vec3d(state.x,state.y,state.z).subtract(state.currentPos.toCenterPos().subtract(0.5));
            matrixStack.translate(-0.5,0,-0.5);
            matrixStack.scale(1,1,1);
            List<BlockModelPart> list = this.blockRenderManager.getModel(state1).getParts(Random.create(0));
            this.blockRenderManager.getModelRenderer().render(state, list, state1,state.currentPos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(state1)), false, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }
        super.render(state, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected float getShadowRadius(JumpscareEntityRenderState livingEntityRenderState) {
        return super.getShadowRadius(livingEntityRenderState)*(livingEntityRenderState.blockState.isSolid()?1.6f-livingEntityRenderState.revealedAmount*0.6f:1.0f);
    }

    @Override
    public void updateRenderState(JumpscarerEntity mobEntity, JumpscareEntityRenderState bipedEntityRenderState, float f) {
        bipedEntityRenderState.handRaise = MathHelper.lerp(f,mobEntity.oldHandRaise,mobEntity.handRaise);
        bipedEntityRenderState.revealedAmount = MathHelper.lerp(f,mobEntity.oldHiding,mobEntity.getHidingValue());
        bipedEntityRenderState.world = mobEntity.getWorld();
        BlockPos blockPos = BlockPos.ofFloored(mobEntity.getX(), mobEntity.getBodyY(0.7), mobEntity.getZ());
        bipedEntityRenderState.currentPos = blockPos;
        bipedEntityRenderState.blockState = mobEntity.getState();
        bipedEntityRenderState.biome=mobEntity.getWorld().getBiome(mobEntity.getBlockPos());

        super.updateRenderState(mobEntity, bipedEntityRenderState, f);
    }

    @Override
    protected float getShadowOpacity(JumpscareEntityRenderState state) {
        return super.getShadowOpacity(state)*(state.blockState.isSolid()?1.0f:state.revealedAmount);
    }

    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light, float a, float r, float g, float b) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(r,g,b,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalY, (float)normalZ);
    }
    @Override
    public Identifier getTexture(JumpscareEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public JumpscareEntityRenderState createRenderState() {
        return new JumpscareEntityRenderState();
    }
}
