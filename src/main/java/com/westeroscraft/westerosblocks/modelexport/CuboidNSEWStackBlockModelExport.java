package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.block.Block;

public class CuboidNSEWStackBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWStackBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Export if not set to custom model
        if (!def.isCustomModel()) {
        	// Bottom model
        	WesterosBlockDef.StackElement se = def.getStackElementByIndex(0);
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
            	ModelObjectCuboid mod = new ModelObjectCuboid();
            	String txt0 = set.getTextureByIndex(0);
            	mod.textures.put("particle", getTextureID(set.getTextureByIndex(0)));
            	int cnt = Math.max(6, set.getTextureCount());
            	String[] textures = new String[cnt];
            	for (int i = 0; i < cnt; i++) {
            		textures[i] = set.getTextureByIndex(i);
            	}
            	doCuboidModel(modelName(setidx), isTinted, txt0, textures, se.cuboids);
            }
        	// Top model
        	WesterosBlockDef.StackElement se2 = def.getStackElementByIndex(1);
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < se2.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = se2.getRandomTextureSet(setidx);        	
            	ModelObjectCuboid mod2 = new ModelObjectCuboid();
            	String txt02 = set.getTextureByIndex(0);
            	mod2.textures.put("particle", getTextureID(set.getTextureByIndex(0)));
            	int cnt2 = Math.max(6, set.getTextureCount());
            	String[] textures2 = new String[cnt2];
            	for (int i = 0; i < cnt2; i++) {
            		textures2[i] = set.getTextureByIndex(i);
            	}
            	doCuboidModel(modelName("_top", setidx), isTinted, txt02, textures2, se2.cuboids);
            }
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = modelFileName("", 0);
        this.writeItemModelFile(def.blockName, mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Bottom states
    	WesterosBlockDef.StackElement se = def.getStackElementByIndex(0);
        // Loop over the random sets we've got
    	List<Variant> vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("", setidx);
        	var.y = 270;
        	vars.add(var);
        }
        so.variants.put("facing=north,half=lower", vars);
        //
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("", setidx);        
        	vars.add(var);
        }
    	so.variants.put("facing=east,half=lower", vars);
        //
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("", setidx);        
        	var.y = 90;
        	vars.add(var);
        }
        so.variants.put("facing=south,half=lower", vars);
        //
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("", setidx);        
        	var.y = 180;
        	vars.add(var);
        }
        so.variants.put("facing=west,half=lower", vars);

        // Top states
        se = def.getStackElementByIndex(1);
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("_top", setidx);        
        	var.y = 270;
        	vars.add(var);
        }
        so.variants.put("facing=north,half=upper", vars);
        //
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("_top", setidx);        
        	vars.add(var);
        }
    	so.variants.put("facing=east,half=upper", vars);
        //
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("_top", setidx);        
        	var.y = 90;
        	vars.add(var);
        }
        so.variants.put("facing=south,half=upper", vars);
        //
        vars = new ArrayList<Variant>();
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	Variant var = new Variant();
        	var.model = modelFileName("_top", setidx);        
        	var.y = 180;
        	vars.add(var);
        }
        so.variants.put("facing=west,half=upper", vars);
        
        this.writeBlockStateFile(def.blockName, so);
    }
}
