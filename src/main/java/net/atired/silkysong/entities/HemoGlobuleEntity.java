package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.EnumSet;

public class HemoGlobuleEntity extends SlimeEntity {
    public HemoGlobuleEntity(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new GlobuleMoveControl(this);
    }
    public static boolean canSpawnInDark(EntityType<? extends SlimeEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && (SpawnReason.isTrialSpawner(spawnReason) || isSpawnDark(world, pos, random))
                && canMobSpawn(type, world, spawnReason, pos, random);
    }
    public static boolean isSpawnDark(ServerWorldAccess world, BlockPos pos, Random random) {
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = world.getDimension();
            int i = dimensionType.monsterSpawnBlockLightLimit();
            if (i < 15 && world.getLightLevel(LightType.BLOCK, pos) > i) {
                return false;
            } else {
                int j = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
                return j <= dimensionType.monsterSpawnLightTest().get(random);
            }
        }
    }
    @Override
    public void setSize(int size, boolean heal) {
        super.setSize(size, heal);
        int i = MathHelper.clamp(size, 1, 127);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * i);
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(i+1);
    }

    @Override
    protected ParticleEffect getParticles() {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, Items.NETHER_WART.getDefaultStack());
    }

    @Override
    protected float getJumpVelocityMultiplier() {
        return super.getJumpVelocityMultiplier()*(float)Math.pow(getSize(),0.33f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimmingGoal(this));
        this.goalSelector.add(2, new FaceTowardTargetGoal(this));
        this.goalSelector.add(3, new RandomLookGoal(this));
        this.goalSelector.add(5, new MoveGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, (target, world) -> Math.abs(target.getY() - this.getY()) <= 4.0));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));
    }
    protected boolean canAttack() {
        return !this.isSmall() && this.canActVoluntarily();
    }
    public static class SwimmingGoal extends Goal {
        private final HemoGlobuleEntity slime;

        public SwimmingGoal(HemoGlobuleEntity slime) {
            this.slime = slime;
            this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
            slime.getNavigation().setCanSwim(true);
        }

        public boolean canStart() {
            return (this.slime.isTouchingWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof GlobuleMoveControl;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().setActive();
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof GlobuleMoveControl slimeMoveControl) {
                slimeMoveControl.move(1.2);
            }

        }
    }

    public static class FaceTowardTargetGoal extends Goal {
        private final HemoGlobuleEntity slime;
        private int ticksLeft;

        public FaceTowardTargetGoal(HemoGlobuleEntity slime) {
            this.slime = slime;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity == null) {
                return false;
            } else {
                return !this.slime.canTarget(livingEntity) ? false : this.slime.getMoveControl() instanceof GlobuleMoveControl;
            }
        }

        public void start() {
            this.ticksLeft = toGoalTicks(300);
            super.start();
        }

        public boolean shouldContinue() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!this.slime.canTarget(livingEntity)) {
                return false;
            } else {
                return --this.ticksLeft > 0;
            }
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity != null) {
                this.slime.lookAtEntity(livingEntity, 10.0F, 10.0F);
            }

            MoveControl var3 = this.slime.getMoveControl();
            if (var3 instanceof GlobuleMoveControl slimeMoveControl) {
                slimeMoveControl.look(this.slime.getYaw(), this.slime.canAttack());
            }

        }
    }

    public static class RandomLookGoal extends Goal {
        private final HemoGlobuleEntity slime;
        private float targetYaw;
        private int timer;

        public RandomLookGoal(HemoGlobuleEntity slime) {
            this.slime = slime;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            return this.slime.getTarget() == null && (this.slime.isOnGround() || this.slime.isTouchingWater() || this.slime.isInLava() || this.slime.hasStatusEffect(StatusEffects.LEVITATION)) && this.slime.getMoveControl() instanceof GlobuleMoveControl;
        }

        public void tick() {
            if (--this.timer <= 0) {
                this.timer = this.getTickCount(40 + this.slime.getRandom().nextInt(60));
                this.targetYaw = (float)this.slime.getRandom().nextInt(360);
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof GlobuleMoveControl slimeMoveControl) {
                slimeMoveControl.look(this.targetYaw, false);
            }

        }
    }

    public static class MoveGoal extends Goal {
        private final HemoGlobuleEntity slime;

        public MoveGoal(HemoGlobuleEntity slime) {
            this.slime = slime;
            this.setControls(EnumSet.of(Control.JUMP, Control.MOVE));
        }

        public boolean canStart() {
            return !this.slime.hasVehicle();
        }

        public void tick() {
            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof GlobuleMoveControl slimeMoveControl) {
                slimeMoveControl.move(1.0);
            }

        }
    }
    public static DefaultAttributeContainer.Builder createHemoglobAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH,4.0f).add(EntityAttributes.ATTACK_DAMAGE,2.0f);
    }
    @Override
    public void tick() {
        boolean wasonground =((SlimeEntity)this).onGroundLastTick;
        float olstretch;
        setSize(getSize(),false);
        super.tick();
        if(this.isOnGround()&&this.targetStretch<-0.2&&getSize()>=2){
            float f = this.getDimensions(this.getPose()).width() * 2.0F;
            float g = f / 1.7F;
            float h = this.random.nextFloat() * 6.2831855F;
            float j = this.random.nextFloat() * 0.5F + 0.5F;
            float k = MathHelper.sin(h) * g * j;
            float l = MathHelper.cos(h) * g * j;
            this.getWorld().addParticleClient(SKparticlesInit.BLOOD_PARTICLE, this.getX() + (double)k, this.getY(), this.getZ() + (double)l, 0.0, 0.0, 0.0);

        }
        if (this.isOnGround() && !wasonground) {
            if(getSize()>=3){

            }
            this.targetStretch = -0.6F*getSize();
        } else if (!this.isOnGround() && wasonground) {
            this.targetStretch = 0.8f*getSize();

        }
    }
    protected void updateStretch() {
        if(this.targetStretch>0.0){
            this.targetStretch *= 0.92F;
        }
        else{
            this.targetStretch *= 0.8F;
        }
    }
    @Override
    protected boolean makesJumpSound() {
        return super.makesJumpSound();
    }
    public float getJumpSoundPitch() {
        float f = this.isSmall() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }
    static class GlobuleMoveControl extends MoveControl {
        private float targetYaw;
        private int ticksUntilJump;
        private final HemoGlobuleEntity globule;
        private boolean jumpOften;

        public GlobuleMoveControl(HemoGlobuleEntity slime) {
            super(slime);
            this.globule = slime;
            this.targetYaw = 180.0F * slime.getYaw() / (float) Math.PI;
        }

        public void look(float targetYaw, boolean jumpOften) {
            if(ticksUntilJump>6) return;
            this.targetYaw = targetYaw;
            this.jumpOften = jumpOften;
        }


        public void move(double speed) {
            this.speed = speed;
            this.state = MoveControl.State.MOVE_TO;
        }

        @Override
        public void tick() {
            this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
            this.entity.headYaw = this.entity.getYaw();
            this.entity.bodyYaw = this.entity.getYaw();
            if(this.entity.isOnGround() && !((SlimeEntity)this.entity).onGroundLastTick){
                this.ticksUntilJump = 10;
            }
            if (this.state != MoveControl.State.MOVE_TO) {
                this.entity.setForwardSpeed(0.0F);
            } else {
                this.state = MoveControl.State.WAIT;
                if (this.entity.isOnGround()) {
                    this.entity.setMovementSpeed((float)(this.speed * this.entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED)));
                    if (this.ticksUntilJump-- <= 0) {
                        this.ticksUntilJump = 30;

                        this.globule.getJumpControl().setActive();
                        if (this.globule.makesJumpSound()) {
                            this.globule.playSound(this.globule.getJumpSound(), this.globule.getSoundVolume(), this.globule.getJumpSoundPitch());
                        }
                    } else {
                        this.globule.sidewaysSpeed *=this.ticksUntilJump/24.0f;
                        this.globule.forwardSpeed *=this.ticksUntilJump/24.0f;
                        this.entity.setMovementSpeed((float)(this.speed * this.entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED)*this.ticksUntilJump/24.0f));
                    }
                } else {
                    this.entity.setMovementSpeed((float)(this.speed * this.entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED)*2.0f));
                }
            }
        }
    }

}
