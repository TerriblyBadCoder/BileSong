package net.atired.silkysong.item;

import net.atired.silkysong.init.SKparticlesInit;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class TwistedMirrorItem extends Item {
    public TwistedMirrorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(stack.get(DataComponentTypes.POTION_CONTENTS).hasEffects()){
            if(user.getWorld() instanceof ServerWorld serverWorld){
                Color colours = new Color(stack.get(DataComponentTypes.POTION_CONTENTS).getColor(16777215));
                serverWorld.playSound(null,user.getBlockPos(),SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS,1.4f,0.7f);
                serverWorld.spawnParticles(new ParryParticleEffect(new Vector3f(colours.getRed()/255f,colours.getGreen()/255f,colours.getBlue()/255f),3),user.getX(),user.getBodyY(0.8),user.getZ(),12,0.3,0.2,0.3,0.6);
                if(!user.getAbilities().creativeMode){
                    user.damage(serverWorld,user.getDamageSources().magic(),user.getHealth()*0.3f);
                }
            }
            for(StatusEffectInstance instance : stack.get(DataComponentTypes.POTION_CONTENTS).getEffects()){
                user.addStatusEffect(instance,user);
            }
            stack.set(DataComponentTypes.POTION_CONTENTS,PotionContentsComponent.DEFAULT);
            user.addVelocity(new Vec3d(0,0.3,0));
            user.swingHand(hand);
            return ActionResult.SUCCESS;
        }

        return super.use(world, user, hand);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(!stack.get(DataComponentTypes.POTION_CONTENTS).hasEffects()) {
            stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.empty(), target.getStatusEffects().stream().toList(), Optional.empty()));
            target.clearStatusEffects();
        }
        if(target.getWorld() instanceof ServerWorld serverWorld){
            Color colours = new Color(stack.get(DataComponentTypes.POTION_CONTENTS).getColor(16777215));
            serverWorld.playSound(target,target.getBlockPos(),SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS,1.4f,0.7f);
            serverWorld.spawnParticles(new ParryParticleEffect(new Vector3f(colours.getRed()/255f,colours.getGreen()/255f,colours.getBlue()/255f),3),target.getX(),target.getBodyY(0.8),target.getZ(),12,0.3,0.2,0.3,0.6);
        }
        super.postHit(stack, target, attacker);
    }
}
