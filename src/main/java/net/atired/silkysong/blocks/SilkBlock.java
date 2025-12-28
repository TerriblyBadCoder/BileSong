package net.atired.silkysong.blocks;

import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SilkBlock extends Block {
    public SilkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if(world instanceof ServerWorld serverWorld&&entity.getRandom().nextFloat()>0.5&&(entity.getMovement().length()>0||entity.getVelocity().multiply(1,0,1).length()>0)){
            serverWorld.spawnParticles(SKparticlesInit.SILK_PARTICLE,entity.getX(),entity.getY()+0.1f,entity.getZ(),1,0.3,0.1,0.3,0.1);
        }
        super.onSteppedOn(world, pos, state, entity);
    }
}
