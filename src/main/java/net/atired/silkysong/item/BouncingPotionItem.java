package net.atired.silkysong.item;

import net.atired.silkysong.entities.BounceSplashPotionEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SplashPotionEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class BouncingPotionItem extends ThrowablePotionItem {
    public BouncingPotionItem(Item.Settings settings) {
        super(settings);
    }

    protected PotionEntity createEntity(ServerWorld world, LivingEntity user, ItemStack stack) {
        return new BounceSplashPotionEntity(world, user, stack);
    }

    protected PotionEntity createEntity(World world, Position pos, ItemStack stack) {
        return new BounceSplashPotionEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    @Override
    public Text getName(ItemStack stack) {
        PotionContentsComponent potionContentsComponent = (PotionContentsComponent)stack.get(DataComponentTypes.POTION_CONTENTS);
        Text test = Text.translatable("item.silkysong.bouncing_potion");
        if(potionContentsComponent.getEffects()!=null&&potionContentsComponent.getEffects().iterator().hasNext())
        test = test.copy().append(
                Text.translatable(potionContentsComponent.getEffects().iterator().next().getTranslationKey()));
        else
            test = test.copy().append("Nothing");
        return test;
    }
}
