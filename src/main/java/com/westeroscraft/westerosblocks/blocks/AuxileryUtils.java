package com.westeroscraft.westerosblocks.blocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
public class AuxileryUtils {
    public static BlockSetType getBlockSetType(BlockBehaviour.Properties props, WesterosBlockDef def) {
        //TODO: implment
        return BlockSetType.OAK;
    }
    public static WoodType getWoodType(BlockBehaviour.Properties props, WesterosBlockDef def) {
        //TODO: implment
        return WoodType.OAK;
    }
    public static final Map<CreativeModeTab, LinkedHashSet<BlockItem>> BLOCK_ITEM_TABS = new HashMap<>();
    public static void registerCreativeTab(BlockItem itemBlock, CreativeModeTab creativeTab) {
        BLOCK_ITEM_TABS.computeIfAbsent(creativeTab, (tab) -> new LinkedHashSet<>()).add(itemBlock);
    }
}