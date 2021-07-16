package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class CrossBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCross {
        public String parent = "block/cross";    // Use 'cross' model for single texture
        public TextureCross textures = new TextureCross();
    }
    public static class TextureCross {
        public String cross;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }
    
    public CrossBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        Variant var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        so.variants.put("", var);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        ModelObjectCross mod = new ModelObjectCross();
        mod.textures.cross = getTextureID(def.getTextureByIndex(0)); 
        // Use tinted cross if 
        boolean isTinted = def.isTinted();
        if (isTinted) {
            mod.parent = "block/tinted_cross";
        }
        this.writeBlockModelFile(def.blockName, mod);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = mod.textures.cross;
        this.writeItemModelFile(def.blockName, mo);
            
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

}
