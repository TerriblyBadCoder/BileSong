package net.atired.silkysong.entities;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.init.SKparticlesInit;
import net.atired.silkysong.networking.payloads.HemoRagePayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HemogoblinEntity extends HostileEntity implements BloodEntity {
    private int hurtierTime = 0;
    public HemogoblinEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
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
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean tried = super.tryAttack(world, target);
        if(tried){

            if(target instanceof HostileEntityStringBoostAccessor accessor){
                float newRage = (float) (accessor.getHemorrhage()+0.33);
                newRage=Math.min(newRage,1.0f);

                for(ServerPlayerEntity serverPlayerEntity: PlayerLookup.tracking(world,target.getBlockPos())){
                    ServerPlayNetworking.send(serverPlayerEntity,new HemoRagePayload(target.getId(),newRage));
                }
                accessor.setHemorrhage(newRage);
            }
        }
        return tried;
    }

    @Override
    public void tick() {
        Vec3d rot = new Vec3d(0.0,0,0.7).rotateY(-getBodyYaw()/180f*3.14f);
        if(getWorld().isClient()&&random.nextFloat()<0.1){
            getWorld().addParticleClient(SKparticlesInit.BLOOD_PARTICLE,getParticleX(0.1)+rot.x,getBodyY(0.7),getParticleZ(0.1)+rot.z,0.0,0.0,0.0);
        }
        if(this.hurtierTime>0){
            this.hurtierTime-=1;
        }
        super.tick();
    }
    @Override
    public void takeKnockback(double strength, double x, double z) {
        strength*=0.2;
        super.takeKnockback(strength, x, z);
    }

    @Override
    protected void initGoals() {
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createGoblinAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MAX_HEALTH, 30.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.STEP_HEIGHT, 1.0);
    }
}
