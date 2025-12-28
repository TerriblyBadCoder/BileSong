package net.atired.silkysong.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.silkysong.client.SilkySongClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ClientPlayerEntity.class)
public abstract class GlobClientPlayerEntityMixin extends LivingEntity {
    @Shadow public abstract void openRidingInventory();

    protected GlobClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "applyMovementSpeedFactors(Lnet/minecraft/util/math/Vec2f;)Lnet/minecraft/util/math/Vec2f;",at=@At("RETURN"))
    private Vec2f getFunnyMovement(Vec2f original){
        if(!isOnGround()){
            Vec3d copy = new Vec3d(original.x* SilkySongClient.PROXY.slopped*0.3,0, original.y* SilkySongClient.PROXY.slopped*0.3).rotateY(-getYaw()/360*3.14f*2.0f);
            addVelocity(copy);
        }
        return original;
    }
}
