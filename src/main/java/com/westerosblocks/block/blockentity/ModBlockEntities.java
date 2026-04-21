package com.westerosblocks.block.blockentity;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.blockentity.custom.WCBigDoorBlockEntity;
import com.westerosblocks.block.blockentity.custom.WCFurnaceBlockEntity;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.HashMap;

public class ModBlockEntities {
    public static HashMap<String, BlockEntityType<?>> customEntitiesByName = new HashMap<>();

    static {
        registerFurnaceBlockEntities();
        registerBigDoorBlockEntities();
    }

    private static void registerFurnaceBlockEntities() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry not initialized - skipping block entity registration");
            return;
        }

        // Get all furnace block definitions directly by type
        for (BlockDefinition definition : registry.getByType("furnace")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                BlockEntityType<?> blockEntityType = register(definition.getBlockName(),
                        BlockEntityType.Builder.create(
                                (pos, state) -> new WCFurnaceBlockEntity(pos, state, definition.getBlockName()),
                                block
                        ).build(null)
                );
                customEntitiesByName.put(definition.getBlockName(), blockEntityType);
            }
        }
    }

    private static void registerBigDoorBlockEntities() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry not initialized - skipping big door block entity registration");
            return;
        }

        for (BlockDefinition definition : registry.getByType("bigdoor")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                BlockEntityType<?> blockEntityType = register(definition.getBlockName(),
                        BlockEntityType.Builder.create(
                                (pos, state) -> new WCBigDoorBlockEntity(pos, state, definition.getBlockName()),
                                block
                        ).build(null)
                );
                customEntitiesByName.put(definition.getBlockName(), blockEntityType);
            }
        }
    }

    public static BlockEntityType<?> getBlockEntityType(String name) {
        return customEntitiesByName.get(name);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, WesterosBlocks.id(name), type);
    }

    public static void registerModBlockEntities() {
        WesterosBlocks.LOGGER.info("Registering Mod Entities for " + WesterosBlocks.MOD_ID);
    }
}
