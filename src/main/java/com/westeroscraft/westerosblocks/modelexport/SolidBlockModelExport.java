package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class SolidBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCubeAll {
        public String parent = "minecraft:block/cube_all";    // Use 'cube_all' model for single texture
        public TextureAll textures = new TextureAll();
    }
    public static class TextureAll {
        public String all;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public String parent = "minecraft:block/cube";    // Use 'cube' model for multiple textures
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public SolidBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
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
        Object model;
        boolean isTinted = def.isTinted();
        if ((def.textures.size() > 1) || isTinted) { // More than one texture, or tinted?
            ModelObjectCube mod = new ModelObjectCube();
            mod.textures.down = getTextureID(def.getTextureByIndex(0));
            mod.textures.up = getTextureID(def.getTextureByIndex(1));
            mod.textures.north = getTextureID(def.getTextureByIndex(2));
            mod.textures.south = getTextureID(def.getTextureByIndex(3));
            mod.textures.west = getTextureID(def.getTextureByIndex(4));
            mod.textures.east = getTextureID(def.getTextureByIndex(5));
            mod.textures.particle = getTextureID(def.getTextureByIndex(2));
            if (isTinted) {
                mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube";
            }
            model = mod;
        }
        else {
            ModelObjectCubeAll mod = new ModelObjectCubeAll();
            mod.textures.all = getTextureID(def.getTextureByIndex(0)); 
            model = mod;
        }
        this.writeBlockModelFile(def.blockName, model);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
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
