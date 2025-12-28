package net.atired.silkysong.item;

import net.atired.silkysong.entities.DecoagulatorEntity;
import net.atired.silkysong.entities.IcosphereEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;

public class DecoagulatorItem extends SlimeCannonBallItem{
    public DecoagulatorItem(Item.Settings settings) {
        super(settings);
    }
    @Override
    public ProjectileEntity.ProjectileCreator<IcosphereEntity> getCreator() {
        return DecoagulatorEntity::new;
    }
}
