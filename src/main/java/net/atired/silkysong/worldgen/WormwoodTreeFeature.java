package net.atired.silkysong.worldgen;

import com.mojang.serialization.Codec;
import net.atired.silkysong.init.SKblockInit;
import net.atired.silkysong.misc.SKgetDatNoise;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTypes;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WormwoodTreeFeature
        extends Feature<DefaultFeatureConfig> {
    public WormwoodTreeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        BlockPos.Mutable blockPos1 = blockPos.mutableCopy();

        float initialAngle = (float) (Math.random()*3.14f*2.0f);
        Vec3d rotated = new Vec3d(7,0,0).rotateY(initialAngle);
        for (int i = 7; i < 18; i++) {
            rotated=rotated.rotateY(0.25f);
            rotated=rotated.multiply(0.85);
            for (int x = -(18-i)/7-1; x < 0; x++) {
                for (int z = -(18-i)/7-1; z < 0; z++) {
                    BlockPos.Mutable blockPos2 = blockPos1.mutableCopy();
                    blockPos2=blockPos2.move((int) rotated.x+x,i-9,(int) rotated.z+z);
                    float noisy = SKgetDatNoise.sampleNoise3D(blockPos2.getX(),blockPos2.getY(),blockPos2.getZ(),10.0f);
                    float noisy2 = SKgetDatNoise.sampleNoise3D(blockPos2.getX(),blockPos2.getY()+1,blockPos2.getZ(),10.0f);

                    if(noisy>-0.1) {
                        structureWorldAccess.setBlockState(blockPos2,Blocks.MOSS_BLOCK.getDefaultState(),2);
                    }else{
                        structureWorldAccess.setBlockState(blockPos2,SKblockInit.WORMWOOD.getDefaultState(),2);
                    }
                    structureWorldAccess.setBlockState(blockPos2.down(),SKblockInit.WORMWOOD.getDefaultState(),2);
                    if(noisy2>-0.4&&structureWorldAccess.getBlockState(blockPos2.up()).isAir()) {
                        structureWorldAccess.setBlockState(blockPos2.up(),Blocks.MOSS_CARPET.getDefaultState(),2);
                    }
                }
            }
        }
        return true;
    }
}

