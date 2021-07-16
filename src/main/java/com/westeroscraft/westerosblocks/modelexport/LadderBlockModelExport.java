package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class LadderBlockModelExport extends ModelExport {
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
    public static class ModelObjectLadder {
        public String parent = WesterosBlocks.MOD_ID + ":block/ladder";    // Use 'ladder' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String ladder;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public LadderBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        Variant varn = new Variant();
        Variant vars = new Variant();
        Variant vare = new Variant();
        Variant varw = new Variant();
        String mod = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        varn.model = vars.model = vare.model = varw.model = mod;
        vare.y = 90;
        vars.y = 180;
        varw.y = 270;
        so.variants.put("facing=north", varn);
        so.variants.put("facing=east", vare);
        so.variants.put("facing=south", vars);
        so.variants.put("facing=west", varw);
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        ModelObjectLadder mod = new ModelObjectLadder();
        mod.textures.ladder = getTextureID(def.getTextureByIndex(0));
        // Export if not set to custom model
        if (!def.isCustomModel())
        	this.writeBlockModelFile(def.blockName, mod);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName;
        this.writeItemModelFile(def.blockName, mo);
    }
}
