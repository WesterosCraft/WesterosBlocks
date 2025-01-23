package com.westerosblocks.entity;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.entity.custom.WCFurnaceBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {
    public static HashMap<String, BlockEntityType<?>> customEntitiesByName = new HashMap<>();

    static {
        HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
        WesterosBlockDef[] customBlockDefs = WesterosBlocks.getCustomBlockDefs();

        Map<String, Block> furnaceBlocks = new HashMap<>();

        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            // if we get another block entity custom block in the future besides furnace we can turn this into a switch statement or somethin
            if (customBlockDef == null || !customBlockDef.blockType.equals("furnace")) continue;

            Block currentBlock = customBlocks.get(customBlockDef.getBlockName());
            if (currentBlock != null) {
                furnaceBlocks.put(customBlockDef.getBlockName(), currentBlock);
            }
        }

        // register the block entities
        for (Map.Entry<String, Block> entry : furnaceBlocks.entrySet()) {
            BlockEntityType<?> blockEntityType = register(entry.getKey(),
                    BlockEntityType.Builder.create(
                            (pos, state) -> new WCFurnaceBlockEntity(pos, state, entry.getKey()),
                            entry.getValue()
                    ).build(null)
            );
            customEntitiesByName.put(entry.getKey(), blockEntityType);
        }
    }

    public static BlockEntityType<?> getBlockEntityType(String name) {
        return customEntitiesByName.get(name);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, WesterosBlocks.id(name), type);
    }

    public static void registerModEntities() {
        WesterosBlocks.LOGGER.info("Registering Mod Entities for " + WesterosBlocks.MOD_ID);
    }
}
