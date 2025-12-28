package net.atired.silkysong;

import net.atired.silkysong.datagen.SKEntityLootTableProvider;
import net.atired.silkysong.datagen.SKWorldGeneration;
import net.atired.silkysong.enchants.SilkySongEnchantsGenerator;
import net.atired.silkysong.init.SKItems;
import net.atired.silkysong.init.worldgen.ConfiguredFeatureInit;
import net.atired.silkysong.init.worldgen.PlacedFeatureInit;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class SilkySongDatagen  implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SilkySongEnchantsGenerator::new);
        pack.addProvider(SKEntityLootTableProvider::new);
        pack.addProvider(SKWorldGeneration::new);
    }
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatureInit::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatureInit::bootstrap);
    }
}
