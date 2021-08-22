package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class CropBlockModelExport extends ModelExport {
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
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        Variant var = new Variant();
        var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
        so.addVariant("", var, null);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        ModelObjectCrop mod = new ModelObjectCrop();
        mod.textures.crop = getTextureID(def.getTextureByIndex(0)); 
        this.writeBlockModelFile(getModelName("base", 0), mod);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = mod.textures.crop;
        this.writeItemModelFile(def.blockName, mo);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), null);
    }

}
