package net.atired.silkysong.particles.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.atired.silkysong.init.SKparticlesInit;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;

import java.util.List;
import java.util.Locale;

public class ParryParticleEffect implements ParticleEffect {
    protected final Vector3f angle;
    protected final int heat;
    private static List<ParticleType<ParryParticleEffect>> TYPES =
            List.of(SKparticlesInit.PARRY_PARTICLE,SKparticlesInit.SIGIL_PARTICLE,SKparticlesInit.FROGSPEW_PARTICLE,SKparticlesInit.MIRROR_PARTICLE);
    public ParryParticleEffect(Vector3f posTo, int heated){
        this.angle = posTo;
        this.heat = heated;
    }
    public static final MapCodec<ParryParticleEffect> CODEC= RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(Codecs.VECTOR_3F.fieldOf("angle").forGetter((effect) -> {
            return effect.angle;
        }),Codecs.POSITIVE_INT.fieldOf("heat").forGetter(ParryParticleEffect::getHeat)).apply(instance, ParryParticleEffect::new);
    });
    public static final PacketCodec<RegistryByteBuf, ParryParticleEffect> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VECTOR_3F, (effect) -> {
        return ((ParryParticleEffect)effect).angle;
    }, PacketCodecs.INTEGER, ParryParticleEffect::getHeat, ParryParticleEffect::new);
    public int getHeat(){
        return heat;
    }
    public Vector3f getPosTo() {
        return angle;
    }

    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.angle.x());
        buf.writeFloat(this.angle.y());
        buf.writeFloat(this.angle.z());
        buf.writeInt(this.heat);
    }


    public ParticleType<ParryParticleEffect> getType() {
        return TYPES.get(this.heat);
    }

    public String asString() {
        return  String.format(Locale.ROOT, "%s %.2f %.2f %.2f,%.2i", Registries.PARTICLE_TYPE.getId(this.getType()), this.angle.x(), this.angle.y(), this.angle.z(),this.heat);
    }
}
