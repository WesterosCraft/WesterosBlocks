package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.util.Identifier;

import java.util.*;

public class Cuboid16WayBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static final String modRot[] = {"", "_rotn22", "_rotn45", "_rot22"};

    public Cuboid16WayBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public String modelFileName(String ext, int setidx, boolean isCustom) {
        if (isCustom)
            return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx);
        else
            return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
    }

    public void generateBlockStateModels() {
        final Map<String, List<BlockStateVariant>> variants = new HashMap<>();

        for (WesterosBlockStateRecord stateRecord : def.states) {
            boolean justBase = stateRecord.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(stateRecord.stateID);
            String fname = justBase ? "base" : stateRecord.stateID;
            boolean isTinted = stateRecord.isTinted();

            // For each direction
            for (int rotation = 0; rotation < 16; rotation++) {

                for (int setIdx = 0; setIdx < stateRecord.getRandomTextureSetCount(); setIdx++) {

                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = Identifier.of(modelFileName(fname + modRot[rotation % 4], setIdx, stateRecord.isCustomModel()));
                    variant.put(VariantSettings.MODEL, modelId);
                    int rot = (90 * (((rotation + 1) % 16) / 4) + stateRecord.rotYOffset) % 360;
                    if (rot > 0) {
                        variant.put(VariantSettings.Y, getRotation(rot));
                    }
                    addVariant("rotation=" + rotation, variant, stateIDs, variants);

                    // make model
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }
}
