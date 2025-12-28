package net.atired.silkysong.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.SKcomponents;
import net.atired.silkysong.item.AmadunnoSwordItem;
import net.atired.silkysong.networking.payloads.HoisterPayload;
import net.atired.silkysong.networking.payloads.ParryPayload;
import net.atired.silkysong.networking.payloads.SilkenCloakPayload;
import net.atired.silkysong.networking.payloads.VelSyncPayload;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.text.MutableText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isInvulnerableTo(ServerWorld world, DamageSource source);

    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    @Shadow @Final private ItemCooldownManager itemCooldownManager;

    @Shadow protected abstract boolean shouldAlwaysDropExperience();

    @Shadow public abstract PlayerInventory getInventory();

    @Shadow public abstract ItemCooldownManager getItemCooldownManager();

    @Shadow protected abstract MutableText addTellClickEvent(MutableText component);

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @ModifyReturnValue(method = "getMovementSpeed()F",at=@At("RETURN"))
    private float getMySpeed(float original){
        if(((LivingEntity)this) instanceof HostileEntityStringBoostAccessor accessor){
            return original*(1.0f+accessor.getStringBoost()*0.5f)*(1.0f+accessor.getHemorrhage()/3.0f);
        }
        return original;
    }
    @Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",at=@At("HEAD"),cancellable = true)
    private void onDamageCancel(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(!(this.activeItemStack.getItem() instanceof AmadunnoSwordItem)&& getEquippedStack(EquipmentSlot.CHEST).getItem() == SKItems.SILKEN_MANTLE&&!getItemCooldownManager().isCoolingDown(getEquippedStack(EquipmentSlot.CHEST))){
            getItemCooldownManager().set(getEquippedStack(EquipmentSlot.CHEST),100);
            if(((LivingEntity)this) instanceof HostileEntityStringBoostAccessor accessor){
                accessor.setStringBoost(accessor.getStringBoost()+0.5f);
            }
            Vec3d velocity = getVelocity();
            for(ServerPlayerEntity serverPlayerEntity:PlayerLookup.tracking(world,getBlockPos())){
                ServerPlayNetworking.send(serverPlayerEntity,new SilkenCloakPayload(getId(),velocity.x,velocity.y,velocity.z));
            }
            cir.cancel();
        }
    }
    @ModifyVariable(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",at=@At("HEAD"),ordinal = 0)
    private float onDamageParry(float value,ServerWorld world, DamageSource source, float amount){

            if(this.activeItemStack.getItem() instanceof AmadunnoSwordItem amadunnoSwordItem){
                ItemStack stack = this.activeItemStack;

                Vec3d vec3d = getRotationVec(0).multiply(-1.5);
                if(source.getAttacker()==null){
                    vec3d = vec3d.multiply(0.2);
                }
                this.addVelocity(vec3d);
                System.out.println("WOW");
                if((LivingEntity)(this) instanceof  ServerPlayerEntity serverPlayerEntity){
                    if(source.getAttacker() instanceof LivingEntity target){
                        if(target instanceof HostileEntityStringBoostAccessor accessor){

                            int partime2 = stack.get(SKcomponents.PARRY_TIME).intValue();
                            stack.set(SKcomponents.PARRY_TIME,Math.min(partime2+accessor.silkySong$getDunnoStringCount()*16,200));
                            accessor.silkySong$setDunnoStringCount(0);
                            for(ServerPlayerEntity a : PlayerLookup.tracking(serverPlayerEntity.getWorld(),target.getBlockPos())){
                                ServerPlayNetworking.send(a,new HoisterPayload(target.getId(),0,new Vector3f(),new Vector3f()));
                            }
                        }
                    }
                    Vec3d rot = serverPlayerEntity.getRotationVec(0);
                    if(serverPlayerEntity.getItemUseTime()<6){
                        serverPlayerEntity.getWorld().spawnParticles(new ParryParticleEffect(serverPlayerEntity.getRotationVec(0).multiply(-1).toVector3f(),0),
                                getX()+rot.getX()*0.3,getEyeY()+rot.getY()*0.3,getZ()+rot.getZ()*0.3,1,0.05,0.05,0.05,0.0);

                        int partime = stack.get(SKcomponents.PARRY_TIME).intValue();
                        stack.set(SKcomponents.PARRY_TIME,Math.min(partime+4,200));
                        serverPlayerEntity.stopUsingItem();
                        serverPlayerEntity.setCurrentHand(Hand.MAIN_HAND);
                        ServerPlayNetworking.send(serverPlayerEntity,new ParryPayload(this.getId(),0,new Vector3f(),vec3d.toVector3f()));
                        return 0.0f;
                    }
                    ServerPlayNetworking.send(serverPlayerEntity,new ParryPayload(this.getId(),1,new Vector3f(),vec3d.toVector3f()));
                    return value*0.33f;
                }
            }
        return value;
    }
}
