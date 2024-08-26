package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCSlabBlock;

import net.minecraft.world.level.block.Block;

public class SlabBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public String parent;
        public Texture textures = new Texture();
        public ModelObjectCube(boolean ambientocclusion, boolean tinted, boolean overlay) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tinted/cube_overlay" : "westerosblocks:block/tinted/cube";
                } 
                else {
                    parent = overlay ? "westerosblocks:block/untinted/cube_overlay" : "minecraft:block/cube";
                }
            }
            else {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tintednoocclusion/cube_overlay" : "westerosblocks:block/tintednoocclusion/cube";
                }
                else {
                    parent = overlay ? "westerosblocks:block/noocclusion/cube_overlay" : "westerosblocks:block/noocclusion/cube";
                }
            }
        }
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
        public String down_ov, up_ov, north_ov, south_ov, west_ov, east_ov;
    }
    public static class ModelObjectHalfLower {
        public String parent;
        public Texture textures = new Texture();
        public ModelObjectHalfLower(boolean ambientocclusion, boolean tinted, boolean overlay) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tinted/half_slab_overlay" : "westerosblocks:block/tinted/half_slab";
                } 
                else {
                    parent = overlay ? "westerosblocks:block/untinted/half_slab_overlay" : "westerosblocks:block/untinted/half_slab";
                }
            }
            else {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tintednoocclusion/half_slab_overlay" : "westerosblocks:block/tintednoocclusion/half_slab";
                }
                else {
                    parent = overlay ? "westerosblocks:block/noocclusion/half_slab_overlay" : "westerosblocks:block/noocclusion/half_slab";
                }
            }
        }
    }
    public static class ModelObjectHalfUpper {
        public String parent;
        public Texture textures = new Texture();
        public ModelObjectHalfUpper(boolean ambientocclusion, boolean tinted, boolean overlay) {
            if (ambientocclusion) {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tinted/upper_slab_overlay" : "westerosblocks:block/tinted/upper_slab";
                } 
                else {
                    parent = overlay ? "westerosblocks:block/untinted/upper_slab_overlay" : "westerosblocks:block/untinted/upper_slab";
                }
            }
            else {
                if (tinted) {
                    parent = overlay ? "westerosblocks:block/tintednoocclusion/upper_slab_overlay" : "westerosblocks:block/tintednoocclusion/upper_slab";
                }
                else {
                    parent = overlay ? "westerosblocks:block/noocclusion/upper_slab_overlay" : "westerosblocks:block/noocclusion/upper_slab";
                }
            }
        }
    }
    public static class TextureSlab {
        public String bottom, top, side, particle;
    }
    public static class ModelObject {
    	public String parent;
    }

    private final WCSlabBlock sblk;
    
    public SlabBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        sblk = (WCSlabBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

        for (WesterosBlockStateRecord sr : def.states) {
			boolean justBase = sr.stateID == null;
			Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
                WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);
                // Do state for top half block
                Variant varu = new Variant();
                varu.model = (justBase) ? WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("top", setidx) : 
                                         WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("top", setidx, sr.stateID);
                varu.weight = set.weight;
                so.addVariant("type=top", varu, stateIDs);
                // Do bottom half slab
                Variant varl = new Variant();
                varl.model = (justBase) ? WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("bottom", setidx) : 
                                         WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("bottom", setidx, sr.stateID);
                varl.weight = set.weight;
                so.addVariant("type=bottom", varl, stateIDs);
                // Do full slab
                Variant var = new Variant();
                var.model = (justBase) ? WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("double", setidx) : 
                                         WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("double", setidx, sr.stateID);
                var.weight = set.weight;
                so.addVariant("type=double", var, stateIDs);
            }
        }
        this.writeBlockStateFile(def.getBlockName(), so);
    }

	protected void doSlabModel(boolean isOccluded, boolean isTinted, boolean isOverlay, int setidx, WesterosBlockStateRecord sr, int sridx, String cond) throws IOException {
		WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setidx);

        // Double block model
        ModelObjectCube mod = new ModelObjectCube(isOccluded, isTinted, isOverlay);
        mod.textures.down = getTextureID(set.getTextureByIndex(0));
        mod.textures.up = getTextureID(set.getTextureByIndex(1));
        mod.textures.north = getTextureID(set.getTextureByIndex(2));
        mod.textures.south = getTextureID(set.getTextureByIndex(3));
        mod.textures.west = getTextureID(set.getTextureByIndex(4));
        mod.textures.east = getTextureID(set.getTextureByIndex(5));
        mod.textures.particle = getTextureID(set.getTextureByIndex(2));
        if (isOverlay) {
            mod.textures.down_ov = getTextureID(sr.getOverlayTextureByIndex(0));
            mod.textures.up_ov = getTextureID(sr.getOverlayTextureByIndex(1));
            mod.textures.north_ov = getTextureID(sr.getOverlayTextureByIndex(2));
            mod.textures.south_ov = getTextureID(sr.getOverlayTextureByIndex(3));
            mod.textures.west_ov = getTextureID(sr.getOverlayTextureByIndex(4));
            mod.textures.east_ov = getTextureID(sr.getOverlayTextureByIndex(5));
        }
        this.writeBlockModelFile(getModelName("double", setidx, cond), mod);
        // Lower half block model
        ModelObjectHalfLower modl = new ModelObjectHalfLower(isOccluded, isTinted, isOverlay);
        modl.textures.down = getTextureID(set.getTextureByIndex(0));
        modl.textures.up = getTextureID(set.getTextureByIndex(1));
        modl.textures.north = getTextureID(set.getTextureByIndex(2));
        modl.textures.south = getTextureID(set.getTextureByIndex(3));
        modl.textures.west = getTextureID(set.getTextureByIndex(4));
        modl.textures.east = getTextureID(set.getTextureByIndex(5));
        modl.textures.particle = getTextureID(set.getTextureByIndex(2));
        if (isOverlay) {
            modl.textures.down_ov = getTextureID(sr.getOverlayTextureByIndex(0));
            modl.textures.up_ov = getTextureID(sr.getOverlayTextureByIndex(1));
            modl.textures.north_ov = getTextureID(sr.getOverlayTextureByIndex(2));
            modl.textures.south_ov = getTextureID(sr.getOverlayTextureByIndex(3));
            modl.textures.west_ov = getTextureID(sr.getOverlayTextureByIndex(4));
            modl.textures.east_ov = getTextureID(sr.getOverlayTextureByIndex(5));
        }
        this.writeBlockModelFile(getModelName("bottom", setidx, cond), modl);
        // Upper half block model
        ModelObjectHalfUpper modu = new ModelObjectHalfUpper(isOccluded, isTinted, isOverlay);
        modu.textures.down = getTextureID(set.getTextureByIndex(0));
        modu.textures.up = getTextureID(set.getTextureByIndex(1));
        modu.textures.north = getTextureID(set.getTextureByIndex(2));
        modu.textures.south = getTextureID(set.getTextureByIndex(3));
        modu.textures.west = getTextureID(set.getTextureByIndex(4));
        modu.textures.east = getTextureID(set.getTextureByIndex(5));
        modu.textures.particle = getTextureID(set.getTextureByIndex(2));
        if (isOverlay) {
            modu.textures.down_ov = getTextureID(sr.getOverlayTextureByIndex(0));
            modu.textures.up_ov = getTextureID(sr.getOverlayTextureByIndex(1));
            modu.textures.north_ov = getTextureID(sr.getOverlayTextureByIndex(2));
            modu.textures.south_ov = getTextureID(sr.getOverlayTextureByIndex(3));
            modu.textures.west_ov = getTextureID(sr.getOverlayTextureByIndex(4));
            modu.textures.east_ov = getTextureID(sr.getOverlayTextureByIndex(5));
        }
        this.writeBlockModelFile(getModelName("top", setidx, cond), modu);
    }

    @Override
    public void doModelExports() throws IOException {
        boolean isOccluded = (def.ambientOcclusion != null) ? def.ambientOcclusion : true;
		for (int idx = 0; idx < def.states.size(); idx++) {
			WesterosBlockStateRecord rec = def.states.get(idx);
            boolean isTinted = rec.isTinted();
            boolean isOverlay = rec.getOverlayTextureByIndex(0) != null;
            String cond = rec.stateID;
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < rec.getRandomTextureSetCount(); setidx++) {
                doSlabModel(isOccluded, isTinted, isOverlay, setidx, rec, idx, cond);
            }
        }
        
        // Build simple item model that refers to lower block model
        ModelObject mo = new ModelObject();
        WesterosBlockStateRecord sr0 = def.states.get(0);
        boolean isTinted = sr0.isTinted();
        String cond = sr0.stateID;
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("bottom", 0, cond);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Handle tint resources
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
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
    	newstate.put("waterlogged", "false");
    	// Bottom half
    	oldstate.put("half", "bottom");
    	newstate.put("type", "bottom");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
       	// Top half
       	oldstate.put("half", "top");
    	newstate.put("type", "top");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
       	// Double slab
    	oldstate.remove("half");
    	newstate.put("type", "double");
        if (sblk != null && sblk.connectstate) {
			newstate.put("connectstate", "0");
		}
        addWorldConverterRecord(oldID + "_2", oldstate, def.getBlockName(), newstate);
    }

}
