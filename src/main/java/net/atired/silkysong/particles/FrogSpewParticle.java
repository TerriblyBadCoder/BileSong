package net.atired.silkysong.particles;

import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FrogSpewParticle<T extends ParryParticleEffect> extends SpriteBillboardParticle {
    private final Vec3d direction;
    private float yaw;
    private SpriteProvider provider;
    private float pitch;
    protected FrogSpewParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider, double xd, double yd, double zd, ParryParticleEffect effect) {
        super(clientWorld, d, e, f);
        Vec3d dir;
        this.provider = spriteProvider;
        this.velocityX = xd;
        this.velocityY = yd;
        this.velocityZ = zd;
        this.setSpriteForAge(spriteProvider);
        dir = new Vec3d(effect.getPosTo());
        dir = dir.normalize();
        this.velocityX = dir.x*0.1;
        this.velocityY = dir.y*0.1;
        this.velocityZ = dir.z*0.1;
        this.scale = 1.0f;
        this.velocityMultiplier = 0.9f;
        this.direction = dir;
        this.maxAge = 13;
        this.pitch = (float)Math.asin(-dir.y);
        this.yaw = (float)Math.atan2(dir.x, dir.z);
    }

    @Override
    public void tick() {
        Vec3d dir = this.direction;
        this.pitch = (float)Math.asin(-dir.y);
        this.setSpriteForAge(provider);
        this.yaw = (float)Math.atan2(dir.x, dir.z);
        this.scale += ((float) this.age /this.maxAge)/12f;
        super.tick();

    }
    public void buildGeometryAlt(VertexConsumer vertexConsumer, Camera camera, float tickDelta,float zrot) {
        this.alpha = (float) Math.clamp(1.4f-Math.abs(((float) this.age /this.maxAge)-0.5f)*2,0,1);
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp((double)tickDelta, this.lastX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickDelta, this.lastY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickDelta, this.lastZ, this.z) - vec3d.getZ());
        Quaternionf quaternionf;
        quaternionf = new Quaternionf().rotateYXZ(this.yaw,this.pitch,zrot);
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0F, 0), new Vector3f(-1.0f, 1.0f, 0), new Vector3f(1.0F, 1.0f, 0), new Vector3f(1.0F, -1.0f, 0)};
        float i = this.getSize(tickDelta);
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
        int o = 240;
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
        buildGeometryAlt(vertexConsumer,camera,tickProgress,this.age/4f);
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
            return new FrogSpewParticle<>(level, x, y, z, this.sprites, dx, dy, dz,particleType);
        }
    }
}
