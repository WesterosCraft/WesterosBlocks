package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.world.level.block.Block;
import java.util.HashMap;

public class Cuboid16WayBlockModelExport extends CuboidBlockModelExport {
    
    public Cuboid16WayBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    private static final String modRot[] = { "base", "rotn22", "rotn45", "rot22" };
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();        
        // For each direction
        for (int rotation = 0; rotation < 16; rotation++) {
	        // Loop over the random sets we've got
	        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
	        	Variant var = new Variant();
	        	var.model = modelFileName(modRot[rotation % 4], setidx);
	        	var.y = 90 * (((rotation + 1) % 16) / 4);
	        	so.addVariant("rotation=" + rotation, var, set.condIDs);
	        }
        }
        this.writeBlockStateFile(def.blockName, so);
    }
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Export if not set to custom model
        if (!def.isCustomModel()) {
            // Loop over the random sets we've got for base model (and for each 22 degree model
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	doCuboidModel(getModelName("base", setidx), isTinted, setidx, null);
            	doCuboidModel(getModelName("rotn22", setidx), isTinted, setidx, -22.5F);
            	doCuboidModel(getModelName("rotn45", setidx), isTinted, setidx, -45F);
            	doCuboidModel(getModelName("rot22", setidx), isTinted, setidx, 22.5F);
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
    	for (int rotation = 0; rotation < 16; rotation++) {
    		oldstate.put("rotation", Integer.toString(rotation));
    		newstate.put("rotation", Integer.toString(rotation));
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
