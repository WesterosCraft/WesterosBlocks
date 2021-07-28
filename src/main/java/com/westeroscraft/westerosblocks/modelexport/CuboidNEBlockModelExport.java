package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class CuboidNEBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNEBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String mod = def.isCustomModel() ? 
        		WesterosBlocks.MOD_ID + ":block/custom/" + def.blockName : 
    			WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        // East is base model
        Variant var = new Variant();
        var.model = mod;
        so.variants.put("facing=east", var);
        // North is 90 degree rotate
        var = new Variant();
        var.model = mod;
        var.y = 90;
        so.variants.put("facing=north", var);
        
        this.writeBlockStateFile(def.blockName, so);
    }
}
