package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class LogBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer x;
        public Integer y;
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
    public static class ModelObjectColumn {
        public String parent = "westerosblocks:block/untinted/cube_log";    // Use hacked 'cube_column' model (weirwood behaviors...)
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String end, side;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public LogBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        Variant varx = new Variant();
        Variant vary = new Variant();
        Variant varz = new Variant();
        Variant varn = new Variant();
        String mod = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        varx.model = vary.model = varz.model = mod;
        so.variants.put("axis=y", vary);
        varz.x = 90;
        so.variants.put("axis=z", varz);
        varx.x = 90;
        varx.y = 90;
        so.variants.put("axis=x", varx);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        ModelObjectColumn mod = new ModelObjectColumn();
        mod.textures.end = getTextureID(def.getTextureByIndex(0));
        mod.textures.side = getTextureID(def.getTextureByIndex(1));
        this.writeBlockModelFile(def.blockName, mod);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);
    }

}
