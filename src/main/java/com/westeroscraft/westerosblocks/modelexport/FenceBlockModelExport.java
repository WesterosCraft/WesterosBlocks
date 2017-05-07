package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;
import com.westeroscraft.westerosblocks.blocks.WCFenceBlock;
import com.westeroscraft.westerosblocks.blocks.WCPaneBlock;
import com.westeroscraft.westerosblocks.blocks.WCPlantBlock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;
import com.westeroscraft.westerosblocks.modelexport.PlantBlockModelExport.TextureLayer0;

import net.minecraft.block.Block;

public class FenceBlockModelExport extends ModelExport {
    private WCFenceBlock blk;
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
    	public List<States> multipart = new ArrayList<States>();
    }
    public static class States {
    	public WhenRec when = new WhenRec();
    	public Apply apply = new Apply();
    }
    public static class SideStates extends States {
    	SideStates() {
    		when.OR = new ArrayList<WhenRec>();
    	}
    }
    public static class WhenRec {
    	String variant;
    	String north, south, west, east;
    	public List<WhenRec> OR;
    }
    public static class Apply {
    	String model;
    	Integer x;
    	Integer y;
    	Boolean uvlock;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectPost {
        public String parent = "block/fence_post";    // Use 'fence_post' model for single texture
        public Texture textures = new Texture();
    }
    public static class ModelObjectSide {
        public String parent = "block/fence_side";    // Use 'fence_side' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String texture;
    }
    public static class ModelObject {
    	public String parent = "block/fence_inventory";
        public Texture textures = new Texture();
    }

    public FenceBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCFenceBlock) blk;
        this.def = def;
        for (Subblock sb : def.subBlocks) {
            addNLSString("tile." + def.blockName + "_" + sb.meta + ".name", sb.label);
        }
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        for (Subblock sb : def.subBlocks) {
        	// Add post based on our variant
        	States ps = new States();
        	ps.when.variant = Integer.toString(sb.meta);
        	ps.apply.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_post_" + sb.meta;
        	so.multipart.add(ps);
        	// Add north variant
        	SideStates ssn = new SideStates();
        	WhenRec wr = new WhenRec();
        	wr.variant = Integer.toString(sb.meta);
        	wr.north = "true";
        	ssn.when.OR.add(wr);
        	ssn.apply.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_side_" + sb.meta;
        	ssn.apply.uvlock = true;
        	so.multipart.add(ssn);
        	// Add east variant
        	ssn = new SideStates();
        	wr = new WhenRec();
        	wr.variant = Integer.toString(sb.meta);
        	wr.east = "true";
        	ssn.when.OR.add(wr);
        	ssn.apply.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_side_" + sb.meta;
        	ssn.apply.uvlock = true;
        	ssn.apply.y = 90;
        	so.multipart.add(ssn);
        	// Add south variant
            ssn = new SideStates();
            wr = new WhenRec();
            wr.variant = Integer.toString(sb.meta);
            wr.south = "true";
            ssn.when.OR.add(wr);
            ssn.apply.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_side_" + sb.meta;
            ssn.apply.uvlock = true;
            ssn.apply.y = 180;
            so.multipart.add(ssn);
        	// Add west variant
            ssn = new SideStates();
            wr = new WhenRec();
            wr.variant = Integer.toString(sb.meta);
            wr.west = "true";
            ssn.when.OR.add(wr);
            ssn.apply.model = WesterosBlocks.MOD_ID + ":" + def.blockName + "_side_" + sb.meta;
            ssn.apply.uvlock = true;
            ssn.apply.y = 270;
            so.multipart.add(ssn);
        }
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        for (Subblock sb : def.subBlocks) {
            ModelObjectPost mod = new ModelObjectPost();
            mod.textures.texture = getTextureID(sb.getTextureByIndex(0)); 
            this.writeBlockModelFile(def.blockName + "_post_" + sb.meta, mod);
            // Side model
            ModelObjectSide smod = new ModelObjectSide();
            smod.textures.texture = getTextureID(sb.getTextureByIndex(0)); 
            this.writeBlockModelFile(def.blockName + "_side_" + sb.meta, smod);
            // Build simple item model that refers to fence inventory model
            ModelObject mo = new ModelObject();
            mo.textures.texture = getTextureID(sb.getTextureByIndex(0));
            this.writeItemModelFile(def.blockName + "_" + sb.meta, mo);
        }
    }

}
