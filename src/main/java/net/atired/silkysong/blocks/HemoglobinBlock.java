package net.atired.silkysong.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.silkysong.init.SKblockInit;
import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HemoglobinBlock extends Block {

    public static final MapCodec<HemoglobinBlock> CODEC = createCodec(HemoglobinBlock::new);
    public static final int MAX_AGE = 7;
    public static final IntProperty SCALE;
    public MapCodec<? extends HemoglobinBlock> getCodec() {
        return CODEC;
    }

    public HemoglobinBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(SCALE, 0));
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos,state.with(SCALE,Math.max(state.get(SCALE)-1,0)),2);
        if(world.getBlockState(pos).get(SCALE)>0){
            world.scheduleBlockTick(pos,this,1);
        }
        super.scheduledTick(state, world, pos, random);
    }

    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if(entity instanceof ItemEntity item){
            world.setBlockState(pos, state.with(SCALE, Math.min(state.get(SCALE)+1, 7)), 2);
            if(!world.getBlockTickScheduler().isQueued(pos,this))
                world.scheduleBlockTick(pos,this,5);
            Vec3d normDir = item.getVelocity().normalize();
            normDir = normDir.multiply(Math.abs(normDir.x),Math.abs(normDir.y),Math.abs(normDir.z));
            boolean nextThing = false;
            Direction direction = Direction.getFacing(normDir);
            normDir = direction.getDoubleVector().multiply(0.2);
            if(world.getBlockState(entity.getBlockPos().add(direction.getVector())).getBlock()== SKblockInit.HEMOGLOBIN_BLOCK){
                nextThing=true;
            }
            float turns = (float) Math.atan2(direction.getVector().getX(),direction.getVector().getZ());
            turns /= 3.14f;
            turns*=2.0f;
            if(!nextThing){
                for (Direction a : DIRECTIONS){
                    if(a.getVector().getY()==0){

                        for (int i = 0; i < (int)turns; i++) {
                            a = a.rotateYClockwise();
                        }
                    }
                    if(a==direction.getOpposite()){
                        continue;
                    }
                    else if(world.getBlockState(entity.getBlockPos().add(a.getVector())).getBlock()== SKblockInit.HEMOGLOBIN_BLOCK){
                        nextThing=true;
                        direction=a;
                        normDir= direction.getDoubleVector().multiply(0.2);
                        break;
                    }
                }
            }
            entity.setVelocity(entity.getVelocity().lerp(normDir,0.33));
            if(entity.getRandom().nextFloat()>0.8){
                Vec3d ranDir = new Vec3d(0.1,0,0).rotateY(entity.getRandom().nextFloat()*3.14f*2.0f).multiply(entity.getRandom().nextFloat());
                world.addParticleClient(SKparticlesInit.BLOOD_PARTICLE,entity.getX(),entity.getY()+0.1,entity.getZ(),ranDir.x,0,ranDir.z);
            }
            if(entity.isOnGround()||Math.abs(entity.getVelocity().y)<0.1){
                entity.setPosition(MathHelper.lerp(0.1,entity.getPos(),entity.getPos().multiply(1,0,1).add(0,pos.getY()+0.33,0)));
            }

        }
        else{
            entity.setVelocity(entity.getVelocity().multiply(0.85,0.4,0.85));
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{SCALE});
    }

    static {
        SCALE = Properties.AGE_7;
    }
}
