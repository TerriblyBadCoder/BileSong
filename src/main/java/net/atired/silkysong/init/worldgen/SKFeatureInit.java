package net.atired.silkysong.init.worldgen;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.worldgen.WormwoodTreeFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class SKFeatureInit {
    public static final Feature<DefaultFeatureConfig> WORMWOOD_TREES = register("wormwood_trees", new WormwoodTreeFeature(DefaultFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return (F) Registry.register(Registries.FEATURE, SilkySong.id(name), feature);
    }
    public static void init(){

    }
}
