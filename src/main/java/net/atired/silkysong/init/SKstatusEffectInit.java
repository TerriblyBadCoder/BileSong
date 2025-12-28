package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.statuseffects.HemoRageEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class SKstatusEffectInit {
    public static final RegistryEntry<StatusEffect> HEMORAGE_EFFECT;
    public static void init(){

    }
    static {
        HEMORAGE_EFFECT = Registry.registerReference(Registries.STATUS_EFFECT, SilkySong.id("hemorage"), new HemoRageEffect());
    }
}
