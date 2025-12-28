package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class SKcomponents {
    public static final ComponentType<Integer> PARRY_TIME = register("parryamount", (builder) -> {
        return builder.codec(Codecs.rangedInt(0, 200)).packetCodec(PacketCodecs.VAR_INT);
    });
    public static final ComponentType<Float> MIRROR_TYPE = register("mirrortype", (builder) -> {
        return builder.codec(Codecs.rangedInclusiveFloat(0f, 200f)).packetCodec(PacketCodecs.FLOAT);
    });
    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return (ComponentType) Registry.register(Registries.DATA_COMPONENT_TYPE, SilkySong.id(id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
    public static void init(){

    }
}
