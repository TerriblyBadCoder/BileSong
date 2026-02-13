package net.atired.silkysong;

import net.atired.silkysong.datagen.SKEntityGen;
import net.atired.silkysong.init.*;
import net.atired.silkysong.init.worldgen.SKBiomeModifInit;
import net.atired.silkysong.init.worldgen.SKFeatureInit;
import net.atired.silkysong.networking.payloads.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

public class SilkySong implements ModInitializer {
    public static String MODID = "silkysong";
    @Override
    public void onInitialize() {
        SKsoundEventInit.init();
        SKentityTypes.init();
        SKItems.init();
        SKblockInit.init();
        SKparticlesInit.registerParticles();
        SKenchantmentsInit.init();
        SKstatusEffectInit.init();
        SKFeatureInit.init();
        SKEntityGen.addSpawns();
        SKBiomeModifInit.load();
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerItemRecipe(Items.POTION,SKItems.HEMOGLOBIN,SKItems.BOUNCING_POTION);
        });
        SKitemGroupInit.init();
        itemGroups();
        PayloadTypeRegistry.playS2C().register(ParryPayload.ID, ParryPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MissiletoadPayload.ID, MissiletoadPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SigilPayload.ID, SigilPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CubePayload.ID, CubePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(VelSyncPayload.ID, VelSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SilkenCloakPayload.ID, SilkenCloakPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HoisterPayload.ID, HoisterPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HemoRagePayload.ID, HemoRagePayload.CODEC);
    }
    private static void itemGroups(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(itemGroup -> {
            itemGroup.add(SKItems.HEMOGLOBULE_EGG);
            itemGroup.add(SKItems.HEMOGOBLIN_EGG);
            itemGroup.add(SKItems.MAN_EGG);
            itemGroup.add(SKItems.BOY_EGG);
            itemGroup.add(SKItems.HUNTER_EGG);
            itemGroup.add(SKItems.BILE_MOSQO_EGG);
            itemGroup.add(SKItems.MISSILETOAD_EGG);
            itemGroup.add(SKItems.MYTH_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
            itemGroup.add(SKItems.TWISTED_MIRROR);
            itemGroup.add(SKItems.TADPOLE);
            itemGroup.add(SKItems.MUSIC_DISC_MAN);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(itemGroup -> {
            itemGroup.add(SKblockInit.TRASH_CUBE);
            itemGroup.add(SKblockInit.HEMOGLOBIN_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> {
            itemGroup.add(SKblockInit.SILK);
            itemGroup.add(SKblockInit.WORMWOOD);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(itemGroup -> {
            itemGroup.add(SKItems.LIVING_TWIG);
            itemGroup.add(SKItems.HEMOGLOBIN);
            itemGroup.add(SKItems.SILK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(itemGroup->{
            ItemGroup.DisplayContext displayContext = itemGroup.getContext();
                displayContext.lookup().getOptional(RegistryKeys.POTION).ifPresent((registryWrapper) -> {
                    addPotions(itemGroup, registryWrapper, SKItems.BOUNCING_POTION, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, displayContext.enabledFeatures());
                });
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(itemGroup -> {
            itemGroup.add(SKItems.DECOAGULATOR);
            itemGroup.add(SKItems.SLIMECANNONBALL);
            itemGroup.add(SKItems.AMADUNNO);
            itemGroup.add(SKItems.TWISTED_MIRROR);
            itemGroup.add(SKItems.BLOOD_ARROW);
            itemGroup.add(SKItems.SILKEN_MANTLE);
        });
    }
    private static void addPotions(ItemGroup.Entries entries, RegistryWrapper<Potion> registryWrapper, Item item, ItemGroup.StackVisibility visibility, FeatureSet enabledFeatures) {
        registryWrapper.streamEntries().filter((potionEntry) -> {
            return ((Potion)potionEntry.value()).isEnabled(enabledFeatures);
        }).map((entry) -> {
            return PotionContentsComponent.createStack(item, entry);
        }).forEach((stack) -> {
            entries.add(stack, visibility);
        });
    }

    public static Identifier id(String path){
        return Identifier.of(MODID,path);
    }
}
