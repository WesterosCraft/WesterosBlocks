package com.westerosblocks;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlocksBlocks;
import com.westerosblocks.item.WesterosBlocksItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WesterosBlocks implements ModInitializer {
	public static final String MOD_ID = "westerosblocks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	List<String> configFiles = List.of("/blockSets.json", "/blocks.json");

	public static WesterosBlocksConfigLoader.WesterosBlocksConfig customConfig;
	private static WesterosBlockDef[] customBlockDefs;

	@Override
	public void onInitialize() {
		customConfig = WesterosBlocksConfigLoader.getBlockConfig(configFiles);

		WesterosBlocksItems.registerModItems();
		WesterosBlocksBlocks.registerModBlocks();

	}
}