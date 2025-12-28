package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKentityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

public class IcosphereEntity extends ThrownItemEntity {
    private final ExplosionBehavior EXPLOSION_BEHAVIOR = new ExplosionBehavior() {
        @Override
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            return false;
        }

        @Override
        public float calculateDamage(Explosion explosion, Entity entity, float amount) {
            if(entity==getOwner()){
                entity.setVelocity(entity.getVelocity().x,2,entity.getVelocity().z);
            }
            return super.calculateDamage(explosion, entity, amount);
        }
    };
    public IcosphereEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
        this.setItem(Items.SLIME_BALL.getDefaultStack());
    }

    public IcosphereEntity(World world, double x, double y, double z, ItemStack stack) {
        super(SKentityTypes.ICOSPHERE, world);
        this.setPos(x,y,z);

    }
    public IcosphereEntity(EntityType<? extends ThrownItemEntity> type,ServerWorld serverWorld, LivingEntity living, ItemStack stack) {
        super(type, living, serverWorld, stack);
    }

    public IcosphereEntity(ServerWorld serverWorld, LivingEntity living, ItemStack stack) {
        super(SKentityTypes.ICOSPHERE, living, serverWorld, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }



    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomData(ReadView view) {

    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity deflector, @Nullable Entity owner, boolean fromAttack) {
        boolean deflected = super.deflect(deflection, deflector, owner, fromAttack);
        if(deflected){
            this.setVelocity(this.getVelocity().multiply(1.5));
            this.age = Math.max(this.age,50);
        }
        return deflected;
    }
    public void explode(ServerWorld serverWorld){
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
    }
    @Override
    public void tick() {
        super.tick();
        if(age>50){
            this.setVelocity(getVelocity().multiply(0.8));
            this.addVelocity(0,0.02,0);
            if(age==68&&getWorld() instanceof ServerWorld serverWorld){
                explode(serverWorld);

            }
            if(age==78){
                discard();
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if(this.age>=50){
            this.setVelocity(this.getVelocity().multiply(-1));
            return;
        }
        Vec3d dir = blockHitResult.getSide().getDoubleVector().multiply(0.4).rotateY((float) (Math.random()-0.5)*2.1f);
        if(blockHitResult.getSide() == Direction.UP){
            setPosition(getPos().add(0,0.02,0));
            dir = new Vec3d(0.3,0,0).rotateY((float) (Math.random()-0.5)*3.14f*4.0f);
            setVelocity(dir.add(0,0.3,0));
        }
        else if(blockHitResult.getSide() == Direction.DOWN){
            setVelocity(0,-0.5,0);
        }
        else{
            setVelocity(dir.add(0,0.3,0));
        }

        playSound(SoundEvents.BLOCK_SLIME_BLOCK_STEP,1.0f,0.4f+random.nextFloat()/10.0f);
    }

    @Override
    protected void writeCustomData(WriteView view) {

    }
}
