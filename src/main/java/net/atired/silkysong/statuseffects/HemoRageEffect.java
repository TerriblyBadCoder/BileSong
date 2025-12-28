package net.atired.silkysong.statuseffects;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.accessors.LivingRenderStateStringBoostAccessor;
import net.atired.silkysong.networking.payloads.HemoRagePayload;
import net.atired.silkysong.networking.payloads.VelSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class HemoRageEffect extends StatusEffect {
    public HemoRageEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public void onApplied(AttributeContainer attributeContainer, int amplifier) {
        super.onApplied(attributeContainer, amplifier);
    }


    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        super.onRemoved(attributeContainer);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration%30==1;
    }

    @Override
    public void onEntityDamage(ServerWorld world, LivingEntity entity, int amplifier, DamageSource source, float amount) {
        if(entity instanceof HostileEntityStringBoostAccessor accessor){
            float newRage = (float) (accessor.getHemorrhage()+0.04*(1+amplifier)*amount);
            newRage=Math.min(newRage,1.0f);

            for(ServerPlayerEntity serverPlayerEntity: PlayerLookup.tracking(world,entity.getBlockPos())){
                ServerPlayNetworking.send(serverPlayerEntity,new HemoRagePayload(entity.getId(),newRage));
            }
            accessor.setHemorrhage(newRage);
        }
        super.onEntityDamage(world, entity, amplifier, source, amount);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        return super.applyUpdateEffect(world, entity, amplifier);
    }
}
