package net.atired.silkysong.enchants;

import net.atired.silkysong.init.SKenchantmentsInit;
import net.atired.silkysong.init.SKtagInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class SilkySongEnchantsGenerator extends FabricDynamicRegistryProvider {
    public SilkySongEnchantsGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
        RegistryWrapper<Enchantment> enchantLookup = registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        register(entries, SKenchantmentsInit.HEARTSTRING_ENCHANT_KEY, Enchantment.builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(SKtagInit.AMADUNNO_TAG),
                                6, // probability of showing up in the enchantment table
                                1, // max level
                                Enchantment.constantCost(16), // cost per level (base)
                                Enchantment.constantCost(20), // cost per level (max)
                                4, // anvil applying cost
                                AttributeModifierSlot.HAND
                        ))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new HeartstringEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5f, 0.15f))));
    }

    private static void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
        entries.add(key, builder.build(key.getValue()), resourceConditions);
    }

    @Override
    public String getName() {
        return "Enchantment Generator";
    }
}
