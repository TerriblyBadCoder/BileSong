package net.atired.silkysong.entities;

import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.EnumSet;

public class BileMosqoEntity extends HostileEntity {
    private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new ExplosionBehavior() {
        @Override
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            return false;
        }



    };
    private int waitTicks = 0;
    public int fallTicks = 0;
    public BileMosqoEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new MosqoMoveControl(this);
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        this.addVelocity(0,0.3,0);
        super.takeKnockback(strength*3.0, x, z);
    }

    @Override
    public void tick() {
        if(this.getHealth()<6.0f){
            this.fallTicks+=1;
            if(this.fallTicks==20&&getWorld() instanceof ServerWorld serverWorld){
                serverWorld.spawnParticles(ParticleTypes.SNEEZE,getX(),getBodyY(0.5),getZ(),25,0.1,0.1,0.1,0.5);
                serverWorld.spawnParticles(ParticleTypes.SNEEZE,getX(),getBodyY(0.5),getZ(),15,0.1,0.1,0.1,0.2);
                serverWorld.spawnParticles(ParticleTypes.ITEM_SLIME,getX(),getBodyY(0.5),getZ(),18,0.1,0.1,0.1,1.4);
                getWorld().createExplosion(this,
                        Explosion.createDamageSource(getWorld(), this),
                        EXPLOSION_BEHAVIOR,
                        getX(),
                        getY(),
                        getZ(),
                        2.0f,
                        false,
                        World.ExplosionSourceType.MOB,
                        ParticleTypes.EXPLOSION,
                        ParticleTypes.EXPLOSION_EMITTER,
                        SoundEvents.ENTITY_GENERIC_EXPLODE);
                discard();
            }
        }
        if(waitTicks>0){
            waitTicks-=1;
        }
        super.tick();
    }

    @Override
    public boolean handleFallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource) {
        return false;
    }
    public static boolean canMosqoSpawnInDark(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && (SpawnReason.isTrialSpawner(spawnReason) || isSpawnDark(world, pos, random))
                && canMobSpawn(type, world, spawnReason, pos, random)&&world.isSkyVisible(pos.down());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new HomeInAtGoal(this));
        this.goalSelector.add(4, new FlyRandomlyGoal(this));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class,10f));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal(this, SilverfishEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal(this, EndermiteEntity.class, true));
        super.initGoals();
    }

    @Override
    protected double getGravity() {
        if(this.getHealth()<6.0){
            return 0.15;
        }
        return 0.0;
    }
    public static DefaultAttributeContainer.Builder createMosqoAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.MAX_HEALTH,14.0f).add(EntityAttributes.ATTACK_DAMAGE, 6.0).add(EntityAttributes.MOVEMENT_SPEED, 0.23000000417232513).add(EntityAttributes.FOLLOW_RANGE, 48.0);
    }
    static class MosqoMoveControl extends MoveControl {
        private final BileMosqoEntity mosqo;
        private int collisionCheckCooldown;

        public MosqoMoveControl(BileMosqoEntity mosqo) {
            super(mosqo);
            this.mosqo = mosqo;
        }

        public void tick() {
            if(this.mosqo.getHealth()<6.0f){
                this.state = State.WAIT;
                return;
            }
            MoveControl moveControl = this.mosqo.getMoveControl();
            this.mosqo.getLookControl().lookAt(new Vec3d(moveControl.getTargetX(),moveControl.getTargetY(),moveControl.getTargetZ()));
            this.mosqo.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES,new Vec3d(moveControl.getTargetX(),moveControl.getTargetY(),moveControl.getTargetZ()));

            if (this.state == State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.mosqo.getRandom().nextInt(5) + 2;
                    Vec3d vec3d = new Vec3d(this.targetX - this.mosqo.getX(), this.targetY - this.mosqo.getY(), this.targetZ - this.mosqo.getZ());
                    double d = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        this.mosqo.setVelocity(this.mosqo.getVelocity().multiply(0.6).add(vec3d.multiply(0.2)));
                    } else {
                        this.state = State.WAIT;

                    }
                }

            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.mosqo.getBoundingBox();

            for(int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.mosqo.getWorld().isSpaceEmpty(this.mosqo, box)) {
                    return false;
                }
            }

            return true;
        }
    }
    private static class FlyRandomlyGoal extends Goal {
        private final BileMosqoEntity mosqo;

        @Override
        public boolean shouldContinue() {
            return false;
        }

        public FlyRandomlyGoal(BileMosqoEntity mosqo) {
            this.mosqo = mosqo;
            this.setControls(EnumSet.of(Control.MOVE,Control.LOOK));
        }

        public boolean canStart() {
            if(this.mosqo.getHealth()<6.0f){
                return false;
            }
            MoveControl moveControl = this.mosqo.getMoveControl();
            if(this.mosqo.waitTicks>0){
                return false;
            }
            if (!moveControl.isMoving()) {
                return true;
            } else {
                double d = moveControl.getTargetX() - this.mosqo.getX();
                double e = moveControl.getTargetY() - this.mosqo.getY();
                double f = moveControl.getTargetZ() - this.mosqo.getZ();
                double g = d * d + e * e + f * f;
                return g < 1.0 || g > 3600.0;
            }
        }


        @Override
        public void stop() {

            this.mosqo.waitTicks = 5+this.mosqo.getRandom().nextInt(5);
            super.stop();
        }

        public void start() {

            this.mosqo.waitTicks = 40;
            Random random = this.mosqo.getRandom();
            double d = this.mosqo.getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 4.0F);
            double e = this.mosqo.getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 4.0F);
            double f = this.mosqo.getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 4.0F);
            this.mosqo.getMoveControl().moveTo(d, e, f, 1.4f);
        }

        @Override
        public void tick() {
            if(this.mosqo.getHealth()<6.0f){
                this.stop();
            }
            MoveControl moveControl = this.mosqo.getMoveControl();
            Vec3d targetted = new Vec3d(moveControl.getTargetX(),moveControl.getTargetY(),moveControl.getTargetZ());
            this.mosqo.getLookControl().lookAt(targetted);
            this.mosqo.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES,targetted);
            super.tick();
        }
    }

    private static class HomeInAtGoal extends Goal {
        private final BileMosqoEntity mosqo;
        private int homeCD = 0;
        public HomeInAtGoal(BileMosqoEntity mosqo) {
            this.mosqo = mosqo;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            if(this.mosqo.getHealth()<6.0f){
                return false;
            }
            LivingEntity livingEntity = this.mosqo.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.mosqo.canTarget(livingEntity);
        }

        public void start() {

            LivingEntity livingEntity = this.mosqo.getTarget();
        }

        public void stop() {
            super.stop();
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.mosqo.getTarget();
            if(livingEntity==null||this.mosqo.getHealth()<6.0f){
                stop();
                return;
            }
            if(homeCD>0){
                this.homeCD-=1;
                return;
            }
            else{
                this.mosqo.getMoveControl().moveTo(livingEntity.getX(),livingEntity.getBodyY(0.5f),livingEntity.getZ(),8.0f);
            }
            if (livingEntity != null && this.mosqo.getVisibilityCache().canSee(livingEntity) ) {

                if(this.mosqo.distanceTo(livingEntity)<1.5){
                    if(mosqo.getWorld() instanceof ServerWorld serverWorld) {
                        this.mosqo.tryAttack(serverWorld,livingEntity);
                    }
                    this.homeCD = 12;
                    this.mosqo.setVelocity(livingEntity.getPos().add(0,livingEntity.getHeight()/2,0).subtract(this.mosqo.getPos()).normalize().multiply(-2));

                this.mosqo.getLookControl().lookAt(livingEntity, 10.0F, 50.0F);
                this.mosqo.getMoveControl().moveTo(livingEntity.getX(),livingEntity.getBodyY(0.5f),livingEntity.getZ(),8.0f);

                }
                else{
                    if(this.mosqo.random.nextFloat()>0.94){
                        this.mosqo.addVelocity(new Vec3d(1.0,0.2,0.0).rotateY(this.mosqo.random.nextFloat()*3.14f*2f).multiply(0.5));
                    }
                }
            }
    }
}
}
