package com.westerosblocks.needsported.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;

public class FireBlockModelExport extends ModelExport {
    public static class SideStates extends States {
    	SideStates() {
    		when = new WhenRec();
    		when.OR = new ArrayList<WhenRec>();
    		WhenRec wr = new WhenRec();
    		wr.north = wr.south = wr.east = wr.west = wr.up = "false";
    		when.OR.add(wr);
    	}
    }
    
    private static class OurApply extends Apply {
    	OurApply(String mn, int y) {
    		model = WesterosBlocks.MOD_ID + ":block/generated/" + mn;
    		if (y != 0) this.y = y;
    	}
    }
    
    // Template objects for Gson export of block models
    public static class ModelObjectFloor {
        public String parent = "minecraft:block/template_fire_floor";    // Use 'fire_floor' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectSide {
        public String parent = "minecraft:block/template_fire_side";    // Use 'fire_side' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectSideAlt {
        public String parent = "minecraft:block/template_fire_side_alt";    // Use 'fire_sida_alt' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectUp {
        public String parent = "minecraft:block/template_fire_up";    // Use 'fire_up' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectUpAlt {
        public String parent = "minecraft:block/template_fire_up_alt";    // Use 'fire_up_alt' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String fire;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }

    public FireBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Make when (all false)
        WhenRec whennone = new WhenRec();
        whennone.north = "false";
        whennone.south = "false";
        whennone.east = "false";
        whennone.west = "false";
        whennone.up = "false";
        // Add firefloor
        States ps = new States();
        ps.when = whennone;
        ps.apply.add(new OurApply(getModelName("floor0", 0), 0));
        ps.apply.add(new OurApply(getModelName("floor1", 0), 0));
        so.addStates(ps, null);
        // Add north:true
        ps = new SideStates();
        WhenRec wrec = new WhenRec();
        wrec.north = "true";
        ps.when.OR.add(wrec);
        ps.apply.add(new OurApply(getModelName("side0", 0), 0));
        ps.apply.add(new OurApply(getModelName("side1", 0), 0));
        ps.apply.add(new OurApply(getModelName("side_alt0", 0), 0));
        ps.apply.add(new OurApply(getModelName("side_alt1", 0), 0));
        so.addStates(ps, null);
        // Add east:true
        ps = new SideStates();
        wrec = new WhenRec();
        wrec.east = "true";
        ps.when.OR.add(wrec);
        ps.apply.add(new OurApply(getModelName("side0", 0), 90));
        ps.apply.add(new OurApply(getModelName("side1", 0), 90));
        ps.apply.add(new OurApply(getModelName("side_alt0", 0), 90));
        ps.apply.add(new OurApply(getModelName("side_alt1", 0), 90));
        so.addStates(ps, null);
        // Add south:true
        ps = new SideStates();
        wrec = new WhenRec();
        wrec.south = "true";
        ps.when.OR.add(wrec);
        ps.apply.add(new OurApply(getModelName("side0", 0), 180));
        ps.apply.add(new OurApply(getModelName("side1", 0), 180));
        ps.apply.add(new OurApply(getModelName("side_alt0", 0), 180));
        ps.apply.add(new OurApply(getModelName("side_alt1", 0), 180));
        so.addStates(ps, null);
        // Add west:true
        ps = new SideStates();
        wrec = new WhenRec();
        wrec.west = "true";
        ps.when.OR.add(wrec);
        ps.apply.add(new OurApply(getModelName("side0", 0), 270));
        ps.apply.add(new OurApply(getModelName("side1", 0), 270));
        ps.apply.add(new OurApply(getModelName("side_alt0", 0), 270));
        ps.apply.add(new OurApply(getModelName("side_alt1", 0), 270));
        so.addStates(ps, null);
        // Add up:true
        ps = new States();
        ps.when = new WhenRec();
        ps.when.up = "true";
        ps.apply.add(new OurApply(getModelName("up0", 0), 0));
        ps.apply.add(new OurApply(getModelName("up1", 0), 0));
        ps.apply.add(new OurApply(getModelName("up_alt0", 0), 0));
        ps.apply.add(new OurApply(getModelName("up_alt1", 0), 0));
        so.addStates(ps, null);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        String txt0 = getTextureID(def.getTextureByIndex(0)); 
        String txt1 = getTextureID(def.getTextureByIndex(1)); 
        // floor0
        ModelObjectFloor modf = new ModelObjectFloor();
        modf.textures.fire = txt0;
        this.writeBlockModelFile(getModelName("floor0", 0), modf);
        // floor1
        modf = new ModelObjectFloor();
        modf.textures.fire = txt1;
        this.writeBlockModelFile(getModelName("floor1", 0), modf);
        // side0
        ModelObjectSide mods = new ModelObjectSide();
        mods.textures.fire = txt0;
        this.writeBlockModelFile(getModelName("side0", 0), mods);
        // side1
        mods = new ModelObjectSide();
        mods.textures.fire = txt1;
        this.writeBlockModelFile(getModelName("side1", 0), mods);
        // side_alt0
        ModelObjectSideAlt modsa = new ModelObjectSideAlt();
        modsa.textures.fire = txt0;
        this.writeBlockModelFile(getModelName("side_alt0", 0), modsa);
        // side_alt1
        modsa = new ModelObjectSideAlt();
        modsa.textures.fire = txt1;
        this.writeBlockModelFile(getModelName("side_alt1", 0), modsa);
        // up0
        ModelObjectUp modu = new ModelObjectUp();
        modu.textures.fire = txt0;
        this.writeBlockModelFile(getModelName("up0", 0), modu);
        // up1
        modu = new ModelObjectUp();
        modu.textures.fire = txt1;
        this.writeBlockModelFile(getModelName("up1", 0), modu);
        // up_alt0
        ModelObjectUpAlt modua = new ModelObjectUpAlt();
        modua.textures.fire = txt0;
        this.writeBlockModelFile(getModelName("up_alt0", 0), modsa);
        // up_alt1
        modua = new ModelObjectUpAlt();
        modua.textures.fire = txt1;
        this.writeBlockModelFile(getModelName("up_alt1", 0), modsa);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = txt0;
        this.writeItemModelFile(def.blockName, mo);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	oldstate.put("north", "false");
    	newstate.put("north", "false");
    	oldstate.put("south", "false");
    	newstate.put("south", "false");
    	oldstate.put("east", "false");
    	newstate.put("east", "false");
    	oldstate.put("west", "false");
    	newstate.put("west", "false");
    	oldstate.put("up", "false");
    	newstate.put("up", "false");
		for (String age : AGE15) {
	    	oldstate.put("age", age);
	    	newstate.put("age", age);
	        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
