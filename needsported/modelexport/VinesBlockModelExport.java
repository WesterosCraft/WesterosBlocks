package com.westerosblocks.needsported.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDef.RandomTextureSet;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.modelexport.FireBlockModelExport.TextureLayer0;
import com.westeroscraft.westerosblocks.blocks.WCVinesBlock;

import net.minecraft.world.level.block.Block;

public class VinesBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObject {
        public String parent;
        public Texture textures = new Texture();
        ModelObject(String ext, String txt) {
        	parent = WesterosBlocks.MOD_ID + ":block/vines/vine_" + ext;
        	textures.vines = txt;
        }
    }
    public static class Texture {
        public String vines;
    }
    public static class ItemModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }
    
    WCVinesBlock vblk;
    
    public VinesBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        vblk = (WCVinesBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    public String modelFileName(String ext, int setidx, boolean isCustom) {
    	if (isCustom)
    		return WesterosBlocks.MOD_ID + ":block/custom/" + getModelName(ext, setidx);
    	else
    		return WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(ext, setidx);
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            for (int i = 0; i < cnt; i++) {
    	        // South
    	        WhenRec when = new WhenRec();
    	        when.south = "true"; 
    	        Apply apply = new Apply();
    	        apply.model = modelFileName("base", setidx, def.isCustomModel());
    	        apply.weight = set.weight;
    	        so.addStates(when, apply, null);
    	        // West
    	        when = new WhenRec();
    	        when.west = "true"; 
    	        apply = new Apply();
    	        apply.model = modelFileName("base", setidx, def.isCustomModel());
    	        apply.weight = set.weight;
    	        apply.y = 90;
    	        so.addStates(when, apply, null);
    	        // North
    	        when = new WhenRec();
    	        when.north = "true";
    	        apply = new Apply();
    	        apply.model = modelFileName("base", setidx, def.isCustomModel());
    	        apply.weight = set.weight;
    	        apply.y = 180;
    	        so.addStates(when, apply, null);
    	        // East
    	        when = new WhenRec();
    	        when.east = "true";
    	        apply = new Apply();
    	        apply.model = modelFileName("base", setidx, def.isCustomModel());
    	        apply.weight = set.weight;
    	        apply.y = 270;
    	        so.addStates(when, apply, null);
    	        // Up
    	        when = new WhenRec();
    	        when.up = "true";
    	        apply = new Apply();
    	        apply.model = modelFileName("top", setidx, def.isCustomModel());
    	        apply.weight = set.weight;
    	        so.addStates(when, apply, null);
    	        // Down
    	        when = new WhenRec();
    	        when.down = "true";
    	        apply = new Apply();
    	        apply.model = modelFileName("top", setidx, def.isCustomModel());;
    	        apply.weight = set.weight;
    	        apply.x = 180;
    	        so.addStates(when, apply, null);
            }
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
    	String txt = getTextureID(def.getTextureByIndex(0));
    	// Build models
        if (!def.isCustomModel()) {
        	// Loop over the random sets we've got
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	RandomTextureSet set = def.getRandomTextureSet(setidx);
            	ModelObject mo1 = new ModelObject("1", getTextureID(set.getTextureByIndex(0)));
                this.writeBlockModelFile(getModelName("base", setidx), mo1);
            	ModelObject mou = new ModelObject("u", getTextureID(set.getTextureByIndex(1)));
                this.writeBlockModelFile(getModelName("top", setidx), mou);
            }
    	}
        // Build simple item model that refers to block model
        ItemModelObject mo = new ItemModelObject();
        mo.textures.layer0 = txt;
        this.writeItemModelFile(def.blockName, mo);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") - need vine connection mapping");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	oldstate.put("up", "false");
    	newstate.put("waterlogged", "false");
    	for (String north : BOOLEAN) {
        	oldstate.put("north", north);
        	newstate.put("north", north);
    		for (String south : BOOLEAN) {
    	    	oldstate.put("south", south);
    	    	newstate.put("south", south);
    			for (String east : BOOLEAN) {
    		    	oldstate.put("east", east);
    		    	newstate.put("east", east);
    				for (String west : BOOLEAN) {
    			    	oldstate.put("west", west);
    			    	newstate.put("west", west);
    			    	for (String up : BOOLEAN) {
        			    	oldstate.put("#up", up);
        			    	newstate.put("up", up);
        			    	for (String down : BOOLEAN) {
            			    	oldstate.put("down", down);
            			    	newstate.put("down", (north.equals("false")) && (south.equals("false")) && (east.equals("false")) && (west.equals("false")) ? "true" : "false");
            			    	addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
        			    	}
    			    	}
    				}
    			}
    		}
    	}
    }

}
