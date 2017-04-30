package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.block.Block;

public class ModelExportFactory {
    public static ModelExport   forBlock(Block blk, WesterosBlockDef def, File dest) {
        ModelExport me = null;
        switch (def.blockType) {
            case "solid":
                me = new SolidBlockModelExport(blk, def, dest);
                break;
        }
        return me;
    }
}
