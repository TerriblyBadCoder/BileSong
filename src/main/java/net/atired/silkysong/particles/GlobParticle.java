package net.atired.silkysong.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class GlobParticle<T extends ParticleEffect> extends SpriteBillboardParticle {
    float addToAngle = 0.0f;
    SpriteProvider spr;
    protected GlobParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider, double xd, double yd, double zd) {
        super(clientWorld, d, e, f,xd,yd,zd);
        this.x = d;
        this.y = e;
        this.z = f;
        this.addToAngle = (float)Math.random()*10.0f;
        this.red = (float) 1.0f;
        this.green = 1.0f;
        this.blue =(float)1.0f;
        this.gravityStrength = 1.7f;
        this.velocityMultiplier=0.95f;
        this.scale = 0.3f;
        this.maxAge = 30;
        this.setSpriteForAge(spriteProvider);
        this.spr = spriteProvider;
        this.velocityY = 0.1f;
        this.velocityX = xd;
        this.velocityZ = zd;
        this.alpha=0.0f;
        this.angle = 0.0f;
        this.lastAngle = this.angle;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spr);
        this.lastAngle = this.angle;
        this.angle=(float)(this.age/2.0f+this.addToAngle)/16.0f;
        this.alpha = Math.min(3-((float) age /maxAge)*3, MathHelper.clamp(age/4.0f,0.0f,1.0f) );

    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new GlobParticle<>(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
