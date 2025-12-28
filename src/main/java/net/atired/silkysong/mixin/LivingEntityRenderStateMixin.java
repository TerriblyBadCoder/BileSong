package net.atired.silkysong.mixin;

import net.atired.silkysong.accessors.LivingRenderStateStringBoostAccessor;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingRenderStateStringBoostAccessor {

    @Unique
    private int strings = 0;
    @Unique
    private float stringBoost = 0.0f;
    @Unique
    private float hemorrhage = 0.0f;
    @Override
    public void silkySong$setStringBoost(float boost) {
        this.stringBoost = boost;
    }

    @Override
    public float silkySong$getStringBoost() {
        return this.stringBoost;
    }

    @Override
    public void silkySong$setHemorrhage(float boost) {
        this.hemorrhage=boost;
    }

    @Override
    public float silkySong$getHemorrhage() {
        return this.hemorrhage;
    }

    @Override
    public void silkySong$setStrings(int boost) {
        this.strings = boost;
    }

    @Override
    public int silkySong$getStrings() {
        return this.strings;
    }
}
