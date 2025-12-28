package net.atired.silkysong.entities;

import net.atired.silkysong.networking.payloads.MissiletoadPayload;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MissileToadEntity extends PassiveEntity {
    private static final TrackedData<Float> BLOATED = DataTracker.registerData(MissileToadEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public float bloatLerped = 0.0f;
    private static final TrackedData<Boolean> ON_TREE = DataTracker.registerData(MissileToadEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public Vec3d targetPos = Vec3d.ZERO;
    public BileMosqoEntity target = null;
    public MissileToadEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4,new WanderAroundGoal(this,1.0));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(3, new LookAtEntityGoal(this, BileMosqoEntity.class, 12.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0));
        super.initGoals();
    }
    public static DefaultAttributeContainer.Builder createToadAttributes() {
        return LivingEntity.createLivingAttributes().add(EntityAttributes.FOLLOW_RANGE, 16.0).add(EntityAttributes.MOVEMENT_SPEED,0.18);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BLOATED, 0.0f);
        builder.add(ON_TREE, false);
        super.initDataTracker(builder);
    }

    @Override
    public int getMaxLookYawChange() {
        return 5;
    }

    public void setTargetting(BileMosqoEntity target) {
        if(getWorld() instanceof ServerWorld serverWorld){
            setTarget(target);
            this.targetPos = getPos();
            for(ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking(this)){
                ServerPlayNetworking.send(serverPlayerEntity,new MissiletoadPayload(getId(),target.getId(),false));
            }
        }
    }
    public void setTarget(BileMosqoEntity target){
        this.target = target;
    }
    public void resetTarget(){
        this.target = null;
    }
    public void resetTargetting(){
        if(getWorld() instanceof ServerWorld serverWorld){
            resetTarget();
            for(ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking(this)){
                ServerPlayNetworking.send(serverPlayerEntity,new MissiletoadPayload(getId(),0,true));
            }
        }
    }
    @Override
    public int getMaxHeadRotation() {
        return 5;
    }
    public void processBile(){
        if(this.targetPos == Vec3d.ZERO&&Math.round((this.getBloat()-Math.floor(this.getBloat()))*100d)/100d==0.0f&&getBloat()<7){
            float distance = 400;
            BileMosqoEntity temp = null;
            for (BileMosqoEntity a : getWorld().getEntitiesByClass(BileMosqoEntity.class,getBoundingBox().expand(8),(living -> {return true;}))){
                if(distanceTo(a)<distance){
                    if(a.getPos().subtract(getPos()).dotProduct(getRotationVector())<0.6||!canSee(a)){
                        continue;
                    }
                    distance = distanceTo(a);
                    temp = a;
                }
            }
            if(temp!=null&&getWorld() instanceof ServerWorld serverWorld){
                setTargetting(temp);
            }
        }
        else if(this.targetPos != Vec3d.ZERO){
            if(this.target!=null&&this.target.isAlive()){
                lookAtEntity(this.target,30,30);
                Vec3d dir = target.getPos().subtract(targetPos);
                Vec3d dir2 = target.getPos().subtract(getPos()).normalize();
                this.targetPos = targetPos.add(dir.multiply(0.3));
                if(this.target.getPos().distanceTo(this.targetPos)<1.3){
                    this.target.stopMovement();
                    this.target.addVelocity(dir2.multiply(-0.4));
                    this.target.setTarget(null);
                    if(this.target.distanceTo(this)<0.9){
                        if(getWorld() instanceof ServerWorld serverWorld){
                            ItemStack stack = new ItemStack(Items.SLIME_BALL);
                            stack.setCount(4);
                            ItemEntity item = dropItem(stack,true,false);
                            item.setPosition(this.target.getPos());
                            stack = new ItemStack(Items.GUNPOWDER);
                            stack.setCount(3);
                            item = dropItem(stack,true,false);
                            item.setPosition(this.target.getPos());
                        }
                        this.target.discard();

                        setBloat(getBloat()+0.1f);
                        resetTargetting();
                        if(getWorld() instanceof ServerWorld serverWorld){
                            playSound(SoundEvents.ENTITY_PLAYER_BURP,1.6f,0.6f);
                            serverWorld.spawnParticles(ParticleTypes.ITEM_SLIME,getParticleX(0.4),getBodyY(0.5),getParticleZ(0.4),12,0,0,0,0.4);
                        }
                        return;
                    }
                }
                if(distanceTo(this.target)>11||dir2.dotProduct(getRotationVector())<0.3||!canSee(target)){
                    resetTargetting();
                }

            }
            else{
                this.targetPos =targetPos.add(getPos().subtract(targetPos).multiply(0.4));
                if(this.getPos().distanceTo(this.targetPos)<0.4){
                    this.targetPos = Vec3d.ZERO;
                }
            }
        }
    }
    public void setBloat(float bloat) {
        this.dataTracker.set(BLOATED,Math.round(bloat*100f)/100f);
    }
    public float getBloat() {
        return this.dataTracker.get(BLOATED);
    }

    @Override
    public void tick() {
        if(getWorld() instanceof ClientWorld clientWorld){
            this.bloatLerped = MathHelper.lerp(0.33f,this.bloatLerped,this.getBloat());
        }
        super.tick();
        if(this.getBloat()<0.0){
            if(getWorld() instanceof ServerWorld serverWorld){
                if(this.getBloat()<-0.4f){
                    Vec3d rot = getRotationVec(0);
                    serverWorld.spawnParticles(new ParryParticleEffect(getRotationVec(0).multiply(-1).toVector3f(),2),
                            getX()+rot.getX()*0.3,getEyeY()+rot.getY()*0.3,getZ()+rot.getZ()*0.3,1,0.05,0.05,0.05,0.0);

                }
                this.setBloat(this.getBloat()+0.1f);
            }
            else{
                if(this.getBloat()<-0.4f){
                for (int i = 0; i < 7; i++) {
                    Vec3d rotated = getRotationVec(0).rotateY(random.nextFloat()-0.5f).rotateX(random.nextFloat()-0.5f);
                    getWorld().addParticleClient(ParticleTypes.ITEM_SLIME,getParticleX(0.4),getBodyY(0.7),getParticleZ(0.4),rotated.x,rotated.y,rotated.z);
                    getWorld().addParticleClient(ParticleTypes.SNEEZE,getParticleX(0.4),getBodyY(0.7),getParticleZ(0.4),rotated.x,rotated.y,rotated.z);
                }
                }

            }

            return;
        }
        processBile();
        if(Math.round((this.getBloat()-Math.floor(this.getBloat()))*100d)/100d>0.0f&&getWorld() instanceof ServerWorld){
            this.setBloat(this.getBloat()+0.02f);
        }

        if(this.getBloat()>=4.0f){
            if (getWorld() instanceof ServerWorld serverWorld) {
                float distance = 400;
                PlayerEntity temp = null;
                for (PlayerEntity a : getWorld().getEntitiesByClass(PlayerEntity.class,getBoundingBox().expand(4),(living -> {return living.canTakeDamage();}))){
                    if(distanceTo(a)<distance){
                        if(a.getPos().subtract(getPos()).dotProduct(getRotationVector())<0.6||!canSee(a)){
                            continue;
                        }
                        distance = distanceTo(a);
                        temp = a;
                    }
                }
                if(temp!=null){
                    setVelocity(getRotationVector().multiply(-0.7).add(0,0.3,0));
                    playSound(SoundEvents.ENTITY_PLAYER_BURP,1.1f,0.3f);
                    playSound(SoundEvents.ENTITY_PANDA_SNEEZE,1.6f,0.6f);

                    for (int i = 0; i <3; i++) {
                        ProjectileEntity.spawnWithVelocity(IcosphereEntity::new, serverWorld, Items.SLIME_BALL.getDefaultStack(), this, 0.0F, 1.2f+random.nextFloat()/7.0f, 25.0F);
                    }
                    this.setBloat(-1.0f);
                }

            }

        }
    }
}
