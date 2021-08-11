package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCPaneBlock;

import net.minecraft.block.Block;

public class PaneBlockModelExport extends ModelExport {
    private boolean legacy_model;
    private boolean bars_model;

    // Template objects for Gson export of block state
    public static class StateObject {
    	public List<States> multipart = new ArrayList<States>();
    }
    public static class States {
    	public WhenRec when = new WhenRec();
    	public Apply apply = new Apply();
    }
    public static class SideStates extends States {
    	SideStates() {
    		when.OR = new ArrayList<WhenRec>();
    	}
    }
    public static class WhenRec {
    	Boolean north, south, west, east;
    	public List<WhenRec> OR;
    }
    public static class Apply {
    	String model;
    	Integer x;
    	Integer y;
    	Boolean uvlock;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectPost {
        public String parent = "minecraft:block/template_glass_pane_post";    // Use 'post' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectSide {
        public String parent = "minecraft:block/template_glass_pane_side";    // Use 'side' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectSideAlt {
        public String parent = "minecraft:block/template_glass_pane_side_alt";    // Use 'side_alt' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectNoSide {
        public String parent = "minecraft:block/template_glass_pane_noside";    // Use 'side' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectNoSideAlt {
        public String parent = "minecraft:block/template_glass_pane_noside_alt";    // Use 'side_alt' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String edge;
        public String pane;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }

    public PaneBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
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
        wnone.north = wnone.south = wnone.east = wnone.west = Boolean.FALSE;
    	// Add post based on our variant
        if (!is_bars) {
            States ps = new States();
            ps.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_post";
            ps.when = null;
            so.multipart.add(ps);
        }
    	// Add north variant
    	SideStates ssn = new SideStates();
    	WhenRec wr = new WhenRec();
    	wr.north = true;
    	ssn.when.OR.add(wr);
    	if (is_legacy || is_bars) ssn.when.OR.add(wnone);
    	ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_side";
    	ssn.apply.uvlock = true;
    	so.multipart.add(ssn);
    	// Add east variant
    	ssn = new SideStates();
    	wr = new WhenRec();
    	wr.east = true;
    	ssn.when.OR.add(wr);
    	if (is_legacy || is_bars) ssn.when.OR.add(wnone);
    	ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_side";
    	ssn.apply.uvlock = true;
    	ssn.apply.y = 90;
    	so.multipart.add(ssn);
    	// Add south variant
    	ssn = new SideStates();
    	wr = new WhenRec();
    	wr.south = true;
    	ssn.when.OR.add(wr);
    	if (is_legacy || is_bars) ssn.when.OR.add(wnone);
    	ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_side_alt";
    	ssn.apply.uvlock = true;
    	so.multipart.add(ssn);
    	// Add west variant
    	ssn = new SideStates();
    	wr = new WhenRec();
    	wr.west = true;
    	ssn.when.OR.add(wr);
    	if (is_legacy || is_bars) ssn.when.OR.add(wnone);
    	ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_side_alt";
    	ssn.apply.uvlock = true;
    	ssn.apply.y = 90;
    	so.multipart.add(ssn);
    	if (!is_bars) {
    	    // Handle north not connected variant
    	    ssn = new SideStates();
    	    wr = new WhenRec();
    	    wr.north = false;
    	    ssn.when.OR.add(wr);
    	    ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_noside";
    	    ssn.apply.uvlock = true;
    	    so.multipart.add(ssn);
    	    // Add east not connected variant
    	    ssn = new SideStates();
    	    wr = new WhenRec();
    	    wr.east = false;
    	    ssn.when.OR.add(wr);
    	    ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_noside_alt";
    	    ssn.apply.uvlock = true;
    	    so.multipart.add(ssn);
    	    // Add south not connected variant
    	    ssn = new SideStates();
    	    wr = new WhenRec();
    	    wr.south = false;
    	    ssn.when.OR.add(wr);
    	    ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_noside_alt";
    	    ssn.apply.y = 90;
    	    ssn.apply.uvlock = true;
    	    so.multipart.add(ssn);
    	    // Add west not connected variant
    	    ssn = new SideStates();
    	    wr = new WhenRec();
    	    wr.west = false;
    	    ssn.when.OR.add(wr);
    	    ssn.apply.model = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName + "_noside";
    	    ssn.apply.uvlock = true;
    	    ssn.apply.y = 270;
    	    so.multipart.add(ssn);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean is_bars = bars_model;
        if (!is_bars) {
            ModelObjectPost mod = new ModelObjectPost();
            mod.textures.pane = getTextureID(def.getTextureByIndex(0)); 
            mod.textures.edge = getTextureID(def.getTextureByIndex(1)); 
            this.writeBlockModelFile(def.blockName + "_post", mod);
        }
        // Side model
        ModelObjectSide smod = new ModelObjectSide();
        smod.textures.pane = getTextureID(def.getTextureByIndex(0)); 
        smod.textures.edge = getTextureID(def.getTextureByIndex(1)); 
        if (is_bars) smod.parent = "westerosblocks:block/untinted/bars_side";
        this.writeBlockModelFile(def.blockName + "_side", smod);
        // Side-alt model
        ModelObjectSideAlt samod = new ModelObjectSideAlt();
        samod.textures.pane = getTextureID(def.getTextureByIndex(0)); 
        samod.textures.edge = getTextureID(def.getTextureByIndex(1)); 
        if (is_bars) samod.parent = "westerosblocks:block/untinted/bars_side_alt";
        this.writeBlockModelFile(def.blockName + "_side_alt", samod);
        if (!is_bars) {
            // NoSide model
            ModelObjectNoSide nsmod = new ModelObjectNoSide();
            nsmod.textures.pane = getTextureID(def.getTextureByIndex(0)); 
            this.writeBlockModelFile(def.blockName + "_noside", nsmod);
            // NoSide-alt model
            ModelObjectNoSideAlt nsamod = new ModelObjectNoSideAlt();
            nsamod.textures.pane = getTextureID(def.getTextureByIndex(0)); 
            this.writeBlockModelFile(def.blockName + "_noside_alt", nsamod);
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
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") - need glass pane connection mapping");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	oldstate.put("north", "$0");
    	oldstate.put("east", "$1");
    	oldstate.put("south", "$2");
    	oldstate.put("west", "$3");
    	newstate.put("north", "$0");
    	newstate.put("east", "$1");
    	newstate.put("south", "$2");
    	newstate.put("west", "$3");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }

}
