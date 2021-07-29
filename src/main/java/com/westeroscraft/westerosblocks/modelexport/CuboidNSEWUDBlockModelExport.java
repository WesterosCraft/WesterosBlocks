package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class CuboidNSEWUDBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWUDBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String mod = modelName();
        
        Variant var = new Variant();
        var.model = mod;
        so.variants.put("facing=north", var);
        var = new Variant();
        var.model = mod;
        var.y = 90;
        so.variants.put("facing=east", var);
        var = new Variant();
        var.model = mod;
        var.y = 180;
        so.variants.put("facing=south", var);
        var = new Variant();
        var.model = mod;
        var.y = 270;
        so.variants.put("facing=west", var);
        var = new Variant();
        var.model = mod;
        var.x = 270;
        so.variants.put("facing=up", var);
        var = new Variant();
        var.model = mod;
        var.x = 90;
        so.variants.put("facing=down", var);
        
        this.writeBlockStateFile(def.blockName, so);
    }
}
