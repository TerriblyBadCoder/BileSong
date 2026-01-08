package net.atired.silkysong.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.silkysong.init.SKblockInit;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class HemoglobinItemEntityMixin extends Entity {
    @Shadow private int itemAge;

    public HemoglobinItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(method = "tick",at=@At("HEAD"))
    private void tickEvil(CallbackInfo ci){
        if(getBlockStateAtPos().getBlock() == SKblockInit.HEMOGLOBIN_BLOCK){
            itemAge-=9;
            if(itemAge<-126&&this.itemAge != -32768){
                itemAge=0;
            }
        }
    }
    @ModifyReturnValue(method = "getGravity()D",at=@At("RETURN"))
    private double myEvilBloodGravity(double original){
        if(getBlockStateAtPos().getBlock() == SKblockInit.HEMOGLOBIN_BLOCK){
            return 0.0;
        }
        return original;
    }
}
