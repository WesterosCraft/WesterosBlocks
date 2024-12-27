package com.westerosblocks;

import com.westerosblocks.block.*;
import com.westerosblocks.item.WesterosBlocksItems;
import com.westerosblocks.item.WesterosItemMenuOverrides;
import com.westerosblocks.sound.ModSounds;
import com.westerosblocks.util.AutoDoorRestore;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.westerosblocks.WesterosBlocksJsonLoader.sanityCheck;

public class WesterosBlocks implements ModInitializer {
    public static final String MOD_ID = "westerosblocks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    List<String> configFiles = List.of(
            "/definitions/color_maps.json",
            "/definitions/block_tags.json",
            "/definitions/menu_overrides.json",
            "/definitions/block_sets.json",
            "/definitions/test_blocks.json"
//            "/definitions/blocks.json"
    );

    public static WesterosBlocksJsonLoader.WesterosBlocksConfig customConfig;
    private static WesterosBlockDef[] customBlockDefs;

    public static HashMap<String, Block> customBlocksByName;

    public static Path modConfigPath;
    public static WesterosBlockColorMap[] colorMaps;
    public static WesterosItemMenuOverrides[] menuOverrides;

    @Override
    public void onInitialize() {
        WesterosBlockDef.initialize();
        customConfig = WesterosBlocksJsonLoader.getBlockConfig(configFiles);

        customBlockDefs = getBlockDefs(customConfig);
        LOGGER.info("Loaded {} block definitions", customBlockDefs.length);

        if (!sanityCheck(customBlockDefs)) {
            LOGGER.error("WesterosBlocks.json failed sanity check");
            return;
        }
        WesterosCreativeModeTabs.registerCreativeModeTabs();
        WesterosBlocksItems.registerModItems();
        ModBlocks.registerModBlocks(customBlockDefs);

        ColorHandlers.registerColorProviders();
        ModSounds.registerSounds(customBlockDefs);


        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // Handle any pending door restores (force immediate)
            AutoDoorRestore.handlePendingHalfDoorRestores(true);
        });
    }

    public static void crash(Exception x, String msg) {
        throw new CrashException(new CrashReport(msg, x));
    }

    public static void crash(String msg) {
        crash(new Exception(), msg);
    }

    public static WesterosBlockDef[] getCustomBlockDefs() {
        return customBlockDefs;
    }

    // Expand block set definitions to obtain the full block definition list
    public static WesterosBlockDef[] getBlockDefs(WesterosBlocksJsonLoader.WesterosBlocksConfig config) {
        WesterosBlockSetDef[] blockSetDefs = config.blockSets;
        WesterosBlockDef[] blockDefs = config.blocks;
        List<WesterosBlockDef> expandedBlockDefs = new LinkedList<WesterosBlockDef>(Arrays.asList(blockDefs));

        if (config.blockSets.length > 0) {
            for (WesterosBlockSetDef blockSetDef : blockSetDefs) {
                if (blockSetDef == null)
                    continue;
                List<WesterosBlockDef> variantBlockDefs = blockSetDef.generateBlockDefs();
                expandedBlockDefs.addAll(variantBlockDefs);
            }
        }

        return expandedBlockDefs.toArray(new WesterosBlockDef[expandedBlockDefs.size()]);
    }

    public static Block findBlockByName(String blkname, String namespace) {
        Block blk = customBlocksByName.get(blkname);
        if (blk != null) return blk;

        Identifier id;
        try {
            id = Identifier.tryParse(blkname);
        } catch (InvalidIdentifierException e) {
            if (namespace != null) {
                try {
                    id = Identifier.of(namespace, blkname);
                } catch (InvalidIdentifierException e2) {
                    return null;
                }
            } else {
                return null;
            }
        }

        if (id.getNamespace().equals(namespace)) {
            blk = customBlocksByName.get(id.getPath());
            if (blk != null) return blk;
        }

        return Registries.BLOCK.get(id);
    }

}