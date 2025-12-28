package net.atired.silkysong.entities;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.SKentityTypes;
import net.atired.silkysong.init.SKparticlesInit;
import net.atired.silkysong.init.SKstatusEffectInit;
import net.atired.silkysong.networking.payloads.HemoRagePayload;
import net.atired.silkysong.networking.payloads.VelSyncPayload;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.joml.Vector3f;

public class DecoagulatorEntity extends IcosphereEntity {
    private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new ExplosionBehavior() {
        @Override
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            return false;
        }



    };
    public DecoagulatorEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    public DecoagulatorEntity(ServerWorld serverWorld, LivingEntity livingEntity, ItemStack stack) {
        super(SKentityTypes.DECOAGULATOR,serverWorld,livingEntity,stack);
    }

    @Override
    public void explode(ServerWorld serverWorld) {
        playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(),1.0f,0.5f+random.nextFloat()/10.0f);
        playSound(SoundEvents.ENTITY_WARDEN_HEARTBEAT,1.0f,1.7f+random.nextFloat()/10.0f);
        serverWorld.spawnParticles(SKparticlesInit.BLOOD_PARTICLE,getX(),getBodyY(0.5),getZ(),25,0.1,0.1,0.1,0.5);
        serverWorld.spawnParticles(SKparticlesInit.BLOOD_PARTICLE,getX(),getBodyY(0.5),getZ(),15,1.5,1.2,1.5,0.2);
        serverWorld.spawnParticles(new ParryParticleEffect(new Vector3f(0,1.0f,0),1),getX(),getBodyY(0.5),getZ(),1,0.1,0.1,0.1,0.2);
        serverWorld.spawnParticles(new DustParticleEffect(DustParticleEffect.RED,1.0f),getX(),getBodyY(0.5),getZ(),18,0.1,0.1,0.1,1.4);
        serverWorld.spawnParticles(new DustParticleEffect(DustParticleEffect.RED,1.0f),getX(),getBodyY(0.5),getZ(),50,2,2,2,3);
        serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, SKItems.DECOAGULATOR.getDefaultStack()),getX(),getBodyY(0.5),getZ(),50,2,2,2,3);
        float range = 1.8f*1.25f*1.5f;
        for(LivingEntity living : serverWorld.getEntitiesByClass(LivingEntity.class,getBoundingBox().expand(5),LivingEntity::isAlive)){
            if(living instanceof HostileEntityStringBoostAccessor accessor&& getPos().distanceTo(living.getPos().add(0,living.getHeight()/2.0,0))<range){
                float newRage = (float) (accessor.getHemorrhage()+0.4);
                living.damage(serverWorld,getDamageSources().explosion(this,this),4.0f);
                Vec3d dir = living.getVelocity();
                if(living == getOwner()){
                    newRage+=0.4f;
                    System.out.println("WOWIEEE");
                    living.setVelocity(living.getVelocity().x,2,living.getVelocity().z);
                    dir=new Vec3d(living.getVelocity().x,2,living.getVelocity().z);
                }
                newRage=Math.min(newRage,1.0f);
                serverWorld.spawnParticles(SKparticlesInit.BLOOD_PARTICLE,living.getX(),living.getBodyY(0.5),living.getZ(),10,0.3,0.2,0.3,0.1);
                living.addStatusEffect(new StatusEffectInstance(SKstatusEffectInit.HEMORAGE_EFFECT,120,0));
                for(ServerPlayerEntity serverPlayerEntity: PlayerLookup.tracking(serverWorld,living.getBlockPos())){
                    ServerPlayNetworking.send(serverPlayerEntity,new HemoRagePayload(living.getId(),newRage));
                    ServerPlayNetworking.send(serverPlayerEntity,new VelSyncPayload(living.getId(),dir.getX(),dir.getY(),dir.getZ()));
                }
                accessor.setHemorrhage(newRage);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
    }
}
