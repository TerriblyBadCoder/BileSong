package net.atired.silkysong.blocks;

import net.atired.silkysong.entities.TrashCubeEntity;
import net.atired.silkysong.init.SKentityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrashCubeBlock extends Block {
    public TrashCubeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos, Blocks.AIR.getDefaultState(),2);
        if(world instanceof ServerWorld serverWorld){
            TrashCubeEntity cubeEntity = new TrashCubeEntity(SKentityTypes.TRASHCUBE,world);
            cubeEntity.setPosition(pos.toCenterPos().add(0,-0.49,0));
            world.spawnEntity(cubeEntity);
        }
    }
}
