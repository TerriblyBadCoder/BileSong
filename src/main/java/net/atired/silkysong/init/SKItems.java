package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.item.*;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.block.jukebox.JukeboxSongs;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SKItems {
    private static RegistryKey<JukeboxSong> of(String id) {
        return RegistryKey.of(RegistryKeys.JUKEBOX_SONG, SilkySong.id(id));
    }
    public static final RegistryKey<JukeboxSong> MAN = of("man");
    public static final Item BLOOD_ARROW = register("blood_arrow", BloodArrowItem::new,new Item.Settings().maxCount(64));
    public static final Item DECOAGULATOR = register("decoagulator", DecoagulatorItem::new,new Item.Settings().maxCount(16));

    public static final Item SLIMECANNONBALL = register("slime_cannon_ball", SlimeCannonBallItem::new,new Item.Settings().maxCount(16));
    public static final Item AMADUNNO = register("amadunno_sword", AmadunnoSwordItem::new,new Item.Settings().component(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.builder().add(EntityAttributes.ENTITY_INTERACTION_RANGE,new EntityAttributeModifier(SilkySong.id("range"),0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.MAINHAND).build())
            .sword(ToolMaterial.GOLD,3.5F, -2.3F).component(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE).component(SKcomponents.PARRY_TIME,50));
    public static final Item TWISTED_MIRROR = register("twisted_mirror", TwistedMirrorItem::new,new Item.Settings().component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).maxCount(1));
    public static final Item LIVING_TWIG = register("living_twig", Item::new,new Item.Settings());
    public static final Item SILK = register("silk_thread", Item::new,new Item.Settings());
    public static final Item HEMOGLOBIN = register("hemoglobin", Item::new,new Item.Settings());
    public static final Item SILKEN_MANTLE = register("silken_mantle", Item::new,new Item.Settings().armor(SKmaterialInit.SILK, EquipmentType.CHESTPLATE));

    public static final Item HUNTER_EGG = register("hunter_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.HUNTER,item);},new Item.Settings());
    public static final Item BOY_EGG = register("boy_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.BOY,item);},new Item.Settings());
    public static final Item MAN_EGG = register("man_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.MAN,item);},new Item.Settings());
    public static final Item MYTH_EGG = register("myth_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.JUMPSCARER,item);},new Item.Settings());
    public static final Item BILE_MOSQO_EGG = register("bile_mosqo_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.BILEMOSQO,item);},new Item.Settings());
    public static final Item MISSILETOAD_EGG = register("missiletoad_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.MISSILETOAD,item);},new Item.Settings());
    public static final Item HEMOGOBLIN_EGG = register("hemogoblin_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.HEMOGOBLIN,item);},new Item.Settings());
    public static final Item HEMOGLOBULE_EGG = register("hemoglobule_spawn_egg", (item)->{return new SpawnEggItem(SKentityTypes.HEMOGLOBULE,item);},new Item.Settings());
    public static final Item TADPOLE = register("tadpole", CurledChildItem::new,new Item.Settings());
    public static final Item BOUNCING_POTION = register("bouncing_potion", BouncingPotionItem::new,new Item.Settings().maxCount(1).component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).component(DataComponentTypes.POTION_DURATION_SCALE, 0.25F));
    public static final Item MUSIC_DISC_MAN = register(
            "music_disc_man", Item::new, new Item.Settings().maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(MAN)
    );
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, SilkySong.id(name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void init(){
    }


}
