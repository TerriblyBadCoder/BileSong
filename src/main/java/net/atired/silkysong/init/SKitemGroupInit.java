package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class SKitemGroupInit {
    public static final ItemGroup ALL_SK_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            SilkySong.id("executive_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(SKItems.LIVING_TWIG))
                    .displayName(Text.translatable("itemgroup.silkysong.silkysong_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(SKItems.LIVING_TWIG);
                        entries.add(SKItems.TWISTED_MIRROR);
                        entries.add(SKItems.AMADUNNO);
                        entries.add(SKblockInit.WORMWOOD);
                        entries.add(SKItems.SILK);
                        entries.add(SKblockInit.SILK);
                        entries.add(SKItems.SILKEN_MANTLE);
                        entries.add(SKItems.HEMOGLOBIN);
                        entries.add(SKItems.BLOOD_ARROW);
                        entries.add(SKblockInit.HEMOGLOBIN_BLOCK);
                        entries.add(SKItems.DECOAGULATOR);
                        entries.add(SKItems.SLIMECANNONBALL);
                        entries.add(SKItems.TADPOLE);
                        entries.add(SKItems.MUSIC_DISC_MAN);
                        entries.add(SKItems.MYTH_EGG);
                        entries.add(SKItems.MISSILETOAD_EGG);
                        entries.add(SKItems.BILE_MOSQO_EGG);
                        entries.add(SKItems.BOY_EGG);
                        entries.add(SKItems.MAN_EGG);
                        entries.add(SKItems.HEMOGOBLIN_EGG);
                        entries.add(SKItems.HEMOGLOBULE_EGG);
                        entries.add(SKItems.HUNTER_EGG);
                    }).build());
    public static void init(){

    }
}
