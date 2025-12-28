package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.SKentityTypes;
import net.atired.silkysong.particles.SigilParticle;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BloodArrowEntity extends ArrowEntity {
    public BloodArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }
    public BloodArrowEntity(World world) {
        super(SKentityTypes.BLOOD_ARROW, world);

        setStack(SKItems.BLOOD_ARROW.getDefaultStack());
    }
    public BloodArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, x, y, z, stack, shotFrom);
    }
    @Override
    public void tick() {
        if(this.age%4==0&&getVelocity().length()>0.1){
            getWorld().addImportantParticleClient(new ParryParticleEffect(getVelocity().normalize().multiply(-1).toVector3f(),1),getX(),getY()+0.1,getZ(),0,0,0);
        }
        super.tick();
    }

    @Override
    protected void onHit(LivingEntity target) {
        if(getWorld() instanceof ServerWorld serverWorld){
            for (int i = 0; i < 3; i++) {
                SigilEntity entity = new SigilEntity(SKentityTypes.SIGIL,serverWorld);
                entity.setPosition(getPos());
                entity.age = i*12;
                entity.setSigilAge(i*24);
                serverWorld.spawnEntity(entity);
            }
        }

        super.onHit(target);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(entityHitResult.getEntity() instanceof LivingEntity living){
            Entity entity2 = getOwner();
            DamageSource damageSource = this.getDamageSources().arrow(this, (Entity)(entity2 != null ? entity2 : this));
            if(getWorld() instanceof ServerWorld serverWorld){
                living.damage(serverWorld,damageSource,0.5f);
            }
            if(getOwner() instanceof LivingEntity livingEntity){
                livingEntity.onAttacking(living);
            }
            onHit(living);
            living.setStuckArrowCount(living.getStuckArrowCount()+1);
            this.discard();
        }
        //Nope!
    }
}
