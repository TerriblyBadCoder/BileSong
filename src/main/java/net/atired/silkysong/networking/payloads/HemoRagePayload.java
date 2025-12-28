package net.atired.silkysong.networking.payloads;

import net.atired.silkysong.accessors.HostileEntityStringBoostAccessor;
import net.atired.silkysong.accessors.LivingRenderStateStringBoostAccessor;
import net.atired.silkysong.networking.SKconstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record HemoRagePayload(int entityID, float hemorage) implements CustomPayload {
    public static final Id<HemoRagePayload> ID = new Id<>(SKconstants.HEMORAGE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, HemoRagePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, HemoRagePayload::entityID,PacketCodecs.FLOAT, HemoRagePayload::hemorage, HemoRagePayload::new);
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<HemoRagePayload> {
        @Override
        public void receive(HemoRagePayload payload, ClientPlayNetworking.Context context) {
            if(context.player().getWorld().getEntityById(payload.entityID)!=null&&context.player().getWorld().getEntityById(payload.entityID) instanceof LivingEntity living && living instanceof HostileEntityStringBoostAccessor accessor){
                accessor.setHemorrhage(payload.hemorage);
            }
        }


    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
