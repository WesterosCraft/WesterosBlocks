package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;

public class FlowerPotBlockModelExport extends ModelExport {
    public static class OurVariant extends Variant {
        public OurVariant(final String modname, final int xrot, final int yrot, Integer weight) {
            model = modname;
            if (xrot != 0)
                x = xrot;
            if (yrot != 0)
                y = yrot;
            if ((xrot != 0) || (yrot != 0))
                uvlock = true;
            this.weight = weight;
        }
    }

    protected String getModelName(String ext, int setidx) {
    	return def.getBlockName() + "/" + ext + ("_v" + (setidx+1));
    }
    // Template objects for Gson export of block models
    public static class ModelObjectFlowerPot {
        public String parent;
        public Texture textures = new Texture();

        public ModelObjectFlowerPot(final boolean empty, final boolean tinted) {
            if (tinted) {
                parent = empty ? "westerosblocks:block/tinted/flower_pot" : "westerosblocks:block/tinted/flower_pot_cross";
            } else {
                parent = empty ? "westerosblocks:block/untinted/flower_pot" : "westerosblocks:block/untinted/flower_pot_cross";
            }
        }
    }

    public static class Texture {
        public String dirt, flowerpot, plant;
        public String particle;
    }

    public static class ModelObject {
        public String parent;
    }

    private WesterosBlockDef bbdef = null;
    private FlowerPotBlock blk = null;
    private int setcnt = 1;
    private boolean isTinted = false;

    public FlowerPotBlockModelExport(final Block blk, final WesterosBlockDef def, final File dest) {
        super(blk, def, dest);
        this.blk = (FlowerPotBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    public String modelFileName(String ext, int setidx) {
    	if (def.isCustomModel())
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
    }

    private void doInit() throws IOException {
        Block bblk = this.blk;
        // Find base block for stairs - textures come from there
    	if (bblk instanceof WesterosBlockLifecycle) {
    		bbdef = ((WesterosBlockLifecycle) bblk).getWBDefinition();
    		setcnt = bbdef.getRandomTextureSetCount();
    		isTinted = bbdef.isTinted();
    	}    	
    }
    @Override
    public void doBlockStateExport() throws IOException {
        final StateObject so = new StateObject();
        doInit();
        
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	String model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", setidx);
        	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
            for (int i = 0; i < cnt; i++) {
            	Variant var = new Variant();
            	var.model = model;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
    			so.addVariant("", var, null);	// Add our variant                	
            }
        }
        this.writeBlockStateFile(def.getBlockName(), so);
    }

    @Override
    public void doModelExports() throws IOException {
        doInit();
        boolean isEmpty = blk.getEmptyPot() == blk;
        // Export if not set to custom model
        if (!def.isCustomModel()) {
	        // Loop over the random sets we've got
	        for (int setidx = 0; setidx < setcnt; setidx++) {
	        	String dirttext = null;
	        	String pottext = null;
	        	String planttext = null;
	        	if (bbdef != null) {
	            	WesterosBlockDef.RandomTextureSet set = bbdef.getRandomTextureSet(setidx);        		
	         		dirttext = getTextureID(set.getTextureByIndex(0));
	         		pottext = getTextureID(set.getTextureByIndex(1));
	         		planttext = getTextureID(set.getTextureByIndex(2));
	        	}
	        	else {
	        		dirttext = "minecraft:block/dirt";
	        		pottext = "minecraft:block/flower_pot";
	        		planttext = "minecraft:block/red_tulip";
	        	}
	        	// Base model
	        	final ModelObjectFlowerPot base = new ModelObjectFlowerPot(isEmpty, isTinted);
	        	base.textures.dirt = dirttext;
	        	base.textures.flowerpot = pottext;
	        	base.textures.plant = planttext;
	        	base.textures.particle = pottext;
	        	this.writeBlockModelFile(getModelName("base", setidx), base);
	        }
        }
    	// Build simple item model that refers to base block model
        final ModelObject mo = new ModelObject();
        mo.parent = modelFileName("base", 0);
        this.writeItemModelFile(def.getBlockName(), mo);

        // Handle tint resources
        if (isTinted) {
            final String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), null, tintres);
            }
        }
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") (need stairs connection filter)");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	newstate.put("waterlogged", "false");
    	addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);    
    }
}