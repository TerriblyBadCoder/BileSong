package net.atired.silkysong.networking.payloads;


import net.atired.silkysong.entities.TrashCubeEntity;
import net.atired.silkysong.networking.SKconstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record VelSyncPayload(int entityID, double xVel, double yVel, double zVel) implements CustomPayload {
    public static final Id<VelSyncPayload> ID = new Id<>(SKconstants.VELSYNC_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, VelSyncPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, VelSyncPayload::entityID,PacketCodecs.DOUBLE, VelSyncPayload::xVel,PacketCodecs.DOUBLE, VelSyncPayload::yVel,PacketCodecs.DOUBLE, VelSyncPayload::zVel, VelSyncPayload::new);
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<VelSyncPayload> {
        @Override
        public void receive(VelSyncPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.client().world.getEntityById(payload.entityID());
            if(entity instanceof TrashCubeEntity trashCubeEntity)
                trashCubeEntity.setClientVelocity(new Vec3d(payload.xVel,payload.yVel,payload.zVel));
            else if(entity!=null){
                entity.setVelocity(new Vec3d(payload.xVel,payload.yVel,payload.zVel));
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
