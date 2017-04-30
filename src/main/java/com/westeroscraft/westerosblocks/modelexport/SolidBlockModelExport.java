package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.WesterosBlockConfig;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;

import net.minecraft.block.Block;

public class SolidBlockModelExport extends ModelExport {
    private WCSolidBlock blk;
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
        public String down, up, north, south, west, east;
    }

    
    public SolidBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, dest);
        this.blk = (WCSolidBlock) blk;
        this.def = def;
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
            if (sb.textures.size() == 1) {  // Only one?
                ModelObjectCubeAll mod = new ModelObjectCubeAll();
                mod.textures.all = getTextureID(sb.getTextureByIndex(0)); 
                model = mod;
            }
            else {
                ModelObjectCube mod = new ModelObjectCube();
                mod.textures.down = getTextureID(sb.getTextureByIndex(0));
                mod.textures.up = getTextureID(sb.getTextureByIndex(1));
                mod.textures.north = getTextureID(sb.getTextureByIndex(2));
                mod.textures.south = getTextureID(sb.getTextureByIndex(3));
                mod.textures.west = getTextureID(sb.getTextureByIndex(4));
                mod.textures.east = getTextureID(sb.getTextureByIndex(5));
                model = mod;
            }
            this.writeBlockModelFile(def.blockName + "_" + sb.meta, model);
        }
    }

}
