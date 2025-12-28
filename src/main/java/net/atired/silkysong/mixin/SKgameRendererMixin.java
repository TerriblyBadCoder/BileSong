package net.atired.silkysong.mixin;

import net.atired.silkysong.accessors.GameRendererPoolAccessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Pool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class SKgameRendererMixin implements GameRendererPoolAccessor {
    @Shadow @Final private Pool pool;

    @Override
    public Pool getPool() {
        return this.pool;
    }
}
