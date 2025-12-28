package net.atired.silkysong.item;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.init.SKcomponents;
import net.atired.silkysong.init.SKparticlesInit;
import net.atired.silkysong.init.SKtagInit;
import net.atired.silkysong.networking.payloads.HoisterPayload;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class AmadunnoSwordItem extends Item  {
    public AmadunnoSwordItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(hand == Hand.MAIN_HAND&&itemStack.get(SKcomponents.PARRY_TIME).intValue()>20){
            user.setCurrentHand(hand);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return (stack.get(SKcomponents.PARRY_TIME).intValue()*13/200);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.hsvToRgb(0.2f, 0.5F, 1.0F);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int partime = stack.get(SKcomponents.PARRY_TIME).intValue();
        if(partime>0)
            stack.set(SKcomponents.PARRY_TIME,partime-1);
        else{
            user.stopUsingItem();
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        return super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 3000;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(target.getWorld() instanceof ServerWorld serverWorld && !attacker.isOnGround()){
            serverWorld.spawnParticles(SKparticlesInit.STARRY_PARTICLE,target.getX(),target.getBodyY(0.8),target.getZ(),5,target.getWidth()/2.0,target.getHeight()/6.0,target.getWidth()/2.0,0.12);
        }
        int partime = stack.get(SKcomponents.PARRY_TIME).intValue();
        stack.set(SKcomponents.PARRY_TIME,Math.min(partime+8,200));

        super.postHit(stack, target, attacker);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        float bonusdamage = 0.0f;
        if(target!=null&&damageSource.getAttacker()!=null&&baseAttackDamage>0.8){
            if(damageSource.getAttacker() instanceof PlayerEntity player&& EnchantmentHelper.hasAnyEnchantmentsIn(player.getMainHandStack(), SKtagInit.HEARTSTRINGS_TAG)){

                int partime = player.getMainHandStack().get(SKcomponents.PARRY_TIME).intValue();
                if(player.getWorld() instanceof ServerWorld serverWorld) {
                    if (target instanceof HostileEntityStringBoostAccessor accessor&&accessor.silkySong$getDunnoStringCount()<5) {
                        Vec3d rot = player.getRotationVec(0).multiply(-0.4);
                        serverWorld.spawnParticles(new ParryParticleEffect(new Vector3f(0,1,0),0),
                                target.getX(),target.getEyeY()+0.1,target.getZ(),1,0.05,0.05,0.05,0.0);
                        serverWorld.spawnParticles(SKparticlesInit.STARRY_PARTICLE,
                                target.getX(),target.getEyeY(),target.getZ(),3,0.15,0.15,0.15,0.0);

                        serverWorld.playSound(target,target.getBlockPos(),SoundEvents.BLOCK_COBWEB_BREAK,SoundCategory.PLAYERS,2.5f,1.3f);
                        serverWorld.playSound(target,target.getBlockPos(),SoundEvents.BLOCK_COBWEB_PLACE,SoundCategory.PLAYERS,2.0f,1.3f);
                        accessor.silkySong$setDunnoStringCount(accessor.silkySong$getDunnoStringCount() + 1);
                        for (ServerPlayerEntity a : PlayerLookup.tracking(serverWorld, target.getBlockPos())) {
                            ServerPlayNetworking.send(a, new HoisterPayload(target.getId(), accessor.silkySong$getDunnoStringCount(), new Vector3f(), new Vector3f()));
                        }
                    }
                }
                if(partime>100){
                    partime-=32;
                    player.getMainHandStack().set(SKcomponents.PARRY_TIME,Math.max(partime,100));
                    if(partime>100){
                        if(player.getWorld().isClient()&& MinecraftClient.getInstance().player==player){
                            SilkySongClient.PROXY.vingette=0.9f;
                            SilkySongClient.PROXY.colours = new Vector3f(0.95f,1.0f,0.6f);
                        }
                        if(player.getWorld() instanceof ServerWorld serverWorld){

                            serverWorld.playSound(target,target.getBlockPos(),SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,SoundCategory.PLAYERS,1.3f,1.4f);
                            serverWorld.playSound(target,target.getBlockPos(),SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,SoundCategory.PLAYERS,1.5f,1.0f);
                            serverWorld.playSound(target,target.getBlockPos(),SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK,SoundCategory.PLAYERS,0.9f,1.0f);
                            Vec3d rot = player.getRotationVec(0).multiply(-0.4);
                            serverWorld.spawnParticles(new ParryParticleEffect(player.getRotationVec(0).multiply(-1).toVector3f(),0),
                                    target.getX()+rot.getX()*0.3,target.getEyeY()+rot.getY()*0.3,target.getZ()+rot.getZ()*0.3,1,0.05,0.05,0.05,0.0);

                        }

                        bonusdamage+=4.0f;
                    }
                }
            }
            Entity attacker = damageSource.getAttacker();
            if(damageSource.getAttacker().isOnGround())
            {

                attacker.setVelocity(0,attacker.getVelocity().y,0);
                Vec3d addvel = attacker.getRotationVector().multiply(1,0,1).normalize().multiply(-0.15).add(0,0.1,0);
                if(attacker instanceof PlayerEntity player && player.isSprinting()){
                    addvel = addvel.multiply(2.6,2.0,2.6);

                    attacker.addVelocity(addvel);
                }
                return super.getBonusAttackDamage(target, baseAttackDamage, damageSource)+2.0f+bonusdamage;
            }
            else{
                if(attacker.getVelocity().y<0.0)
                    attacker.setVelocity(attacker.getVelocity().multiply(1,0,1));
                attacker.addVelocity(attacker.getRotationVector().multiply(1,0,1).normalize().multiply(0.1).add(0,0.5,0));
            }
        }
        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource)+bonusdamage;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }
}
