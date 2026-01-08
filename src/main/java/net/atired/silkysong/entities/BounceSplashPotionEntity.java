package net.atired.silkysong.entities;

import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.SKentityTypes;
import net.atired.silkysong.init.SKparticlesInit;
import net.atired.silkysong.init.SKtagInit;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SplashPotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;

public class BounceSplashPotionEntity extends PotionEntity {
    private static final TrackedData<Integer> BOUNCES = DataTracker.registerData(BounceSplashPotionEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> LERPED = DataTracker.registerData(BounceSplashPotionEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public BounceSplashPotionEntity(EntityType<? extends PotionEntity> entityType, World world) {
        super(entityType, world);
    }
    public BounceSplashPotionEntity(World world, LivingEntity owner, ItemStack stack) {
        super(SKentityTypes.BOUNCING_POTION, world, owner, stack);
    }

    public BounceSplashPotionEntity(World world, double x, double y, double z, ItemStack stack) {
        super(SKentityTypes.BOUNCING_POTION, world, x, y, z, stack);
    }
    @Override
    protected void onCollision(HitResult hitResult) {
        World var3 = this.getWorld();
        if (var3 instanceof ServerWorld serverWorld) {
            ItemStack itemStack = this.getStack();
            PotionContentsComponent potionContentsComponent = (PotionContentsComponent)itemStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
            if (potionContentsComponent.matches(Potions.WATER)) {
                this.explodeWaterPotion(serverWorld);
            } else if (potionContentsComponent.hasEffects()) {
                int col = getStack().get(DataComponentTypes.POTION_CONTENTS).getColor();
                Vector3f colour = new Vector3f((col & 0xFF0000)>>16,(col & 0x00FF00)>>8,(col & 0x0000FF)).div(255.0f);
                serverWorld.spawnParticles(new ParryParticleEffect(colour,3),getX(),getY(),getZ(),7,0.1,0.1,0.1,0.5);
                this.spawnAreaEffectCloud(serverWorld, itemStack, hitResult);
            }

            int i = potionContentsComponent.potion().isPresent() && ((Potion)((RegistryEntry)potionContentsComponent.potion().get()).value()).hasInstantEffect() ? 2007 : 2002;
            serverWorld.syncWorldEvent(i, this.getBlockPos(), potionContentsComponent.getColor());
            if(hitResult.getType()== HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult){
                Vec3d dir = getVelocity().multiply(1,0.9,1);
                Vec3d normal =blockHitResult.getSide().getDoubleVector();
                if(normal.y!=0){
                    dir = dir.multiply(1,-1,1).add(0,-Math.abs(dir.y)/dir.y*0.1,0);
                }
                if(normal.x!=0){
                    dir = dir.multiply(-1,1,1);
                }
                if(normal.z!=0){
                    dir = dir.multiply(1,1,-1);
                }
                if(getBounces()<=0){
                   this.discard();
                }
                setBounces(getBounces()-1);
                setVelocity(dir);
            }
        }
    }
    @Override
    public void readCustomData(ReadView view) {
        super.readCustomData(view);
        setBounces(view.getInt("bounces",1));
    }

    @Override
    public void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt("bounces",getBounces());
    }

    @Override
    public void tick() {
        if(getBounces()<=0&&getWorld() instanceof ServerWorld serverWorld){
            setLerp(getLerp()*0.7f);
        }
        super.tick();
    }

    public float getLerp(){
        return this.dataTracker.get(LERPED);
    }
    public void setLerp(float lerp){
        this.dataTracker.set(LERPED,lerp);
    }
    public int getBounces(){
        return this.dataTracker.get(BOUNCES);
    }
    public void setBounces(int bounces){
        this.dataTracker.set(BOUNCES,bounces);
    }
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(LERPED,1.0f);
        builder.add(BOUNCES, 4);
        super.initDataTracker(builder);
    }
    private void explodeWaterPotion(ServerWorld world) {
        Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.getWorld().getEntitiesByClass(LivingEntity.class, box, AFFECTED_BY_WATER);
        Iterator var4 = list.iterator();

        while(var4.hasNext()) {
            LivingEntity livingEntity = (LivingEntity)var4.next();
            double d = this.squaredDistanceTo(livingEntity);
            if (d < 16.0) {
                if (livingEntity.hurtByWater()) {
                    livingEntity.damage(world, this.getDamageSources().indirectMagic(this, this.getOwner()), 1.0F);
                }

                if (livingEntity.isOnFire() && livingEntity.isAlive()) {
                    livingEntity.extinguishWithSound();
                }
            }
        }

        List<AxolotlEntity> list2 = this.getWorld().getNonSpectatingEntities(AxolotlEntity.class, box);
        Iterator var9 = list2.iterator();

        while(var9.hasNext()) {
            AxolotlEntity axolotlEntity = (AxolotlEntity)var9.next();
            axolotlEntity.hydrateFromPotion();
        }

    }

    @Override
    protected void spawnAreaEffectCloud(ServerWorld world, ItemStack stack, HitResult hitResult) {
        PotionContentsComponent potionContentsComponent = (PotionContentsComponent)stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
        float f = (Float)stack.getOrDefault(DataComponentTypes.POTION_DURATION_SCALE, 1.0F);
        Iterable<StatusEffectInstance> iterable = potionContentsComponent.getEffects();
        Box box = this.getBoundingBox().offset(hitResult.getPos().subtract(this.getPos()));
        Box box2 = box.expand(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, box2);
        float g = ProjectileUtil.getToleranceMargin(this);
        if (!list.isEmpty()) {
            Entity entity = this.getEffectCause();
            Iterator var12 = list.iterator();

            while(true) {
                LivingEntity livingEntity;
                double d;
                do {
                    do {
                        if (!var12.hasNext()) {
                            return;
                        }

                        livingEntity = (LivingEntity)var12.next();
                    } while(!livingEntity.isAffectedBySplashPotions());

                    d = box.squaredMagnitude(livingEntity.getBoundingBox().expand((double)g));
                } while(!(d < 16.0));

                double e = 1.0 - Math.sqrt(d) / 4.0;
                Iterator var18 = iterable.iterator();

                while(var18.hasNext()) {
                    StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var18.next();
                    RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
                    if (((StatusEffect)registryEntry.value()).isInstant()) {
                        ((StatusEffect)registryEntry.value()).applyInstantEffect(world, this, this.getOwner(), livingEntity, statusEffectInstance.getAmplifier(), e);
                    } else {
                        int i = statusEffectInstance.mapDuration((baseDuration) -> {
                            return (int)(e * (double)baseDuration * (double)f + 0.5);
                        });
                        StatusEffectInstance statusEffectInstance2 = new StatusEffectInstance(registryEntry, i, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
                        if (!statusEffectInstance2.isDurationBelow(20)) {
                            livingEntity.addStatusEffect(statusEffectInstance2, entity);
                        }
                    }
                }
            }
        }
    }


    @Override
    protected Item getDefaultItem() {
        return SKItems.BOUNCING_POTION;
    }
}
