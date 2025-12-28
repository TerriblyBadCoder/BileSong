package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKItems;
import net.minecraft.client.render.entity.EndermanEntityRenderer;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ManEntity extends HostileEntity {
    private int hurtierTime = 0;
    public ManEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean hurtByWater() {
        return true;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean hurted = super.damage(world, source, amount);
        if (hurted) {
            this.hurtierTime=30;
        }
        return hurted;
    }

    @Override
    public void tick() {
        if(this.hurtierTime>0){
            this.hurtierTime-=1;
        }
        super.tick();
    }
    @Override
    public void takeKnockback(double strength, double x, double z) {
        strength*=4.0;
        super.takeKnockback(strength, x, z);
    }

    @Override
    protected void initGoals() {
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.goalSelector.add(1,new FleeEntityGoal(this, PlayerEntity.class,12,2.0,4.0,(entity)->{return this.hurtierTime>0;}));

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
        super.dropEquipment(world, source, causedByPlayer);
        Entity entity = source.getAttacker();
        if(entity!=null&&entity instanceof SkeletonEntity skeletonEntity){
            this.dropItem(world, SKItems.MUSIC_DISC_MAN);
        }
    }
    public static DefaultAttributeContainer.Builder createManAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MAX_HEALTH, 40.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.ATTACK_DAMAGE, 8.0)
                .add(EntityAttributes.FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.STEP_HEIGHT, 1.0);
    }
}
