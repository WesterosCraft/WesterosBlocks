package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class BedBlockModelExport extends ModelExport {
    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer y;
        public Variant(String bn, String ext, int y) {
            model = bn + "_" + ext;
            if (y != 0)
                this.y = y;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectBedHead {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/bed_head";    // Use 'bed_head' model for single texture
        public Texture textures = new Texture();
    }
    // Template objects for Gson export of block models
    public static class ModelObjectBedFoot {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/bed_foot";    // Use 'bed_foot' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String bedtop;
        public String bedend;
        public String bedside;
        public String bedtop2;
        public String bedend2;
        public String bedside2;
    }
    public static class ModelObject {
        public String parent;
        public Texture textures = new Texture();
    }
    
    public BedBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = WesterosBlocks.MOD_ID + ":block/generated/" + def.blockName;
        
        so.variants.put("facing=north,part=foot", new Variant(bn, "foot", 180));
        so.variants.put("facing=east,part=foot", new Variant(bn, "foot", 270));
        so.variants.put("facing=south,part=foot", new Variant(bn, "foot", 0));
        so.variants.put("facing=west,part=foot", new Variant(bn, "foot", 90));
        so.variants.put("facing=north,part=head", new Variant(bn, "head", 180));
        so.variants.put("facing=east,part=head", new Variant(bn, "head", 270));
        so.variants.put("facing=south,part=head", new Variant(bn, "head", 0));
        so.variants.put("facing=west,part=head", new Variant(bn, "head", 90));

        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        ModelObjectBedHead mod = new ModelObjectBedHead();
        mod.textures.bedtop = getTextureID(def.getTextureByIndex(0));
        mod.textures.bedend = getTextureID(def.getTextureByIndex(4));
        mod.textures.bedside = getTextureID(def.getTextureByIndex(2));
        this.writeBlockModelFile(def.blockName + "_head", mod);
        ModelObjectBedFoot modf = new ModelObjectBedFoot();
        modf.textures.bedtop = getTextureID(def.getTextureByIndex(1));
        modf.textures.bedend = getTextureID(def.getTextureByIndex(5));
        modf.textures.bedside = getTextureID(def.getTextureByIndex(3));
        this.writeBlockModelFile(def.blockName + "_foot", modf);
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":item/untinted/bed_item";
        mo.textures.bedtop = mod.textures.bedtop;
        mo.textures.bedend = mod.textures.bedend;
        mo.textures.bedside = mod.textures.bedside;
        mo.textures.bedtop2 = modf.textures.bedtop;
        mo.textures.bedend2 = modf.textures.bedend;
        mo.textures.bedside2 = modf.textures.bedside;
        this.writeItemModelFile(def.blockName, mo);
    }
}
