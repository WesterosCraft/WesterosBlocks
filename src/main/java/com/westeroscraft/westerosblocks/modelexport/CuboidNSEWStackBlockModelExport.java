package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class CuboidNSEWStackBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWStackBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            int variant = sb.meta / 2;
            boolean istop = (sb.meta & 1) > 0;
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 270;
            so.variants.put(String.format("facing=north,top=%s,variant=%d", Boolean.toString(istop), variant), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            so.variants.put(String.format("facing=east,top=%s,variant=%d", Boolean.toString(istop), variant), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 90;
            so.variants.put(String.format("facing=south,top=%s,variant=%d", Boolean.toString(istop), variant), var);
            var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            var.y = 180;
            so.variants.put(String.format("facing=west,top=%s,variant=%d", Boolean.toString(istop), variant), var);
        }
        this.writeBlockStateFile(def.blockName, so);
    }
}
