package net.atired.silkysong.particles;

import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MirrorParticle<T extends ParryParticleEffect> extends SpriteBillboardParticle {
    float addToAngle = 0.0f;
    SpriteProvider spr;
    protected MirrorParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider, double xd, double yd, double zd,ParryParticleEffect particleType) {
        super(clientWorld, d, e, f,xd,yd,zd);
        this.x = d;
        this.y = e;
        this.z = f;
        this.red = particleType.getPosTo().x;
        this.green = particleType.getPosTo().y;
        this.blue = particleType.getPosTo().z;
        this.addToAngle = (float)Math.random()*10.0f;
        this.gravityStrength = 0.7f;
        this.scale = 0.3f;
        this.maxAge = 30;
        this.setSpriteForAge(spriteProvider);
        this.spr = spriteProvider;
        this.velocityY = 0.1f+Math.random()/5.0f;
        this.angle = (float) (Math.random()*2.0f*3.14f);
        this.lastAngle = this.angle;
    }

    @Override
    public void tick() {
        double oly = this.y;
        super.tick();
        oly = Math.abs(oly-this.y);
        this.setSpriteForAge(this.spr);
        this.lastAngle = this.angle;
        this.angle+= (1.0f-(this.age/(float)maxAge))*3f*(float)oly;
        if(this.angle>6.28f){
            this.angle-=6.28f;
        }
        this.red = MathHelper.lerp(0.2f,this.red,1.0f);
        this.green = MathHelper.lerp(0.2f,this.green,1.0f);
        this.blue = MathHelper.lerp(0.2f,this.blue,1.0f);
        this.alpha = Math.min(3-((float) age /maxAge)*3,1);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp((double)tickProgress, this.lastX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickProgress, this.lastY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickProgress, this.lastZ, this.z) - vec3d.getZ());
        Quaternionf quaternionf;
        quaternionf = new Quaternionf().rotateYXZ(this.angle,this.angle,this.angle);
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0F, 0), new Vector3f(-1.0f, 1.0f, 0), new Vector3f(1.0F, 1.0f, 0), new Vector3f(1.0F, -1.0f, 0)};
        float i = this.getSize(tickProgress);

        for(int j = 0; j < 4; ++j) {
            Vector3f vector3f = vector3fs[j];
            vector3f.mul(this.scale);
            vector3f.rotate(quaternionf);
            vector3f.add(f, g, h);
        }

        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();
        int o = getBrightness(tickProgress);
        vertexConsumer.vertex((float)vector3fs[0].x(), (float)vector3fs[0].y(), (float)vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).normal(1,0,0).light(o);
        vertexConsumer.vertex((float)vector3fs[1].x(), (float)vector3fs[1].y(), (float)vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).normal(1,0,0).light(o);
        vertexConsumer.vertex((float)vector3fs[2].x(), (float)vector3fs[2].y(), (float)vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue,this.alpha).normal(1,0,0).light(o);
        vertexConsumer.vertex((float)vector3fs[3].x(), (float)vector3fs[3].y(), (float)vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).normal(1,0,0).light(o);

        vertexConsumer.vertex((float)vector3fs[3].x(), (float)vector3fs[3].y(), (float)vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).normal(-1,0,0).light(o);
        vertexConsumer.vertex((float)vector3fs[2].x(), (float)vector3fs[2].y(), (float)vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).normal(-1,0,0).light(o);
        vertexConsumer.vertex((float)vector3fs[1].x(), (float)vector3fs[1].y(), (float)vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).normal(-1,0,0).light(o);
        vertexConsumer.vertex((float)vector3fs[0].x(), (float)vector3fs[0].y(), (float)vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).normal(-1,0,0).light(o);

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<ParryParticleEffect> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(ParryParticleEffect particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new MirrorParticle<>(level, x, y, z, this.sprites, dx, dy, dz,particleType);
        }
    }
}
