package net.atired.silkysong.item;

import net.atired.silkysong.entities.IcosphereEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class SlimeCannonBallItem extends Item implements ProjectileItem {
    public SlimeCannonBallItem(Item.Settings settings) {
        super(settings);
    }

    public ProjectileEntity.ProjectileCreator<IcosphereEntity> getCreator() {
        return IcosphereEntity::new;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.3F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (world instanceof ServerWorld serverWorld) {
            ProjectileEntity.spawnWithVelocity(this.getCreator(), serverWorld, itemStack, user, 0.0F, 1.2f, 1.0F);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new IcosphereEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
}
