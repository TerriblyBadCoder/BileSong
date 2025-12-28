package net.atired.silkysong.networking.payloads;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.entities.BileMosqoEntity;
import net.atired.silkysong.entities.MissileToadEntity;
import net.atired.silkysong.networking.SKconstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public record MissiletoadPayload(int entityID, int targetID,boolean reset) implements CustomPayload {
    public static final Id<MissiletoadPayload> ID = new Id<>(SKconstants.MISSILETOAD_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, MissiletoadPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER,MissiletoadPayload::entityID,PacketCodecs.INTEGER, MissiletoadPayload::targetID,PacketCodecs.BOOLEAN,MissiletoadPayload::reset, MissiletoadPayload::new);
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<MissiletoadPayload> {
        @Override
        public void receive(MissiletoadPayload payload, ClientPlayNetworking.Context context) {
            MissileToadEntity missileToad = (MissileToadEntity)context.client().world.getEntityById(payload.entityID);
            if(missileToad==null){
                return;
            }
            if(payload.reset){
               missileToad.resetTarget();
            }
            else{
               BileMosqoEntity bileMosqo = (BileMosqoEntity)context.client().world.getEntityById(payload.targetID);
               missileToad.setTarget(bileMosqo);
               missileToad.targetPos = missileToad.getPos();
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
