package com.westerosblocks;

import com.westerosblocks.block.WesterosBlockColorMap;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockSetDef;
import com.westerosblocks.block.WesterosBlocksBlocks;
import com.westerosblocks.item.WesterosBlocksItems;
import com.westerosblocks.item.WesterosItemMenuOverrides;
import com.westerosblocks.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WesterosBlocks implements ModInitializer {
    public static final String MOD_ID = "westerosblocks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    List<String> configFiles = List.of(
            "/definitions/color_maps.json",
            "/definitions/block_tags.json",
            "/definitions/menu_overrides.json",
            "/definitions/block_sets.json",
            "/definitions/blocks.json");

    public static WesterosBlocksJsonLoader.WesterosBlocksConfig customConfig;
    private static WesterosBlockDef[] customBlockDefs;

    public static HashMap<String, Block> customBlocksByName;
    public static Block[] customBlocks = new Block[0];
    public static Path modConfigPath;
    public static WesterosBlockColorMap[] colorMaps;
    public static WesterosItemMenuOverrides[] menuOverrides;

    @Override
    public void onInitialize() {
        customConfig = WesterosBlocksJsonLoader.getBlockConfig(configFiles);

        customBlockDefs = getBlockDefs(customConfig);
        LOGGER.info("Loaded " + customBlockDefs.length + " block definitions");

        WesterosBlocksItems.registerModItems();
        WesterosBlocksBlocks.registerModBlocks();
        ModSounds.registerSounds(customBlockDefs);
    }

    public static WesterosBlockDef[] getCustomBlockDefs() {
        return customBlockDefs;
    }

    // Expand block set definitions to obtain the full block definition list
    public static WesterosBlockDef[] getBlockDefs(WesterosBlocksJsonLoader.WesterosBlocksConfig config) {
        WesterosBlockSetDef[] blockSetDefs = config.blockSets;
        WesterosBlockDef[] blockDefs = config.blocks;
        List<WesterosBlockDef> expandedBlockDefs = new LinkedList<WesterosBlockDef>(Arrays.asList(blockDefs));
        for (WesterosBlockSetDef blockSetDef : blockSetDefs) {
            if (blockSetDef == null)
                continue;
            List<WesterosBlockDef> variantBlockDefs = blockSetDef.generateBlockDefs();
            expandedBlockDefs.addAll(variantBlockDefs);
        }
        return expandedBlockDefs.toArray(new WesterosBlockDef[expandedBlockDefs.size()]);
    }
}