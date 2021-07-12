package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class CropBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCrop {
        public String parent = "block/crop";    // Use 'crop' model for single texture
        public TextureCrop textures = new TextureCrop();
    }
    public static class TextureCrop {
        public String crop;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }
    
    public CropBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        Variant var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":" + def.blockName;
        so.variants.put("", var);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        ModelObjectCrop mod = new ModelObjectCrop();
        mod.textures.crop = getTextureID(def.getTextureByIndex(0)); 
        this.writeBlockModelFile(def.blockName, mod);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = mod.textures.crop;
        this.writeItemModelFile(def.blockName, mo);
    }
}
