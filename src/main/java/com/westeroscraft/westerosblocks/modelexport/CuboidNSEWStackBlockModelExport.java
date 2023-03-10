package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.world.level.block.Block;

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
            	doCuboidModel(getModelName("base", setidx), isTinted, txt0, textures, se.cuboids,
            			null);
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
            	doCuboidModel(getModelName("top", setidx), isTinted, txt02, textures2, se2.cuboids,
            			null);
            }
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = modelFileName("base", 0);
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
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("base", setidx);
        	var.y = 270;
        	so.addVariant("facing=north,half=lower", var, set.condIDs);
        }
        //
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("base", setidx);        
        	so.addVariant("facing=east,half=lower", var, set.condIDs);
        }
        //
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("base", setidx);        
        	var.y = 90;
        	so.addVariant("facing=south,half=lower", var, set.condIDs);
        }
        //
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("base", setidx);        
        	var.y = 180;
        	so.addVariant("facing=west,half=lower", var, set.condIDs);
        }

        // Top states
        se = def.getStackElementByIndex(1);
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("top", setidx);        
        	var.y = 270;
        	so.addVariant("facing=north,half=upper", var, set.condIDs);
        }
        //
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("top", setidx);        
        	so.addVariant("facing=east,half=upper", var, set.condIDs);
        }
        //
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("top", setidx);        
        	var.y = 90;
        	so.addVariant("facing=south,half=upper", var, set.condIDs);
        }
        //
        for (int setidx = 0; setidx < se.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = se.getRandomTextureSet(setidx);
        	Variant var = new Variant();
        	var.model = modelFileName("top", setidx);        
        	var.y = 180;
        	so.addVariant("facing=west,half=upper", var, set.condIDs);
        }
        
        this.writeBlockStateFile(def.blockName, so);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	newstate.put("waterlogged", "false");
    	for (String facing : FACING) {
    		oldstate.put("facing", facing);
    		newstate.put("facing", facing);
    		
    		oldstate.put("top", "true");
    		newstate.put("half", "upper");
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	
			oldstate.put("top", "false");
			newstate.put("half", "lower");
			addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
