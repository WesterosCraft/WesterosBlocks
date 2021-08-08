package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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
        // Loop over the random sets we've got
        List<Variant> vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	// East is base model
        	Variant var = new Variant();
        	var.model = modelFileName("", setidx);
        	vars.add(var);
        }
        so.variants.put("facing=east", vars);
        
        // North is 90 degree rotate
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("", setidx);
        	var.y = 90;
        	vars.add(var);
        }
        so.variants.put("facing=north", vars);
        
        this.writeBlockStateFile(def.blockName, so);
    }
}
