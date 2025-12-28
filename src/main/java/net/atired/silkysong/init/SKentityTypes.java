package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class SKentityTypes {
    public static final EntityType<TrashCubeEntity> TRASHCUBE = EntityType.Builder.<TrashCubeEntity>create(TrashCubeEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("trash_cube")));
    public static final EntityType<IcosphereEntity> ICOSPHERE = EntityType.Builder.<IcosphereEntity>create(IcosphereEntity::new, SpawnGroup.MISC).dimensions(0.3f, 0.3f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("icosphere")));
    public static final EntityType<DecoagulatorEntity> DECOAGULATOR = EntityType.Builder.<DecoagulatorEntity>create(DecoagulatorEntity::new, SpawnGroup.MISC).dimensions(0.3f, 0.3f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("decoagulator")));
    public static final EntityType<SigilEntity> SIGIL = EntityType.Builder.<SigilEntity>create(SigilEntity::new, SpawnGroup.MISC).dimensions(0.3f, 0.3f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("sigil")));
    public static final EntityType<BloodArrowEntity> BLOOD_ARROW = EntityType.Builder.<BloodArrowEntity>create(BloodArrowEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(),EntityType.ARROW.getHeight()).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("blood_arrow")));
    public static final EntityType<HemogoblinEntity> HEMOGOBLIN = EntityType.Builder.<HemogoblinEntity>create(HemogoblinEntity::new, SpawnGroup.MONSTER).dimensions(0.9f, 2.0f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("hemogoblin")));
    public static final EntityType<HemoGlobuleEntity> HEMOGLOBULE = EntityType.Builder.<HemoGlobuleEntity>create(HemoGlobuleEntity::new, SpawnGroup.MONSTER).dimensions(EntityType.SLIME.getWidth(),EntityType.SLIME.getHeight()).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("hemoglobule")));
    public static final EntityType<MissileToadEntity> MISSILETOAD = EntityType.Builder.<MissileToadEntity>create(MissileToadEntity::new, SpawnGroup.CREATURE).dimensions(0.95f, 0.55f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("missiletoad")));
    public static final EntityType<JumpscarerEntity> JUMPSCARER = EntityType.Builder.<JumpscarerEntity>create(JumpscarerEntity::new, SpawnGroup.MONSTER).dimensions(0.8f, 1.8f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("jumpscarer")));

    public static final EntityType<ManEntity> MAN = EntityType.Builder.<ManEntity>create(ManEntity::new, SpawnGroup.MONSTER).dimensions(0.5f, 2.5f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("man")));
    public static final EntityType<PuppetEntity> BOY = EntityType.Builder.<PuppetEntity>create(PuppetEntity::new, SpawnGroup.MONSTER).dimensions(0.7f, 1.5f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("boy")));
    public static final EntityType<HunterEntity> HUNTER = EntityType.Builder.<HunterEntity>create(HunterEntity::new, SpawnGroup.MONSTER).dimensions(0.8f, 1.98f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("hunter")));

    public static final EntityType<BileMosqoEntity> BILEMOSQO = EntityType.Builder.<BileMosqoEntity>create(BileMosqoEntity::new, SpawnGroup.MONSTER).dimensions(0.85f, 0.85f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SilkySong.id("bile_mosqo")));

    public static void init(){

        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("trash_cube"), TRASHCUBE);
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("blood_arrow"), BLOOD_ARROW);
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("sigil"), SIGIL);
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("icosphere"), ICOSPHERE);
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("decoagulator"), DECOAGULATOR);
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("jumpscarer"), JUMPSCARER);
        FabricDefaultAttributeRegistry.register(JUMPSCARER,JumpscarerEntity.createJumpscarerAttributes());
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("man"), MAN);
        FabricDefaultAttributeRegistry.register(MAN,ManEntity.createManAttributes());

        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("missiletoad"), MISSILETOAD);
        FabricDefaultAttributeRegistry.register(MISSILETOAD,MissileToadEntity.createToadAttributes());
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("hemoglobule"), HEMOGLOBULE);
        FabricDefaultAttributeRegistry.register(HEMOGLOBULE,HemoGlobuleEntity.createHemoglobAttributes());
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("hemogoblin"), HEMOGOBLIN);
        FabricDefaultAttributeRegistry.register(HEMOGOBLIN,HemogoblinEntity.createGoblinAttributes());
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("hunter"), HUNTER);
        FabricDefaultAttributeRegistry.register(HUNTER, SkeletonEntity.createAbstractSkeletonAttributes());
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("boy"), BOY);
        FabricDefaultAttributeRegistry.register(BOY,PuppetEntity.createBoyAttributes());
        Registry.register(Registries.ENTITY_TYPE, SilkySong.id("bile_mosqo"), BILEMOSQO);
        FabricDefaultAttributeRegistry.register(BILEMOSQO,BileMosqoEntity.createMosqoAttributes());
    }
}
