package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class TrapDoorBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer y;
        public Variant(String blkname, String ext, int yrot) {
            model = WesterosBlocks.MOD_ID + ":" + blkname + "_" + ext;
            if (yrot != 0)
                y = yrot;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectTrapdoorBottom {
        public String parent = "block/trapdoor_bottom";    // Use 'trapdoor_bottom' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectTrapdoorTop {
        public String parent = "block/trapdoor_top";    // Use 'trapdoor_top' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectTrapdoorOpen {
        public String parent = "block/trapdoor_open";    // Use 'trapdoor_open' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String texture;
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public TrapDoorBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName;
        
        so.variants.put("facing=north,half=bottom,open=false", new Variant(bn, "bottom", 0));
        so.variants.put("facing=south,half=bottom,open=false", new Variant(bn, "bottom", 0));
        so.variants.put("facing=east,half=bottom,open=false", new Variant(bn, "bottom", 0));
        so.variants.put("facing=west,half=bottom,open=false", new Variant(bn, "bottom", 0));
        so.variants.put("facing=north,half=top,open=false", new Variant(bn, "top", 0));
        so.variants.put("facing=south,half=top,open=false", new Variant(bn, "top", 0));
        so.variants.put("facing=east,half=top,open=false", new Variant(bn, "top", 0));
        so.variants.put("facing=west,half=top,open=false", new Variant(bn, "top", 0));
        so.variants.put("facing=north,half=bottom,open=true", new Variant(bn, "open", 0));
        so.variants.put("facing=south,half=bottom,open=true", new Variant(bn, "open", 180));
        so.variants.put("facing=east,half=bottom,open=true", new Variant(bn, "open", 90));
        so.variants.put("facing=west,half=bottom,open=true", new Variant(bn, "open", 270));
        so.variants.put("facing=north,half=top,open=true", new Variant(bn, "open", 0));
        so.variants.put("facing=south,half=top,open=true", new Variant(bn, "open", 180));
        so.variants.put("facing=east,half=top,open=true", new Variant(bn, "open", 90));
        so.variants.put("facing=west,half=top,open=true", new Variant(bn, "open", 270));
        
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        String txt = getTextureID(def.getTextureByIndex(0));
        ModelObjectTrapdoorBottom modb = new ModelObjectTrapdoorBottom();
        modb.textures.texture = txt; 
        this.writeBlockModelFile(def.blockName + "_bottom", modb);
        ModelObjectTrapdoorTop modt = new ModelObjectTrapdoorTop();
        modt.textures.texture = txt; 
        this.writeBlockModelFile(def.blockName + "_top", modt);
        ModelObjectTrapdoorOpen modo = new ModelObjectTrapdoorOpen();
        modo.textures.texture = txt; 
        this.writeBlockModelFile(def.blockName + "_open", modo);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_bottom";
        this.writeItemModelFile(def.blockName, mo);
    }

}
