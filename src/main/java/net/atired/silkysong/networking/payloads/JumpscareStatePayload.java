package net.atired.silkysong.networking.payloads;

import net.atired.silkysong.entities.BileMosqoEntity;
import net.atired.silkysong.entities.MissileToadEntity;
import net.atired.silkysong.networking.SKconstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record JumpscareStatePayload(int entityID,int itemID) implements CustomPayload {
    public static final Id<JumpscareStatePayload> ID = new Id<>(SKconstants.JUMPSCARESTATE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, JumpscareStatePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, JumpscareStatePayload::entityID,PacketCodecs.INTEGER,JumpscareStatePayload::itemID, JumpscareStatePayload::new);
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<JumpscareStatePayload> {
        @Override
        public void receive(JumpscareStatePayload payload, ClientPlayNetworking.Context context) {

        }


    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
