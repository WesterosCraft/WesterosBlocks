package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class CuboidNSEWBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 270;
            so.variants.put(String.format("facing=north,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            so.variants.put(String.format("facing=east,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 90;
            so.variants.put(String.format("facing=south,variant=%d", sb.meta), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 180;
            so.variants.put(String.format("facing=west,variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(def.blockName, so);
    }
}
