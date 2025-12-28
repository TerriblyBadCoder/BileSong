package net.atired.silkysong.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.SKtagInit;
import net.atired.silkysong.item.AmadunnoSwordItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow public abstract void fill(RenderPipeline pipeline, int x1, int y1, int x2, int y2, int color);

    @Shadow public abstract boolean scissorContains(int x, int y);

    @Shadow @Final private Matrix3x2fStack matrices;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "drawItemBar(Lnet/minecraft/item/ItemStack;II)V",at = @At("TAIL"))
    private void drawEvilBar(ItemStack stack, int x, int y, CallbackInfo ci){
        if (stack.isItemBarVisible()&& EnchantmentHelper.hasAnyEnchantmentsIn(stack, SKtagInit.HEARTSTRINGS_TAG)&&stack.getItem() instanceof AmadunnoSwordItem ) {
            int i = x + 2;
            int j = y + 13;
            int step = stack.getItemBarStep();
            if(step>7){
                this.fill(RenderPipelines.GUI, i+7, j, i +step, j + 1, ColorHelper.fullAlpha(ColorHelper.fromFloats(1.0f,1.0f,0.5f,0.4f)));
            }
            this.fill(RenderPipelines.GUI, i+6, j, i + 7, j + 2, -16777216);

        }
    }
    @Inject(method = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;III)V",at=@At("HEAD"),cancellable = true)
    private void drawVitricItem(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, CallbackInfo ci) {
        if (stack.getItem() == SKItems.TADPOLE) {
            Quaternionf quaternionf3 = new Quaternionf();
            float scaled = 0.95f-(float)(Math.sin(this.client.player.getWorld().getTime() / 13f + (x + y) / 12f) * 0.05f);
            float fracted = ((float)((int)(this.client.player.getWorld().getTime()%24000)/2)*20.0f+x+y);
            this.matrices.scaleAround(scaled,scaled,x+8,y+8f);
            this.matrices.rotateAbout((float) (Math.cos(this.client.player.getWorld().getTime() /4f + (x + y) / 12f) * 0.2f),x+8,y+8);
            this.matrices.translate((float) Math.sin(fracted), (float) -Math.cos(fracted*0.9f));
        }
    }
    @Inject(method = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;III)V",at=@At("TAIL"),cancellable = true)
    private void drawVitricItemAgain(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, CallbackInfo ci) {
        if (stack.getItem() == SKItems.TADPOLE) {
            Quaternionf quaternionf3 = new Quaternionf();

            float fracted = ((float)((int)(this.client.player.getWorld().getTime()%24000)/2)*20.0f+x+y);
            float scaled = 0.95f-(float)(Math.sin(this.client.player.getWorld().getTime() / 13f + (x + y) / 12f) * 0.05f);
            this.matrices.translate((float) -Math.sin(fracted), (float) Math.cos(fracted*0.9f));
            this.matrices.scaleAround(1.0f/scaled,1.0f/scaled,x+8,y+8f);
            this.matrices.rotateAbout((float) -(Math.cos(this.client.player.getWorld().getTime() /4f + (x + y) / 12f) * 0.2f),x+8,y+8);
        }
    }
}
