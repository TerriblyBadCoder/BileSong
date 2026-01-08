package net.atired.silkysong.init;

import net.atired.silkysong.SilkySong;
import net.atired.silkysong.blocks.HemoglobinBlock;
import net.atired.silkysong.blocks.SilkBlock;
import net.atired.silkysong.blocks.TrashCubeBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class SKblockInit {
    public static final Block HEMOGLOBIN_BLOCK = register("hemoglobin_block", HemoglobinBlock::new,(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.RED).solid().noCollision()));
    public static final Block SILK = register("silk", SilkBlock::new,(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).mapColor(MapColor.WHITE).strength(0.1F).sounds(BlockSoundGroup.WOOL)));
    public static final Block WORMWOOD = register("wormwood", PillarBlock::new,(AbstractBlock.Settings.copy(Blocks.OAK_LOG).mapColor(MapColor.DARK_GREEN)));
    public static final Block TRASH_CUBE = register("trash_cube", TrashCubeBlock::new,(AbstractBlock.Settings.copy(Blocks.STONE).mapColor(MapColor.WHITE).strength(0.1F)));

    private static Block register(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Identifier identifier = SilkySong.id(path);
        RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        Block block = Blocks.register(registryKey, factory, settings);
        SKItems.register(path,(b)->{return new BlockItem(block,b);},new Item.Settings());
        return block;
    }
    public static void init(){
    }
}
