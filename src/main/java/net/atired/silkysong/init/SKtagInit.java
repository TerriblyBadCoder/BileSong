package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SKtagInit {
    public static final TagKey<Enchantment> HEARTSTRINGS_TAG = TagKey.of(RegistryKeys.ENCHANTMENT, SilkySong.id("heartstrings"));
    public static final TagKey<Item> AMADUNNO_TAG = TagKey.of(RegistryKeys.ITEM, SilkySong.id("amadunno_sword"));

}
