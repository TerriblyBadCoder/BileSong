package net.atired.silkysong.entities;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PuppetEntity extends HostileEntity {
    private int strafeCD = 60;
    public PuppetEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initGoals() {
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new PuppetAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected double getGravity() {
        return super.getGravity()*0.6f;
    }

    @Override
    public void tick() {
        super.tick();
        if(getWorld().isClient()&&random.nextFloat()<0.1){
            getWorld().addParticleClient(SKparticlesInit.SILK_PARTICLE,getParticleX(0.6),getBodyY(0.9),getParticleZ(0.6),0.0,0.0,0.0);
        }
        for(HostileEntity a : getWorld().getEntitiesByClass(HostileEntity.class,getBoundingBox().expand(4),HostileEntity::isLiving)){
            if(a.distanceTo(this)<4&&!(a instanceof PuppetEntity) && a instanceof HostileEntityStringBoostAccessor accessor){
                accessor.setStringBoost(accessor.getStringBoost()+0.1f);
            }
        }
        if(this.isOnGround()&&this.getTarget()!=null&&strafeCD==0){
            double power = Math.clamp(distanceTo(this.getTarget())/7.0-1.0f,0.0f,1.0f);
            Vec3d vecto = getTarget().getPos().subtract(getPos()).multiply(1,0,1).normalize();
            this.strafeCD = random.nextBetween(65,110);
            Vec3d dir = vecto.rotateY(3.14f/2.0f).multiply(1.7*power);
            if(random.nextFloat()>0.5) dir = dir.multiply(-1);
            dir = dir.add(0,0.01,0);
            this.moveControl.strafeTo((float) dir.x, (float) dir.z);
            addVelocity(dir.multiply(0.2));
        }
        if(this.getTarget()!=null&&this.strafeCD>0){
            this.strafeCD-=1;
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        world.spawnParticles(SKparticlesInit.SILK_PARTICLE,getParticleX(0.6),getBodyY(0.9),getParticleZ(0.6),4,0.2,0.2,0.2,0.0);

        return super.damage(world, source, amount);
    }

    public static DefaultAttributeContainer.Builder createBoyAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.STEP_HEIGHT, 1.0);
    }

    @Override
    public float getMovementSpeed() {
        double power =1.0f;
        if(this.getTarget()!=null){
            power = Math.clamp(distanceTo(this.getTarget())/2.0-3.0f,-0.5f,1.0f);
        }
        return (float) (super.getMovementSpeed()*power);
    }

    private class PuppetAttackGoal extends MeleeAttackGoal{

        public PuppetAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public void tick() {

            LivingEntity livingEntity = this.mob.getTarget();

            super.tick();
        }
    }
}
