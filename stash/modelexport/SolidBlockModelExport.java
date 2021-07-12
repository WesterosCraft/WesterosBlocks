package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

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
        public String parent = "block/cube_all";    // Use 'cube_all' model for single texture
        public TextureAll textures = new TextureAll();
    }
    public static class TextureAll {
        public String all;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public String parent = "block/cube";    // Use 'cube' model for multiple textures
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
        for (Subblock sb : def.subBlocks) {
            addNLSString("tile." + def.blockName + "_" + sb.meta + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            Variant var = new Variant();
            var.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            so.variants.put(String.format("variant=%d", sb.meta), var);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            Object model;
            boolean isTinted = sb.isTinted(def);
            if ((sb.textures.size() > 1) || isTinted) { // More than one texture, or tinted?
                ModelObjectCube mod = new ModelObjectCube();
                mod.textures.down = getTextureID(sb.getTextureByIndex(0));
                mod.textures.up = getTextureID(sb.getTextureByIndex(1));
                mod.textures.north = getTextureID(sb.getTextureByIndex(2));
                mod.textures.south = getTextureID(sb.getTextureByIndex(3));
                mod.textures.west = getTextureID(sb.getTextureByIndex(4));
                mod.textures.east = getTextureID(sb.getTextureByIndex(5));
                mod.textures.particle = getTextureID(sb.getTextureByIndex(2));
                if (isTinted) {
                    mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube";
                }
                model = mod;
            }
            else {
                ModelObjectCubeAll mod = new ModelObjectCubeAll();
                mod.textures.all = getTextureID(sb.getTextureByIndex(0)); 
                model = mod;
            }
            this.writeBlockModelFile(def.blockName + "_" + sb.meta, model);
            // Build simple item model that refers to block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_" + sb.meta;
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);
            // Add tint overrides
            if (isTinted) {
                String tintres = def.getBlockColorMapResource(sb);
                if (tintres != null) {
                    ModelExport.addTintingOverride(def.blockName, String.format("variant=%s", sb.meta), tintres);
                }
            }
        }
    }

}
