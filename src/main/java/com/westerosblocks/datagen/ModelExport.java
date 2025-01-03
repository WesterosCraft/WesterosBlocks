package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.util.Identifier;

public class ModelExport {

    public static Identifier createBlockIdentifier(String texturePath) {
        return Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath);
    }
}
