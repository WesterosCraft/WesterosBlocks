package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArrowSlitBlockExport extends ModelExport {
    public ArrowSlitBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
    }

    @Override
    public void generateBlockStateModels() {
        if (def == null || def.states.isEmpty()) {
            return;
        }

        BlockStateBuilder builder = new BlockStateBuilder(block);
        Map<String, List<BlockStateVariant>> variants = builder.getVariants();

        // Process each state (single, mid, bottom, top)
        for (ModBlockStateRecord state : def.states) {
            String stateID = state.stateID != null ? state.stateID : "single";
            boolean isCustom = state.isCustomModel();

            // Get the base model path
            String modelPath = String.format("%stest_arrow_slit/test_arrow_slit_%s",
                    isCustom ? CUSTOM_PATH : GENERATED_PATH,
                    stateID);

            // Create variants for each facing direction
            for (int rotation = 0; rotation < 360; rotation += 90) {
                BlockStateVariant variant = VariantBuilder.createWithRotation(
                        WesterosBlocks.id(modelPath),
                        state.getRandomTextureSet(0),
                        rotation
                );

                // Add variant with both facing and type conditions
                String condition = String.format("facing=%s,type=%s", 
                    getFacingDirection(rotation), 
                    stateID);
                builder.addVariant(condition, variant, null, variants);
            }
        }

        // Generate the block state file
        generateBlockStateFiles(generator, block, variants);
    }

    private String getFacingDirection(int rotation) {
        return switch (rotation) {
            case 0 -> "north";
            case 90 -> "east";
            case 180 -> "south";
            case 270 -> "west";
            default -> "north";
        };
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, ModBlock blockDefinition) {
        // For arrow slits, we'll use the single model as the item model
        String path = String.format("%stest_arrow_slit/test_arrow_slit_single", CUSTOM_PATH);
        itemModelGenerator.register(block.asItem(), new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty()));
    }
} 