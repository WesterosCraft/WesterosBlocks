package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class RailBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

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
            model = WesterosBlocks.MOD_ID + ":" + blkname + "_" + ext;
            if (yrot != 0)
                y = yrot;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectRailFlat {
        public String parent = "block/rail_flat";    // Use 'rail_flst' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailCurved {
        public String parent = "block/rail_curved";    // Use 'rail_curved' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailRaisedNE {
        public String parent = "block/rail_raised_ne";    // Use 'rail_raised_ne' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectRailRaisedSW {
        public String parent = "block/rail_raised_sw";    // Use 'rail_raised_sw' model for single texture
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
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.subBlocks.get(0).label);
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
        Subblock sb = def.subBlocks.get(0);
        String txt_norm = getTextureID(sb.getTextureByIndex(0));
        String txt_curved = getTextureID(sb.getTextureByIndex(1));
        
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

}
