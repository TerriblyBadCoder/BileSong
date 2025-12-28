package net.atired.silkysong.init;

import com.mojang.serialization.MapCodec;
import net.atired.silkysong.SilkySong;
import net.atired.silkysong.enchants.HeartstringEnchantmentEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class SKenchantmentsInit {
    public static final RegistryKey<Enchantment> HEARTSTRING_ENCHANT_KEY = RegistryKey.of(RegistryKeys.ENCHANTMENT, SilkySong.id("heartstrings"));
    public static final MapCodec<HeartstringEnchantmentEffect> HEARTSTRING_EFFECT = register("heartstrings",HeartstringEnchantmentEffect.CODEC);
   private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String name, MapCodec<T> codec)
    {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, SilkySong.id(name),codec);
    }
    public static void init(){

    }
}
