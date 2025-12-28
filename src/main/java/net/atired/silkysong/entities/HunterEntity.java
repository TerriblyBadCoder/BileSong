package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKItems;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HunterEntity extends SkeletonEntity {
    private int cdBall = 60;

    public HunterEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }
    public static boolean canHunterSpawnInDark(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return canSpawnInDark(type,world,spawnReason,pos,random)&&world.isSkyVisible(pos.down());
    }
    @Override
    public void tick() {
        if(getTarget()!=null&&distanceTo(getTarget())>4&&canSee(getTarget())&&getWorld() instanceof ServerWorld serverWorld){
            if(this.getItemUseTime()>2){
               if(this.cdBall>=5){
                   this.cdBall-=1;
               }

            }
            if(this.getItemUseTime()==0&&this.cdBall<5){
                if(this.cdBall>0) this.cdBall-=1;
                else{
                    swingHand(Hand.OFF_HAND);
                    this.addVelocity(getRotationVector().multiply(-1,0,-1).multiply(0.4).add(0,0.15,0 ));
                    IcosphereEntity icosphereEntity = ProjectileEntity.spawnWithVelocity(IcosphereEntity::new, serverWorld, getOffHandStack(), this, 0.0F, 1.1f, 1.0F);
                    icosphereEntity.addVelocity(0,0.1,0);
                    this.cdBall = 40+random.nextInt(40);
                }

            }
        }
        super.tick();
    }

    @Override
    protected void initGoals() {
        super.initGoals();
    }
    @Override
    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier, @Nullable ItemStack shotFrom) {
        PersistentProjectileEntity persistentProjectileEntity = super.createArrowProjectile(arrow, damageModifier, shotFrom);
        if (persistentProjectileEntity instanceof ArrowEntity arrowEntity) {
            arrowEntity.addEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100));
        }
        addVelocity(new Vec3d(0.1,0,0).rotateY(3.14f*random.nextFloat()*2.0f));
        return persistentProjectileEntity;
    }
    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.equipStack(EquipmentSlot.OFFHAND,new ItemStack(SKItems.SLIMECANNONBALL));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected double getGravity() {
        return super.getGravity()*1.1;
    }

    SoundEvent getStepSound() {
        return SoundEvents.ENTITY_STRAY_STEP;
    }
}
