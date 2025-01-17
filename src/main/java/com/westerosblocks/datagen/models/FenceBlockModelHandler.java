package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelPart;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class FenceBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final BooleanProperty NORTH = Properties.NORTH;
    private static final BooleanProperty EAST = Properties.EAST;
    private static final BooleanProperty SOUTH = Properties.SOUTH;
    private static final BooleanProperty WEST = Properties.WEST;

    public FenceBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    // Modern way to create model ID using Registries
    private Identifier createModelId(Block block, String suffix, int setIdx, String stateId) {
        String blockPath = Registries.BLOCK.getId(block).getPath();
        String basePath = GENERATED_PATH + blockPath;
        String statePath = stateId != null ? "/" + stateId : "";
        String variantPath = suffix + "_v" + (setIdx + 1);

        return Identifier.of(
                WesterosBlocks.MOD_ID,
                basePath + statePath + "/" + variantPath
        );
    }

    public void generateBlockStateModels() {


    }


    private void generateInventoryModel() {
        WesterosBlockStateRecord sr0 = def.states.get(0);
        WesterosBlockDef.RandomTextureSet set = sr0.getRandomTextureSet(0);

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TEXTURE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + set.getTextureByIndex(0)));

        Identifier inventoryModelId = Models.CUSTOM_FENCE_INVENTORY.upload(
                block,
                "inventory",
                textureMap,
                generator.modelCollector
        );

        generator.registerParentedItemModel(block, inventoryModelId);
    }
}