package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCPaneBlock;

import net.minecraft.world.level.block.Block;

public class PaneBlockModelExport extends ModelExport {
    private boolean legacy_model;
    private boolean bars_model;

    public static class SideStates extends States {
    	SideStates() {
    		when.OR = new ArrayList<WhenRec>();
    	}
    }
    // Template objects for Gson export of block models
    public static class ModelObjectPost {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/ctm_pane_post";
        public Texture textures = new Texture();
    }
    public static class ModelObjectSide {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/ctm_pane_side";
        public Texture textures = new Texture();
    }
    public static class ModelObjectNoSide {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/ctm_pane_noside";
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String cap;
        public String side;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }

    private WCPaneBlock pblk;
    
    public PaneBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        pblk = (WCPaneBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
        legacy_model = ((WCPaneBlock) blk).isLegacyModel();
        bars_model = ((WCPaneBlock) blk).isBarsModel();
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

    	boolean is_legacy = legacy_model;
    	boolean is_bars = bars_model;
        // Record for when legacy model is needed (all false)
        WhenRec wnone = new WhenRec();
        wnone.north = wnone.south = wnone.east = wnone.west = "false";
    	// Add post based on our variant
        if (!is_bars) {
        	Apply a = new Apply();
            a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("post", 0);
            a.uvlock = true;
            so.addStates(null, a, null);
        }
        
    	// Add north variant
        WhenRec base = new WhenRec();
    	WhenRec wr = new WhenRec();
    	wr.north = "true";
    	base.addOR(wr);
    	if (is_legacy || is_bars) base.addOR(wnone);
    	Apply a = new Apply();
    	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", 0);
    	a.uvlock = true;
    	so.addStates(base, a, null);
    	
    	// Add east variant
        base = new WhenRec();
    	wr = new WhenRec();
    	wr.east = "true";
    	base.addOR(wr);
    	if (is_legacy || is_bars) base.addOR(wnone);
    	a = new Apply();
    	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", 0);
    	a.uvlock = true;
    	a.y = 90;
    	so.addStates(base, a, null);

    	// Add south variant
        base = new WhenRec();
    	wr = new WhenRec();
    	wr.south = "true";
    	base.addOR(wr);
    	if (is_legacy || is_bars) base.addOR(wnone);
    	a = new Apply();
    	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", 0);
    	a.uvlock = true;
    	a.y = 180;
    	so.addStates(base, a, null);

    	// Add west variant
        base = new WhenRec();
    	wr = new WhenRec();
    	wr.west = "true";
    	base.addOR(wr);
    	if (is_legacy || is_bars) base.addOR(wnone);
    	a = new Apply();
    	a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("side", 0);
    	a.uvlock = true;
    	a.y = 270;
    	so.addStates(base, a, null);
    	
    	if (!is_bars) {
    	    // Handle north
    	    wr = new WhenRec();
    	    wr.north = "false";
        	a = new Apply();
    	    a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("noside", 0);
    	    a.uvlock = true;
        	so.addStates(wr, a, null);
    	    // Add east 
    	    wr = new WhenRec();
    	    wr.east = "false";
        	a = new Apply();
    	    a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("noside", 0);
    	    a.y = 90;
    	    a.uvlock = true;
        	so.addStates(wr, a, null);
    	    // Add south not connected variant
    	    wr = new WhenRec();
    	    wr.south = "false";
        	a = new Apply();
    	    a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("noside", 0);
    	    a.y = 180;
    	    a.uvlock = true;
        	so.addStates(wr, a, null);
    	    // Add west not connected variant
    	    wr = new WhenRec();
    	    wr.west = "false";
        	a = new Apply();
    	    a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("noside", 0);
    	    a.uvlock = true;
    	    a.y = 270;
        	so.addStates(wr, a, null);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean is_bars = bars_model;
        if (!is_bars) {
            ModelObjectPost mod = new ModelObjectPost();
            mod.textures.side = getTextureID(def.getTextureByIndex(0));
            mod.textures.cap = getTextureID(def.getTextureByIndex(1));
            this.writeBlockModelFile(getModelName("post", 0), mod);
        }
        // Side model
        ModelObjectSide smod = new ModelObjectSide();
        smod.textures.side = getTextureID(def.getTextureByIndex(0)); 
        smod.textures.cap = getTextureID(def.getTextureByIndex(1)); 
        if (is_bars) smod.parent = "westerosblocks:block/untinted/bars_side";
        this.writeBlockModelFile(getModelName("side", 0), smod);
        if (!is_bars) {
            // NoSide model
            ModelObjectNoSide nsmod = new ModelObjectNoSide();
            nsmod.textures.side = getTextureID(def.getTextureByIndex(0)); 
            this.writeBlockModelFile(getModelName("noside", 0), nsmod);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = getTextureID(def.getTextureByIndex(0));
        this.writeItemModelFile(def.blockName, mo);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	// No metadata other than variant - need filter for all of this - just pass one combination
    	oldstate.put("north", "false");
    	newstate.put("north", "false");
    	oldstate.put("south", "false");
    	newstate.put("south", "false");
    	oldstate.put("east", "false");
    	newstate.put("east", "false");
    	oldstate.put("west", "false");
    	newstate.put("west", "false");
    	newstate.put("waterlogged", "false");
    	if (pblk.unconnect) {
        	newstate.put("unconnect", "false");    		
    	}
    	addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
