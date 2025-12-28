package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeafLitterBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JumpscarerEntity extends HostileEntity {
    private int hurtierTime = 0;
    public float handRaise = 0.0f;
    public float oldHandRaise = 0.0f;
    public boolean fleeing = false;

    public float speedValue = 0.0f;
    public float oldHiding = 0.0f;
    private static final TrackedData<Float> HIDING = DataTracker.registerData(JumpscarerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<BlockState> STATE = DataTracker.registerData(JumpscarerEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);

    public JumpscarerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void readCustomData(ReadView view) {
        super.readCustomData(view);
        dataTracker.set(STATE,view.read("BlockState", BlockState.CODEC).orElse(Blocks.SHORT_GRASS.getDefaultState()));
    }
    public static boolean canMythSpawnInDark(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && (SpawnReason.isTrialSpawner(spawnReason) || isSpawnDark(world, pos, random))
                && canMobSpawn(type, world, spawnReason, pos, random)&&world.getBlockState(pos.down()).isIn(BlockTags.DIRT)&&world.isSkyVisible(pos.down());
    }
    @Override
    public void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.put("BlockState", BlockState.CODEC, getState());
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        this.hurtierTime=80;
        return super.damage(world, source, amount);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable()&&getHidingValue()>0.0f;
    }

    @Override
    protected EntityDimensions getBaseDimensions(EntityPose pose) {
        return super.getBaseDimensions(pose).scaled(1.0f,getHidingValue()==0.0f?0.5f:1.0f);
    }

    @Override
    public Box getBoundingBox(EntityPose pose) {
        return super.getBoundingBox(pose).expand(0,-(1.0f-getHidingValue()),0);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        if(this.random.nextFloat()>0.75){
            this.equipStack(EquipmentSlot.OFFHAND, SKItems.SLIMECANNONBALL.getDefaultStack());
        }
        if(this.random.nextFloat()>0.75){
            this.equipStack(EquipmentSlot.MAINHAND, SKItems.SLIMECANNONBALL.getDefaultStack());
        }
        BlockState nearbyState = Blocks.SHORT_GRASS.getDefaultState();
        label1:
        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                for (int y = -1; y < 1; y++) {
                    BlockPos pos = getBlockPos().add(x,y,z);
                    Block gotBlock = world.getBlockState(pos).getBlock();
                    if(gotBlock==Blocks.FERN||gotBlock==Blocks.FIREFLY_BUSH||gotBlock==Blocks.DEAD_BUSH||
                            gotBlock==Blocks.TALL_DRY_GRASS||gotBlock==Blocks.SHORT_DRY_GRASS||gotBlock==Blocks.LEAF_LITTER
                    ){
                        nearbyState=world.getBlockState(pos);
                        if(gotBlock instanceof LeafLitterBlock leafLitterBlock){
                            int randRot =  random.nextBetween(0,4);
                            Direction dir  =Direction.SOUTH.rotateYClockwise();
                            for (int i = 0; i < randRot; i++) {
                                dir=dir.rotateYClockwise();
                            }
                            nearbyState=gotBlock.getDefaultState();
                            nearbyState=nearbyState.with(leafLitterBlock.getAmountProperty(), Math.min(4, random.nextBetween(2,5))).with(Properties.HORIZONTAL_FACING,dir);
                        }
                        break label1;
                    }
                }
            }
        }

        dataTracker.set(STATE,nearbyState);
        this.setPos(Math.floor(this.getX())+0.5f,getY(),Math.floor(this.getZ())+0.5f);
        return data;
    }

    @Override
    public float getMovementSpeed() {
        return super.getMovementSpeed()*getHidingValue()*0.75f*(Math.min(this.speedValue,1.0f));
    }

    @Override
    public void tick() {
        float olvalue = getHidingValue();
        this.oldHandRaise=this.handRaise;
        if(this.hurtierTime>0){
            this.hurtierTime-=1;
        }
        if(oldHiding<getHidingValue()&&getWorld().isClient()){
            this.handRaise = 1.0f;
        }
        if(this.handRaise>0.0f){
            this.handRaise-=0.2f;
        }
        if(this.speedValue>0.0f){
            this.speedValue-=0.05f;
            if(this.speedValue<=0){
                this.speedValue=1.5f;
            }
        }

        if(getWorld() instanceof ServerWorld serverWorld){
            if(getHidingValue()>=1.0f){
                if(getTarget()!=null&&distanceTo(getTarget())<4){
                    if(getEquippedStack(EquipmentSlot.MAINHAND).getItem()==SKItems.SLIMECANNONBALL){
                        swingHand(Hand.MAIN_HAND);
                        this.fleeing=true;
                        this.hurtierTime=80;
                        this.addVelocity(getRotationVector().multiply(-1,0,-1).multiply(0.4).add(0,0.15,0 ));
                        IcosphereEntity icosphereEntity = ProjectileEntity.spawnWithVelocity(IcosphereEntity::new, serverWorld, SKItems.SLIMECANNONBALL.getDefaultStack(), this, 0.0F, 1.1f, 1.0F);
                        icosphereEntity.addVelocity(0,0.1,0);
                        equipStack(EquipmentSlot.MAINHAND, Items.AIR.getDefaultStack());
                    }
                    if(getEquippedStack(EquipmentSlot.OFFHAND).getItem()==SKItems.SLIMECANNONBALL){
                        swingHand(Hand.OFF_HAND);
                        this.fleeing=true;
                        this.hurtierTime=80;
                        this.addVelocity(getRotationVector().multiply(-1,0,-1).multiply(0.4).add(0,0.15,0 ));
                        IcosphereEntity icosphereEntity = ProjectileEntity.spawnWithVelocity(IcosphereEntity::new, serverWorld, SKItems.SLIMECANNONBALL.getDefaultStack(), this, 0.0F, 1.1f, 1.5F);
                        icosphereEntity.addVelocity(0,0.15,0);
                        equipStack(EquipmentSlot.OFFHAND, Items.AIR.getDefaultStack());
                    }
                }
            }
            if((getTarget()!=null||this.hurtierTime>0)&&getHidingValue()<1.0f){
                this.speedValue=1.5f;
                setHidingValue(getHidingValue()+0.2f);
                if(olvalue<1.0f&&getHidingValue()>=1.0f){

                    this.setVelocity(0,0.55,0);
                }
                serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK_CRUMBLE,getSteppingBlockState()),getX(),getY(),getZ(),5,0.3,0.1,0.3,0.2);
            }
            else if(getHidingValue()>0&&(getTarget()==null&&this.hurtierTime==0)){
                setHidingValue(getHidingValue()-0.2f);
                if(this.getHidingValue()==0){
                    this.fleeing=false;
                }
                serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK_CRUMBLE,getSteppingBlockState()),getX(),getY(),getZ(),5,0.3,0.1,0.3,0.2);
            }
        }
        else{
            Vec3d positioned = getPos().add(getHeight()*(olvalue+0.2));
            if(random.nextFloat()>0.93&&getState().getBlock()==Blocks.FIREFLY_BUSH) {
                Vec3d posCopy = positioned.add((random.nextFloat()-0.5f)*8.0f,random.nextFloat()*4.0f,(random.nextFloat()-0.5f)*8.0f);
                getWorld().addParticleClient(ParticleTypes.FIREFLY,posCopy.x,posCopy.y,posCopy.z,0.0,0,0);
            }
        }
        super.tick();
        this.oldHiding=getHidingValue();
    }
    public void setHidingValue(float hid){
        hid = Math.max(hid,0);
        dataTracker.set(HIDING,hid);
    }
    public BlockState getState(){
        return dataTracker.get(STATE);
    }
    @Override
    public void onAttacking(Entity target) {
        this.hurtierTime=80;
        this.speedValue*=0.5f;
        super.onAttacking(target);
    }

    public float getHidingValue(){
        return dataTracker.get(HIDING);
    }
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(HIDING,0.0f);
        builder.add(STATE,Blocks.SHORT_GRASS.getDefaultState());
        super.initDataTracker(builder);
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        super.takeKnockback(strength*1.5, x, z);
    }

    @Override
    protected void initGoals() {
        this.targetSelector.add(3, new ActiveTargetGoal(this, GolemEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));

        this.goalSelector.add(1, new JumpscareFleeGoal(this, 1.5,(one,two)->{return this.fleeing;}));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createJumpscarerAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.4F)
                .add(EntityAttributes.ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.FOLLOW_RANGE, 12.0)
                .add(EntityAttributes.STEP_HEIGHT, 1.0);
    }
    private static class JumpscareFleeGoal extends EscapeDangerGoal {

        protected TargetPredicate targetPredicate;
        public JumpscareFleeGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }
        public JumpscareFleeGoal(PathAwareEntity mob, double speed, TargetPredicate.EntityPredicate predicate) {
            super(mob, speed);
            this.targetPredicate= TargetPredicate.createAttackable().setBaseMaxDistance(64).setPredicate(predicate);
        }

        @Override
        protected boolean isInDanger() {
            if(mob instanceof JumpscarerEntity jumpscarerEntity){
                return jumpscarerEntity.fleeing;
            }
            return super.isInDanger();
        }
    }
}
