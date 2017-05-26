package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

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
            model = WesterosBlocks.MOD_ID + ":" + bn;
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
        for (Subblock sb : def.subBlocks) {
            addNLSString("tile." + def.blockName + "_" + sb.meta + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            String bn = def.blockName + "_" + sb.meta + "_lit";
            so.variants.put(String.format("facing=north,lit=true,variant=%d", sb.meta), new Variant(bn, 0));
            so.variants.put(String.format("facing=south,lit=true,variant=%d", sb.meta), new Variant(bn, 180));
            so.variants.put(String.format("facing=west,lit=true,variant=%d", sb.meta), new Variant(bn, 270));
            so.variants.put(String.format("facing=east,lit=true,variant=%d", sb.meta), new Variant(bn, 90));
            bn = def.blockName + "_" + sb.meta;
            so.variants.put(String.format("facing=north,lit=false,variant=%d", sb.meta), new Variant(bn, 0));
            so.variants.put(String.format("facing=south,lit=false,variant=%d", sb.meta), new Variant(bn, 180));
            so.variants.put(String.format("facing=west,lit=false,variant=%d", sb.meta), new Variant(bn, 270));
            so.variants.put(String.format("facing=east,lit=false,variant=%d", sb.meta), new Variant(bn, 90));
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            boolean isTinted = sb.isTinted(def);
            ModelObjectCubeAll mod = new ModelObjectCubeAll();
            mod.textures.top = getTextureID(sb.getTextureByIndex(1)); 
            mod.textures.side = getTextureID(sb.getTextureByIndex(2)); 
            mod.textures.front = getTextureID(sb.getTextureByIndex(3)); // ON
            if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/orientable";
            this.writeBlockModelFile(def.blockName + "_" + sb.meta + "_lit", mod);
            
            mod = new ModelObjectCubeAll();
            mod.textures.top = getTextureID(sb.getTextureByIndex(1)); 
            mod.textures.side = getTextureID(sb.getTextureByIndex(2)); 
            mod.textures.front = getTextureID(sb.getTextureByIndex(4)); // Off
            if (isTinted) mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/orientable";
            this.writeBlockModelFile(def.blockName + "_" + sb.meta, mod);
            // Build simple item model that refers to block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_" + sb.meta;
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);
        }
    }

}
