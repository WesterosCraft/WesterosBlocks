package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class RailBlockModelExport extends ModelExport {
    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer y;
        public Variant(String blkname, String ext) {
            this(blkname, ext, 0);
        }
        public Variant(String blkname, String ext, int yrot) {
            model = WesterosBlocks.MOD_ID + ":block/generated/" + blkname + "_" + ext;
            if (yrot != 0)
                y = yrot;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectRailFlat {
        public String parent = "minecraft:block/rail_flat";    // Use 'rail_flst' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailCurved {
        public String parent = "minecraft:block/rail_curved";    // Use 'rail_curved' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailRaisedNE {
        public String parent = "minecraft:block/rail_raised_ne";    // Use 'rail_raised_ne' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailRaisedSW {
        public String parent = "minecraft:block/rail_raised_sw";    // Use 'rail_raised_sw' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String rail;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public Texture0 textures = new Texture0();
    }
    public static class Texture0 {
        public String layer0;
    }

    
    public RailBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName;
        
        so.variants.put("shape=north_south", new Variant(bn, "flat"));
        so.variants.put("shape=east_west", new Variant(bn, "flat", 90));
        so.variants.put("shape=ascending_east", new Variant(bn, "raised_ne", 90));
        so.variants.put("shape=ascending_west", new Variant(bn, "raised_sw", 90));
        so.variants.put("shape=ascending_north", new Variant(bn, "raised_ne"));
        so.variants.put("shape=ascending_south", new Variant(bn, "raised_sw"));
        so.variants.put("shape=south_east", new Variant(bn, "curved"));
        so.variants.put("shape=south_west", new Variant(bn, "curved", 90));
        so.variants.put("shape=north_west", new Variant(bn, "curved", 180));
        so.variants.put("shape=north_east", new Variant(bn, "curved", 270));
        
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        String txt_norm = getTextureID(def.getTextureByIndex(0));
        String txt_curved = getTextureID(def.getTextureByIndex(1));
        
        ModelObjectRailFlat mod = new ModelObjectRailFlat();
        mod.textures.rail = txt_norm; 
        this.writeBlockModelFile(def.blockName + "_flat", mod);
        
        ModelObjectRailCurved modc = new ModelObjectRailCurved();
        modc.textures.rail = txt_curved; 
        this.writeBlockModelFile(def.blockName + "_curved", modc);
        
        ModelObjectRailRaisedNE modne = new ModelObjectRailRaisedNE();
        modne.textures.rail = txt_norm;
        this.writeBlockModelFile(def.blockName + "_raised_ne", modne);

        ModelObjectRailRaisedSW modsw = new ModelObjectRailRaisedSW();
        modsw.textures.rail = txt_norm;
        this.writeBlockModelFile(def.blockName + "_raised_sw", modsw);

        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = txt_norm;
        this.writeItemModelFile(def.blockName, mo);
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
    	for (String railshape : RAILSHAPE) {
    		oldstate.put("shape", railshape);
    		newstate.put("shape", railshape);
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
