package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class DoorBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model = "";
        public Integer x;
        public Integer y;
        public Variant(String blkname, String ext) {
            this(blkname, ext, 0, 0);
        }
        public Variant(String blkname, String ext, int yrot) {
            this(blkname, ext, 0, yrot);
        }
        public Variant(String blkname, String ext, int xrot, int yrot) {
            model = WesterosBlocks.MOD_ID + ":block/generated/" + blkname + "_" + ext;
            if (xrot != 0)
                x = xrot;
            if (yrot != 0)
                y = yrot;
        }
    }
    // Template objects for Gson export of block models
    public static class ModelObjectBottom {
        public String parent = "minecraft:block/door_bottom";    // Use 'door_bottom' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectBottomRH {
        public String parent = "minecraft:block/door_bottom_rh";    // Use 'door_bottom_rh' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectTop {
        public String parent = "minecraft:block/door_top";    // Use 'door_top' model
        public Texture textures = new Texture();
    }
    public static class ModelObjectTopRH {
        public String parent = "minecraft:block/door_top_rh";    // Use 'door_top_rh' model
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String bottom, top;
    }
    public static class TextureLayer0 {
        public String layer0;
    }
    public static class ModelObject {
        public String parent = "item/generated";
        public TextureLayer0 textures = new TextureLayer0();
    }

    public DoorBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        String bn = def.blockName;
        so.variants.put("facing=east,half=lower,hinge=left,open=false", new Variant(bn, "bottom"));
        so.variants.put("facing=south,half=lower,hinge=left,open=false", new Variant(bn, "bottom", 90));
        so.variants.put("facing=west,half=lower,hinge=left,open=false", new Variant(bn, "bottom", 180));
        so.variants.put("facing=north,half=lower,hinge=left,open=false", new Variant(bn, "bottom", 270));
        so.variants.put("facing=east,half=lower,hinge=right,open=false", new Variant(bn, "bottom_rh"));
        so.variants.put("facing=south,half=lower,hinge=right,open=false", new Variant(bn, "bottom_rh", 90));
        so.variants.put("facing=west,half=lower,hinge=right,open=false", new Variant(bn, "bottom_rh", 180));
        so.variants.put("facing=north,half=lower,hinge=right,open=false", new Variant(bn, "bottom_rh", 270));
        so.variants.put("facing=east,half=lower,hinge=left,open=true", new Variant(bn, "bottom_rh", 90));
        so.variants.put("facing=south,half=lower,hinge=left,open=true", new Variant(bn, "bottom_rh", 180));
        so.variants.put("facing=west,half=lower,hinge=left,open=true", new Variant(bn, "bottom_rh", 270));
        so.variants.put("facing=north,half=lower,hinge=left,open=true", new Variant(bn, "bottom_rh"));
        so.variants.put("facing=east,half=lower,hinge=right,open=true", new Variant(bn, "bottom", 270));
        so.variants.put("facing=south,half=lower,hinge=right,open=true", new Variant(bn, "bottom"));
        so.variants.put("facing=west,half=lower,hinge=right,open=true", new Variant(bn, "bottom", 90));
        so.variants.put("facing=north,half=lower,hinge=right,open=true", new Variant(bn, "bottom", 180));
        so.variants.put("facing=east,half=upper,hinge=left,open=false", new Variant(bn, "top"));
        so.variants.put("facing=south,half=upper,hinge=left,open=false", new Variant(bn, "top", 90));
        so.variants.put("facing=west,half=upper,hinge=left,open=false", new Variant(bn, "top", 180));
        so.variants.put("facing=north,half=upper,hinge=left,open=false", new Variant(bn, "top", 270));
        so.variants.put("facing=east,half=upper,hinge=right,open=false", new Variant(bn, "top_rh"));
        so.variants.put("facing=south,half=upper,hinge=right,open=false", new Variant(bn, "top_rh", 90));
        so.variants.put("facing=west,half=upper,hinge=right,open=false", new Variant(bn, "top_rh", 180));
        so.variants.put("facing=north,half=upper,hinge=right,open=false", new Variant(bn, "top_rh", 270));
        so.variants.put("facing=east,half=upper,hinge=left,open=true", new Variant(bn, "top_rh", 90));
        so.variants.put("facing=south,half=upper,hinge=left,open=true", new Variant(bn, "top_rh", 180));
        so.variants.put("facing=west,half=upper,hinge=left,open=true", new Variant(bn, "top_rh", 270));
        so.variants.put("facing=north,half=upper,hinge=left,open=true", new Variant(bn, "top_rh"));
        so.variants.put("facing=east,half=upper,hinge=right,open=true", new Variant(bn, "top", 270));
        so.variants.put("facing=south,half=upper,hinge=right,open=true", new Variant(bn, "top"));
        so.variants.put("facing=west,half=upper,hinge=right,open=true", new Variant(bn, "top", 90));
        so.variants.put("facing=north,half=upper,hinge=right,open=true", new Variant(bn, "top", 180));
        
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        String downtxt = getTextureID(def.getTextureByIndex(1));
        String uptxt = getTextureID(def.getTextureByIndex(0));
        // Bottom
        ModelObjectBottom bot = new ModelObjectBottom();
        bot.textures.bottom = downtxt;
        bot.textures.top = uptxt;
        this.writeBlockModelFile(def.blockName + "_bottom", bot);
        // Bottom RH
        ModelObjectBottomRH brh = new ModelObjectBottomRH();
        brh.textures.bottom = downtxt;
        brh.textures.top = uptxt;
        this.writeBlockModelFile(def.blockName + "_bottom_rh", brh);
        // Top
        ModelObjectTop top = new ModelObjectTop();
        top.textures.bottom = downtxt;
        top.textures.top = uptxt;
        this.writeBlockModelFile(def.blockName + "_top", top);
        // Top RH
        ModelObjectTopRH trh = new ModelObjectTopRH();
        trh.textures.bottom = downtxt;
        trh.textures.top = uptxt;
        this.writeBlockModelFile(def.blockName + "_top_rh", trh);
        // Build simple item model that refers to base block model
        ModelObject mo = new ModelObject();
        mo.textures.layer0 = uptxt;
        this.writeItemModelFile(def.blockName, mo);
    }
}

