package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCBedBlock;

import net.minecraft.world.level.block.Block;

public class BedBlockModelExport extends ModelExport {
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
    
    private WCBedBlock bblk;
    
    public BedBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        bblk = (WCBedBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    private static class ModelRec {
    	public String cond;
    	public String ext;
    	public int y;
    	ModelRec(String c, String x, int y) {
    		cond = c; ext = x;
    		this.y = y;
    	}
    };
    private static final ModelRec MODELS[] = {
		new ModelRec("facing=north,part=foot", "foot", 180),
		new ModelRec("facing=east,part=foot", "foot", 270),
		new ModelRec("facing=south,part=foot", "foot", 0),
		new ModelRec("facing=west,part=foot", "foot", 90),
		new ModelRec("facing=north,part=head", "head", 180),
		new ModelRec("facing=east,part=head", "head", 270),
		new ModelRec("facing=south,part=head", "head", 0),
		new ModelRec("facing=west,part=head", "head", 90)
    };
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        
        for (ModelRec rec : MODELS) {
        	Variant var = new Variant();
        	var.model =  WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(rec.ext, 0);
        	if (rec.y != 0) var.y = rec.y;
        	so.addVariant(rec.cond, var, null);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    private String getBaseModel(boolean head) {
    	if (bblk.bedType == WCBedBlock.BedType.RAISED)
    		return WesterosBlocks.MOD_ID + ":block/untinted/bed_raised" + (head ? "_head" : "_foot");
    	else if (bblk.bedType == WCBedBlock.BedType.HAMMOCK)
    		return WesterosBlocks.MOD_ID + ":block/untinted/bed_hammock" + (head ? "_head" : "_foot");
    	else
    		return WesterosBlocks.MOD_ID + ":block/untinted/bed" + (head ? "_head" : "_foot");    		
    }
    
    @Override
    public void doModelExports() throws IOException {
        ModelObjectBedHead mod = new ModelObjectBedHead();
        mod.parent = getBaseModel(true);
        mod.textures.bedtop = getTextureID(def.getTextureByIndex(0));
        mod.textures.bedend = getTextureID(def.getTextureByIndex(4));
        mod.textures.bedside = getTextureID(def.getTextureByIndex(2));
        this.writeBlockModelFile(getModelName("head", 0), mod);
        ModelObjectBedFoot modf = new ModelObjectBedFoot();
        modf.parent = getBaseModel(false);
        modf.textures.bedtop = getTextureID(def.getTextureByIndex(1));
        modf.textures.bedend = getTextureID(def.getTextureByIndex(5));
        modf.textures.bedside = getTextureID(def.getTextureByIndex(3));
        this.writeBlockModelFile(getModelName("foot", 0), modf);
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
    
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	String OCCUPIED[] = new String[] { "true", "false" };
    	for (String occupied : OCCUPIED) {
	    	oldstate.put("occupied",occupied);
	    	newstate.put("occupied",occupied);
	    	for (String facing : FACING) {
		    	oldstate.put("facing",facing);
		    	newstate.put("facing",facing);
		    	for (String part : HEADFOOT) {
			    	oldstate.put("part",part);
			    	newstate.put("part",part);
			        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
	    		}
	    	}
    	}
    }
}
