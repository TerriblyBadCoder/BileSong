package net.atired.silkysong.init.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class SKBiomeModifInit {
    public static void load() {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SWAMP,BiomeKeys.MANGROVE_SWAMP),
                GenerationStep.Feature.FLUID_SPRINGS,
                PlacedFeatureInit.WORMWOOD_TREES_KEY
        );
    }
}
