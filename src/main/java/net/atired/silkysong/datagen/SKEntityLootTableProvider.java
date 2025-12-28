package net.atired.silkysong.datagen;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.init.SKItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SKEntityLootTableProvider extends SimpleFabricLootTableProvider {



    public static final RegistryKey<LootTable> TEST_CHEST = RegistryKey.of(RegistryKeys.LOOT_TABLE, SilkySong.id("entities/bile_mosqo"));
    public static final RegistryKey<LootTable> TEST_CHEST2 = RegistryKey.of(RegistryKeys.LOOT_TABLE, SilkySong.id("entities/boy"));
    public static final RegistryKey<LootTable> TEST_CHEST3 = RegistryKey.of(RegistryKeys.LOOT_TABLE, SilkySong.id("entities/man"));
    public static final RegistryKey<LootTable> TEST_CHEST4 = RegistryKey.of(RegistryKeys.LOOT_TABLE, SilkySong.id("entities/jumpscarer"));
    public static final RegistryKey<LootTable> TEST_CHEST5 = RegistryKey.of(RegistryKeys.LOOT_TABLE, SilkySong.id("entities/hemogoblin"));
    public static final RegistryKey<LootTable> TEST_CHEST6 = RegistryKey.of(RegistryKeys.LOOT_TABLE, SilkySong.id("entities/hemoglobule"));


    public SKEntityLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(TEST_CHEST, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(2.0F))
                        .with(ItemEntry.builder(Items.SLIME_BALL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,2)))
                                ))
        );
        lootTableBiConsumer.accept(TEST_CHEST2, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(SKItems.LIVING_TWIG)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,3))))
                )
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(SKItems.SILK)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,3)))))

        );
        lootTableBiConsumer.accept(TEST_CHEST3, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.COPPER_INGOT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,4))))
                        .rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.ENDER_PEARL).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,1)))
                        ))
        );
        lootTableBiConsumer.accept(TEST_CHEST4, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(SKItems.TADPOLE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f,1f))))
                        )
        );
        lootTableBiConsumer.accept(TEST_CHEST5, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(SKItems.HEMOGLOBIN).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,3)))
                        ))
        );
        lootTableBiConsumer.accept(TEST_CHEST6, LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(SKItems.HEMOGLOBIN).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.5f,1.6f)))
                        ))
        );
    }
}
