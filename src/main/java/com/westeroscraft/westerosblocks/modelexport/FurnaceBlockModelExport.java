package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class FurnaceBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer y;
        public Variant(String bn, int y) {
            model = WesterosBlocks.MOD_ID + ":block/" + bn;
            if (y != 0)
                this.y = y;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCubeAll {
        public String parent = "block/orientable";    // Use 'orientable' model for single texture
        public TextureOrient textures = new TextureOrient();
    }
    public static class TextureOrient {
        public String top;
        public String front;
        public String side;
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public FurnaceBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName + "_lit";
        so.variants.put("facing=north,lit=true", new Variant(bn, 0));
        so.variants.put("facing=south,lit=true", new Variant(bn, 180));
        so.variants.put("facing=west,lit=true", new Variant(bn, 270));
        so.variants.put("facing=east,lit=true", new Variant(bn, 90));
        bn = def.blockName;
        so.variants.put("facing=north,lit=false", new Variant(bn, 0));
        so.variants.put("facing=south,lit=false", new Variant(bn, 180));
        so.variants.put("facing=west,lit=false", new Variant(bn, 270));
        so.variants.put("facing=east,lit=false", new Variant(bn, 90));
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        ModelObjectCubeAll mod = new ModelObjectCubeAll();
        mod.textures.top = getTextureID(def.getTextureByIndex(1)); 
        mod.textures.side = getTextureID(def.getTextureByIndex(2)); 
        mod.textures.front = getTextureID(def.getTextureByIndex(3)); // ON
        if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/orientable";
        this.writeBlockModelFile(def.blockName + "_lit", mod);
            
        mod = new ModelObjectCubeAll();
        mod.textures.top = getTextureID(def.getTextureByIndex(1)); 
        mod.textures.side = getTextureID(def.getTextureByIndex(2)); 
        mod.textures.front = getTextureID(def.getTextureByIndex(4)); // Off
        if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/orientable";
        this.writeBlockModelFile(def.blockName, mod);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);
        // Handle tint resources
        if (isTinted) {
        	String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

}
