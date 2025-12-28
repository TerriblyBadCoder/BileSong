package net.atired.silkysong.networking.payloads;


import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.entities.TrashCubeEntity;
import net.atired.silkysong.networking.SKconstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public record SilkenCloakPayload(int entityID, double xVel, double yVel, double zVel) implements CustomPayload {
    public static final Id<SilkenCloakPayload> ID = new Id<>(SKconstants.SILKEN_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SilkenCloakPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SilkenCloakPayload::entityID,PacketCodecs.DOUBLE, SilkenCloakPayload::xVel,PacketCodecs.DOUBLE, SilkenCloakPayload::yVel,PacketCodecs.DOUBLE, SilkenCloakPayload::zVel, SilkenCloakPayload::new);
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SilkenCloakPayload> {
        @Override
        public void receive(SilkenCloakPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.client().world.getEntityById(payload.entityID());
            if(entity instanceof LivingEntity player){
                player.playSound(SoundEvents.BLOCK_COBWEB_BREAK,2.0f,1.0f);
                player.playSound(SoundEvents.BLOCK_COBWEB_PLACE,2.0f,1.0f);
                if(player instanceof HostileEntityStringBoostAccessor accessor){
                    accessor.setStringBoost(1.0f);
                    accessor.setStringBoost2(0.6f);
                }
            }


        }

        public static Vec3d rotateVectorCC(Vec3d vec, Vec3d axis, double theta) {
            double x, y, z;
            double u, v, w;
            x = vec.getX();
            y = vec.getY();
            z = vec.getZ();
            u = axis.getX();
            v = axis.getY();
            w = axis.getZ();
            double xPrime = u * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                    + x * Math.cos(theta)
                    + (-w * y + v * z) * Math.sin(theta);
            double yPrime = v * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                    + y * Math.cos(theta)
                    + (w * x - u * z) * Math.sin(theta);
            double zPrime = w * (u * x + v * y + w * z) * (1d - Math.cos(theta))
                    + z * Math.cos(theta)
                    + (-v * x + u * y) * Math.sin(theta);
            return new Vec3d(xPrime, yPrime, zPrime);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
