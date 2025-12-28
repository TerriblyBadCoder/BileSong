package net.atired.silkysong.networking.payloads;

import net.atired.silkysong.client.SilkySongClient;
import net.atired.silkysong.networking.SKconstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public record ParryPayload(int entityID, double power, Vector3f vec3d, Vector3f velocity) implements CustomPayload {
    public static final Id<ParryPayload> ID = new Id<>(SKconstants.PARRY_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ParryPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, ParryPayload::entityID,PacketCodecs.DOUBLE, ParryPayload::power, PacketCodecs.VECTOR_3F, ParryPayload::vec3d, PacketCodecs.VECTOR_3F, ParryPayload::velocity, ParryPayload::new);
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<ParryPayload> {
        @Override
        public void receive(ParryPayload payload, ClientPlayNetworking.Context context) {
            if(context.player()==context.player().getWorld().getEntityById(payload.entityID)){
                context.player().addVelocity(new Vec3d(payload.velocity));
                context.player().playSoundToPlayer(SoundEvents.ITEM_SHIELD_BLOCK.value(), SoundCategory.PLAYERS,1.0f,1.6f);
                if(payload.power==0){
                    context.player().playSoundToPlayer(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.PLAYERS,0.7f,1.2f);
                    SilkySongClient.PROXY.vingette = 1.0f;
                    SilkySongClient.PROXY.colours = SilkySongClient.PROXY.colours.mul(0.0f);
                    ItemStack stack = context.player().getActiveItem();
                    context.player().stopUsingItem();
                    context.player().getItemCooldownManager().set(stack,0);
                    context.player().setCurrentHand(Hand.MAIN_HAND);
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
