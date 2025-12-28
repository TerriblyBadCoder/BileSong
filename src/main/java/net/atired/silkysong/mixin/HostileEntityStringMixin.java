package net.atired.silkysong.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.SKblockInit;
import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class HostileEntityStringMixin extends Entity implements HostileEntityStringBoostAccessor {

    public HostileEntityStringMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract void stopRiding();
    @Unique
    private int strings = 0;

    @Unique
    private float hemorage = 0.0f;
    @Unique
    private float stringBoost = 0.0f;
    @Unique
    private float stringBoostBoost = 0.0f;

    private int hemoRageTickDelay =40;
    @Override
    public float getHemorrhage() {
        return this.hemorage;
    }
    @Override
    public void setHemorrhage(float boost) {
        this.hemoRageTickDelay =40;
        this.hemorage=boost;
    }
    @ModifyVariable(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",at=@At("HEAD"),ordinal = 0)
    private float damageBoost(float value){
        return value*(1.0f+this.hemorage);
    }
    @Override
    public void setStringBoost(float boost) {
        this.stringBoost = Math.clamp(boost,0.0f,1.0f);
    }
    @Override
    public void setStringBoost2(float boost) {
        this.stringBoostBoost = Math.clamp(boost,0.0f,1.0f);
    }
    @ModifyReturnValue(method = "getMovementSpeed()F",at=@At("RETURN"))
    private float getMySpeed(float original){
        return original*(1.0f+this.stringBoost);
    }
    @Inject(method= "dropEquipment(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;Z)V",at=@At("HEAD"))
    private void dropMySillyStrings(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci){
        if(this.stringBoost>0.2){
            ItemStack stack = SKItems.SILK.getDefaultStack();
            stack.setCount((int)(this.stringBoost*3.0+1));
            dropStack(world,stack);
        }
    }
    @Inject(method = "tick",at=@At("TAIL"))
    private void lowerString(CallbackInfo ci){
        if(getSteppingBlockState().getBlock() == SKblockInit.SILK){
            this.stringBoost = Math.min(this.stringBoost+0.05f,1.0f);
        }
        if(this.stringBoost>0.0){
            this.stringBoost = Math.clamp(this.stringBoost-0.01f+stringBoostBoost,0.0f,1.0f);
            this.stringBoostBoost*=0.9f;
            if(getWorld().isClient()&&random.nextFloat()<0.4&&this.stringBoostBoost>0.02){
                getWorld().addParticleClient(SKparticlesInit.SILK_PARTICLE,getParticleX(0.6),getBodyY(0.9),getParticleZ(0.6),0.0,0.0,0.0);
            }
        }
        if(this.hemorage>0){
            if(this.hemoRageTickDelay>0){
                this.hemoRageTickDelay-=1;
            }else{
                this.hemorage=Math.max(0.0f,this.hemorage-=0.05f);
            }
        }
    }

    @Override
    public void silkySong$setDunnoStringCount(int count) {
        this.strings = Math.min(count,5);
    }

    @Override
    public int silkySong$getDunnoStringCount() {
        return this.strings;
    }

    @Override
    public float getStringBoost2() {
        return this.stringBoostBoost;
    }
    @Override
    public float getStringBoost() {
        return this.stringBoost;
    }
}
