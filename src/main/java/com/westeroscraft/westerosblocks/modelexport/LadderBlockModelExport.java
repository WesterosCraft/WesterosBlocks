package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

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
        for (Subblock sb : def.subBlocks) {
            addNLSString("tile." + def.blockName + "_" + sb.meta + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
            Variant varn = new Variant();
            Variant vars = new Variant();
            Variant vare = new Variant();
            Variant varw = new Variant();
            String mod = WesterosBlocks.MOD_ID + ":" + def.blockName + "_" + sb.meta;
            varn.model = vars.model = vare.model = varw.model = mod;
            vare.y = 90;
            vars.y = 180;
            varw.y = 270;
            so.variants.put(String.format("facing=north,variant=%d", sb.meta), varn);
            so.variants.put(String.format("facing=east,variant=%d", sb.meta), vare);
            so.variants.put(String.format("facing=south,variant=%d", sb.meta), vars);
            so.variants.put(String.format("facing=west,variant=%d", sb.meta), varw);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            ModelObjectLadder mod = new ModelObjectLadder();
            mod.textures.ladder = getTextureID(sb.getTextureByIndex(0));
            this.writeBlockModelFile(def.blockName + "_" + sb.meta, mod);
            // Build simple item model that refers to block model
            ModelObject mo = new ModelObject();
            mo.parent = WesterosBlocks.MOD_ID + ":block/" + def.blockName + "_" + sb.meta;
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);
        }
    }
}
