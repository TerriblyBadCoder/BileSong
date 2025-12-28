package net.atired.silkysong.item;

import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CurledChildItem extends Item {
    public CurledChildItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(!user.getAbilities().creativeMode)
            stack.setCount(stack.getCount()-1);
        user.playSound(SoundEvents.ENTITY_HORSE_DEATH,0.3f,1.7f+user.getRandom().nextFloat()/10.0f);
        user.playSound(SoundEvents.BLOCK_ANVIL_HIT,1.2f,1.0f-user.getRandom().nextFloat()/10.0f);
        if(MinecraftClient.getInstance().player==user){
            SilkySongClient.PROXY.slopped=1.0f;
        }
        user.swingHand(Hand.OFF_HAND);
        user.swingHand(Hand.MAIN_HAND);
        for (int i = 0; i < 12; i++) {
            Vec3d dir = new Vec3d(1,0,0).rotateY(user.getRandom().nextFloat()*3.14f*2.0f).multiply(0.15+user.getRandom().nextFloat()/10.0f).add(0,0.1+user.getRandom().nextFloat()/10.0f,0);
            user.getWorld().addParticleClient(SKparticlesInit.JUMP_GLOB_PARTICLE,user.getParticleX(0.4),user.getBodyY(0.5f+user.getRandom().nextFloat()/2.0f),user.getParticleZ(0.4),dir.x,dir.y,dir.z);
        }
        for (int i = 0; i < 12; i++) {
            Vec3d dir = new Vec3d(1,0,0).rotateY(user.getRandom().nextFloat()*3.14f*2.0f).multiply(0.6+user.getRandom().nextFloat()/2.0f).add(0,0.1+user.getRandom().nextFloat(),0);
            user.getWorld().addParticleClient(SKparticlesInit.JUMP_GLOB_PARTICLE,user.getParticleX(0.4),user.getBodyY(0.5f+user.getRandom().nextFloat()/2.0f),user.getParticleZ(0.4),dir.x,dir.y,dir.z);
        }
        user.getItemCooldownManager().set(stack,20);
        user.setVelocity(new Vec3d(user.getVelocity().x,0.4,user.getVelocity().z));
        return super.use(world, user, hand);
    }
}
