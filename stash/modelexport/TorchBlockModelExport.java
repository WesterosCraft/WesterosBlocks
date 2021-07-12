package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;

public class TorchBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer y;
        public Variant(String blkname) {
            model = WesterosBlocks.MOD_ID + ":" + blkname;
        }
        public Variant(String blkname, String ext, int yrot) {
            model = WesterosBlocks.MOD_ID + ":" + blkname + "_" + ext;
            if (yrot != 0)
                y = yrot;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectTorch {
        public String parent = "block/torch";    // Use 'torch' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectTorchWall {
        public String parent = "block/torch_wall";    // Use 'torch_wall' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String torch;
    }
    public static class ModelObject {
    	public String parent = "item/generated";
        public Texture0 textures = new Texture0();
    }
    public static class Texture0 {
        public String layer0;
    }

    
    public TorchBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("tile." + def.blockName + ".name", def.subBlocks.get(0).label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName;
        
        so.variants.put("facing=up", new Variant(bn));
        so.variants.put("facing=east", new Variant(bn, "wall", 0));
        so.variants.put("facing=south", new Variant(bn, "wall", 90));
        so.variants.put("facing=west", new Variant(bn, "wall", 180));
        so.variants.put("facing=north", new Variant(bn, "wall", 270));
        
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        Subblock sb = def.subBlocks.get(0);
        String txt = getTextureID(sb.getTextureByIndex(0));
        ModelObjectTorch mod = new ModelObjectTorch();
        mod.textures.torch = txt; 
        this.writeBlockModelFile(def.blockName, mod);
        
        ModelObjectTorchWall modw = new ModelObjectTorchWall();
        modw.textures.torch = txt; 
        this.writeBlockModelFile(def.blockName + "_wall", modw);
        
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = txt;
        this.writeItemModelFile(def.blockName, mo);
    }

}
