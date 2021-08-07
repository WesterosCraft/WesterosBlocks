package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import java.util.List;
import java.util.ArrayList;

public class CuboidNSEWUDBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWUDBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

        List<Variant> vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelName(setidx);
        	vars.add(var);
        }
        so.variants.put("facing=north", vars);
        
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelName(setidx);
            var.y = 90;
        	vars.add(var);
        }
        so.variants.put("facing=east", vars);
        
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelName(setidx);
        	var.y = 180;
        	vars.add(var);
        }
        so.variants.put("facing=south", vars);
        
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelName(setidx);
        	var.y = 270;
        	vars.add(var);
        }
        so.variants.put("facing=west", vars);
        
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelName(setidx);
        	var.x = 270;
        	vars.add(var);
        }
        so.variants.put("facing=up", vars);
        
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelName(setidx);
        	var.x = 90;
        	vars.add(var);
        }
        so.variants.put("facing=down", vars);
        
        this.writeBlockStateFile(def.blockName, so);
    }
}
