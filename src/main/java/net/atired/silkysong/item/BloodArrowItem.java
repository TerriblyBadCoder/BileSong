package net.atired.silkysong.item;

import net.atired.silkysong.entities.BloodArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BloodArrowItem extends ArrowItem {
    public BloodArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        BloodArrowEntity arrowEntity = new BloodArrowEntity(world);
        Vec3d pos = shooter.getEyePos();
        arrowEntity.setPos(pos.getX(),pos.getY(),pos.getZ());
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        arrowEntity.setOwner(shooter);
        return arrowEntity;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        BloodArrowEntity arrowEntity = new BloodArrowEntity(world);
        arrowEntity.setPos(pos.getX(),pos.getY(),pos.getZ());

        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }
}
