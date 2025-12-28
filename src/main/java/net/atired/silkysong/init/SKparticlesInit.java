package net.atired.silkysong.init;

import com.mojang.serialization.MapCodec;
import net.atired.silkysong.SilkySong;
import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SKparticlesInit {

    public static final SimpleParticleType BLOOD_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType JUMP_GLOB_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType STARRY_PARTICLE = FabricParticleTypes.simple();
    public static final ParticleType<ParryParticleEffect> MIRROR_PARTICLE = new ParticleType<>(true) {
        @Override
        public MapCodec<ParryParticleEffect> getCodec() {
            return ParryParticleEffect.CODEC;
        }

        @Override
        public PacketCodec<? super RegistryByteBuf, ParryParticleEffect> getPacketCodec() {
            return ParryParticleEffect.PACKET_CODEC;
        }
    };
    public static final SimpleParticleType SILK_PARTICLE = FabricParticleTypes.simple();
    public static final ParticleType<ParryParticleEffect> FROGSPEW_PARTICLE = new ParticleType<>(true) {
        @Override
        public MapCodec<ParryParticleEffect> getCodec() {
            return ParryParticleEffect.CODEC;
        }

        @Override
        public PacketCodec<? super RegistryByteBuf, ParryParticleEffect> getPacketCodec() {
            return ParryParticleEffect.PACKET_CODEC;
        }
    };
    public static final ParticleType<ParryParticleEffect> PARRY_PARTICLE = new ParticleType<>(true) {
        @Override
        public MapCodec<ParryParticleEffect> getCodec() {
            return ParryParticleEffect.CODEC;
        }

        @Override
        public PacketCodec<? super RegistryByteBuf, ParryParticleEffect> getPacketCodec() {
            return ParryParticleEffect.PACKET_CODEC;
        }
    };
    public static final ParticleType<ParryParticleEffect> SIGIL_PARTICLE = new ParticleType<>(true) {
        @Override
        public MapCodec<ParryParticleEffect> getCodec() {
            return ParryParticleEffect.CODEC;
        }

        @Override
        public PacketCodec<? super RegistryByteBuf, ParryParticleEffect> getPacketCodec() {
            return ParryParticleEffect.PACKET_CODEC;
        }
    };
    public static void  registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("parry"), PARRY_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("frogpar"), FROGSPEW_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("parry_sigil"), SIGIL_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("silk"), SILK_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("stars"), STARRY_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("mirror"), MIRROR_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("blood"), BLOOD_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, SilkySong.id("jumpscarer_glob"), JUMP_GLOB_PARTICLE);
    }
}
