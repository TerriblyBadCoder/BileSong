package net.atired.silkysong.client.renderers;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.client.SKrenderLayers;
import net.atired.silkysong.client.renderstates.TrashCubeRenderState;
import net.atired.silkysong.entities.TrashCubeEntity;
import net.atired.silkysong.init.SKblockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.CamelEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.Pool;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import org.joml.Quaternionf;

import java.util.List;

public class TrashCubeEntityRenderer extends EntityRenderer<TrashCubeEntity, TrashCubeRenderState> {
    private static final Identifier TEXTURE_FAN = SilkySong.id("textures/block/fan.png");
    private static final Identifier TEXTURE_WIND = SilkySong.id("textures/entity/wind.png");
    private final ItemModelManager itemModelManager;

    private final BlockRenderManager blockRenderManager;
    public TrashCubeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemModelManager = context.getItemModelManager();
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(TrashCubeRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float alpha = 0.2f;
        float alphasin = 1.0f;
        BlockState state1 = SKblockInit.TRASH_CUBE.getDefaultState();
        matrices.push();
        matrices.translate(0, 1, 0);
        matrices.multiply(new Quaternionf().rotationYXZ(state.yaw/90.0f,state.pitch/180*3.14f,0));
        matrices.translate(-1, 0, -1);
        matrices.scale(2,2,2);
        List<BlockModelPart> list = this.blockRenderManager.getModel(state1).getParts(Random.create(0));
        matrices.translate(0,-0.5,0);
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_FAN));
        MatrixStack.Entry entry = matrices.peek();
        Vec3d[] vec3ds = {new Vec3d(-0.5,0.5,0),new Vec3d(0.5,0.5,0),new Vec3d(0.5,-0.5,0),new Vec3d(-0.5,-0.5,0)};
        int i = 0;
        float time = state.age/1.3f;
        for (Vec3d vec3d : vec3ds){
            vec3d = vec3d.rotateZ(-time);
            vec3d = vec3d.multiply(0.76);
            vec3d = vec3d.add(0.5,0.5,0.1);
            vec3ds[i]=vec3d;
            i+=1;
        }
        vertex(entry,consumer,(float)vec3ds[0].x,(float)vec3ds[0].y,(float)vec3ds[0].z,0,0,0,0,-1,light,1.0f,1.0f,1.0f,1.0f);
        vertex(entry,consumer,(float)vec3ds[1].x,(float)vec3ds[1].y,(float)vec3ds[1].z,1,0,0,0,-1,light,1.0f,1.0f,1.0f,1.0f);
        vertex(entry,consumer,(float)vec3ds[2].x,(float)vec3ds[2].y,(float)vec3ds[2].z,1,1,0,0,-1,light,1.0f,1.0f,1.0f,1.0f);
        vertex(entry,consumer,(float)vec3ds[3].x,(float)vec3ds[3].y,(float)vec3ds[3].z,0,1,0,0,-1,light,1.0f,1.0f,1.0f,1.0f);
        this.blockRenderManager.getModelRenderer().render(state, list, state1,state.currentPos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(state1)), false, OverlayTexture.DEFAULT_UV);
        consumer = vertexConsumers.getBuffer(SKrenderLayers.getEntityTranslucentDissolve3(TEXTURE_WIND,false));
        for (int j = 0; j <4; j++) {
            vec3ds = new Vec3d[]{new Vec3d(-1, -1, 1.5), new Vec3d(1, -1, 1.5), new Vec3d(1, -1, 0), new Vec3d(-1, -1, 0),new Vec3d(-1, -0.6, 1.5), new Vec3d(1, -0.6, 1.5)};
            i = 0;
            for (Vec3d vec3d : vec3ds){
                vec3d=vec3d.multiply(0.48);
                vec3d = vec3d.rotateZ(j/2f*(float)Math.PI);
                vec3d = vec3d.add(0.5,.5,-0.7-j/4000.0f);
                vec3ds[i]=vec3d;
                i+=1;
            }

            vertex(entry,consumer,(float)vec3ds[0].x,(float)vec3ds[0].y,(float)vec3ds[0].z,(j+1)/4.0f,0,0,0,-1,light,0.5f,1.0f,1.0f,1.0f);
            vertex(entry,consumer,(float)vec3ds[1].x,(float)vec3ds[1].y,(float)vec3ds[1].z,j/4.0f,0,0,0,-1,light,0.5f,1.0f,1.0f,1.0f);
            vertex(entry,consumer,(float)vec3ds[2].x,(float)vec3ds[2].y,(float)vec3ds[2].z,j/4.0f,1,0,0,-1,light,0.0f,1.0f,1.0f,1.0f);
            vertex(entry,consumer,(float)vec3ds[3].x,(float)vec3ds[3].y,(float)vec3ds[3].z,(j+1)/4.0f,1,0,0,-1,light,0.0f,1.0f,1.0f,1.0f);
            i = 0;
            for (Vec3d vec3d : vec3ds){
                vec3d = vec3d.add(0.0,0.0,-0.1);
                vec3ds[i]=vec3d;
                i+=1;
            }
            vertex(entry,consumer,(float)vec3ds[0].x,(float)vec3ds[0].y,(float)vec3ds[0].z,(j+1)/4.0f,0.6f,0,0,-1,light,0.5f,1.0f,1.0f,1.0f);
            vertex(entry,consumer,(float)vec3ds[1].x,(float)vec3ds[1].y,(float)vec3ds[1].z,j/4.0f,0.6f,0,0,-1,light,0.5f,1.0f,1.0f,1.0f);
            vertex(entry,consumer,(float)vec3ds[5].x,(float)vec3ds[5].y,(float)vec3ds[5].z,j/4.0f,1,0,0,-1,light,0.0f,1.0f,1.0f,1.0f);
            vertex(entry,consumer,(float)vec3ds[4].x,(float)vec3ds[4].y,(float)vec3ds[4].z,(j+1)/4.0f,1,0,0,-1,light,0.0f,1.0f,1.0f,1.0f);


        }
        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }
    public void vertex(MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, int light,float a,float r, float g,float b) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z).color(r,g,b,a).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal((float)normalX, (float)normalY, (float)normalZ);
    }
    public void updateRenderState(TrashCubeEntity entity, TrashCubeRenderState flyingItemEntityRenderState, float f) {
        super.updateRenderState(entity, flyingItemEntityRenderState, f);
        BlockPos blockPos = BlockPos.ofFloored(entity.getX(), entity.getBodyY(0.7), entity.getZ());
        flyingItemEntityRenderState.currentPos = blockPos;
        flyingItemEntityRenderState.world = entity.getWorld();
        flyingItemEntityRenderState.yaw = entity.getCustomYaw();
        flyingItemEntityRenderState.pitch = entity.getCustomPitch();
        flyingItemEntityRenderState.self = entity;
    }
    @Override
    public TrashCubeRenderState createRenderState() {
        return new TrashCubeRenderState();
    }
}
