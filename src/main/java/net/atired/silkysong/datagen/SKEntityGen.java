package net.atired.silkysong.datagen;

import net.atired.silkysong.entities.BileMosqoEntity;
import net.atired.silkysong.entities.HemoGlobuleEntity;
import net.atired.silkysong.entities.HunterEntity;
import net.atired.silkysong.entities.JumpscarerEntity;
import net.atired.silkysong.init.SKentityTypes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class SKEntityGen {
    public static void addSpawns(){
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).negate()), SpawnGroup.MONSTER, SKentityTypes.BOY,10,1,1);
        SpawnRestriction.register(SKentityTypes.BOY, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP), SpawnGroup.MONSTER, SKentityTypes.HUNTER,30,1,1);
        SpawnRestriction.register(SKentityTypes.HUNTER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HunterEntity::canHunterSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).negate()), SpawnGroup.MONSTER, SKentityTypes.MAN,3,1,1);
        SpawnRestriction.register(SKentityTypes.MAN, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.SWAMP_HUT_HAS_STRUCTURE), SpawnGroup.MONSTER, SKentityTypes.JUMPSCARER,15,4,5);
        SpawnRestriction.register(SKentityTypes.JUMPSCARER, SpawnLocationTypes.UNRESTRICTED, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, JumpscarerEntity::canMythSpawnInDark);


        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).negate()), SpawnGroup.MONSTER, SKentityTypes.HEMOGLOBULE,10,1,1);
        SpawnRestriction.register(SKentityTypes.HEMOGLOBULE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, HemoGlobuleEntity::canSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).negate()), SpawnGroup.MONSTER, SKentityTypes.HEMOGOBLIN,10,1,1);
        SpawnRestriction.register(SKentityTypes.HEMOGOBLIN, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, HostileEntity::canSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.SWAMP_HUT_HAS_STRUCTURE), SpawnGroup.MONSTER, SKentityTypes.BILEMOSQO,20,4,7);
        SpawnRestriction.register(SKentityTypes.BILEMOSQO, SpawnLocationTypes.UNRESTRICTED, Heightmap.Type.MOTION_BLOCKING, BileMosqoEntity::canMosqoSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.SWAMP_HUT_HAS_STRUCTURE), SpawnGroup.CREATURE, SKentityTypes.MISSILETOAD,3,2,4);
        SpawnRestriction.register(SKentityTypes.MISSILETOAD, SpawnLocationTypes.UNRESTRICTED, Heightmap.Type.MOTION_BLOCKING, MobEntity::canMobSpawn);

    }
}
