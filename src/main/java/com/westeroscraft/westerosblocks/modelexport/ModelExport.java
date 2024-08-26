package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westeroscraft.westerosblocks.WesterosBlockConfig;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockTags;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public abstract class ModelExport {
    private static File destdir;
    private static File blockstatedir;
    private static File blockmodeldir;
    private static File itemmodeldir;
    private static boolean didInit = false;

    // Common state lists
    public static final String[] FACING = {  "north", "south", "east", "west" };
    public static final String[] BOOLEAN = {  "true", "false" };
    public static final String[] TOPBOTTOM = {  "top", "bottom" };
    public static final String[] UPPERLOWER = {  "upper", "lower" };
    public static final String[] LEFTRIGHT = {  "left", "right" };
    public static final String[] ALLFACING = {  "north", "south", "east", "west", "up", "down" };
    public static final String[] UPFACING = {  "north", "south", "east", "west", "up" };
    public static final String[] HEADFOOT = { "head", "foot" };
    public static final String[] AGE15 = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" };
    public static final String[] DISTANCE7 = { "0", "1", "2", "3", "4", "5", "6", "7" };
    public static final String[] BITES7 = { "0", "1", "2", "3", "4", "5", "6", "7" };
    public static final String[] SHAPE5 = { "straight", "inner_right", "inner_left", "outer_right", "outer_left" };
    public static final String[] AXIS = { "x", "y", "z" };
    public static final String[] FACINGNE = {  "north", "east" };
    public static final String[] RAILSHAPE = { "north_south", "east_west", "ascending_east", "ascending_west", "ascending_north", "ascending_south","south_east", "south_west", "north_west", "north_east" };

    // Test for WB standard 'all clear' texture
    public boolean isTransparentTexture(String txt) {
    	return txt.equals("transparent");
    }
    
    public static void doInit(File dest) {
    	if (!didInit) {
            destdir = dest;
            blockstatedir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/blockstates");
            blockstatedir.mkdirs();
            blockmodeldir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/models/block/generated");
            blockmodeldir.mkdirs();
            itemmodeldir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/models/item");
            itemmodeldir.mkdirs();
    		didInit = true;
    	}
    }
    
    protected final Block block;
    protected final WesterosBlockDef def;
    public ModelExport(Block block, WesterosBlockDef def, File dest) {
    	doInit(dest);
    	this.block = block;
    	this.def = def;
    }
    public void writeBlockStateFile(String blockname, Object obj) throws IOException {
        File f = new File(blockstatedir, blockname + ".json");
        f.getParentFile().mkdirs();
        FileWriter fos = null;
        try {
            fos = new FileWriter(f);
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(obj, fos);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    
    protected String getModelName(String ext, int setidx) {
    	return def.blockName + "/" + ext + ("_v" + (setidx+1));
    }

    protected String getModelName(String ext, int setidx, String cond) {
			if (cond == null)
				return getModelName(ext, setidx);
    	return def.blockName + "/" + cond + "/" + ext + ("_v" + (setidx+1));
    }

    public void writeBlockModelFile(String model, Object obj) throws IOException {
        File f = new File(blockmodeldir, model + ".json");
        f.getParentFile().mkdirs();
        FileWriter fos = null;
        try {
            fos = new FileWriter(f);
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(obj, fos);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    public void writeItemModelFile(String model, Object obj) throws IOException {
        File f = new File(itemmodeldir, model + ".json");
        f.getParentFile().mkdirs();
        FileWriter fos = null;
        try {
            fos = new FileWriter(f);
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(obj, fos);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    public static String getTextureID(String id) {
        if (id.indexOf(':') >= 0) {
            return id;
        }
        else {
            return WesterosBlocks.MOD_ID + ":block/" + id;
        }
    }
    public abstract void doBlockStateExport() throws IOException;
    public abstract void doModelExports() throws IOException;
    public void doWorldConverterMigrate() throws IOException {
    	WesterosBlocks.log.info("No WorldConverter support for " + def.blockType);
    }

    private static HashMap<String, String> nls = new HashMap<String, String>();
    public static void addNLSString(String id, String val) {
        nls.put(id, val);
    }
    public static void writeNLSFile(Path dest) throws IOException {
        File tgt = new File(dest.toFile(), "assets/" + WesterosBlocks.MOD_ID + "/lang");
        tgt.mkdirs();
        FileWriter fos = null;
        try {
            fos = new FileWriter(new File(tgt, "en_us.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();                
            gson.toJson(nls, fos);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    
    private static LinkedList<String> wcList = new LinkedList<String>();
    private static LinkedList<String> itmMapList = new LinkedList<String>();
    
    public static void addWorldConverterRecord(String oldBlockName, Map<String, String> oldBlockState, String newBlockName, Map<String, String> newBlockState) {
    	if (oldBlockName.indexOf(':') < 0) oldBlockName = WesterosBlocks.MOD_ID + ":" + oldBlockName;
    	if (newBlockName.indexOf(':') < 0) newBlockName = WesterosBlocks.MOD_ID + ":" + newBlockName;
    	StringBuilder sb = new StringBuilder(oldBlockName);
    	if ((oldBlockState != null) && (oldBlockState.size() > 0)) {
    		sb.append('[');
    		boolean first = true;
    		TreeSet<String> keys = new TreeSet<String>(oldBlockState.keySet());
    		for (String key : keys) {
    			if (!first) sb.append(',');
    			sb.append(key).append("=").append(oldBlockState.get(key));
    			first = false;
    		}
    		sb.append(']');    		
    	}
    	sb.append(" -> ");
    	sb.append(newBlockName);
    	if ((newBlockState != null) && (newBlockState.size() > 0)) {
    		sb.append('[');
    		boolean first = true;
    		TreeSet<String> keys = new TreeSet<String>(newBlockState.keySet());
    		for (String key : keys) {
    			if (!first) sb.append(',');
    			sb.append(key).append("=").append(newBlockState.get(key));
    			first = false;
    		}
    		sb.append(']');    		
    	}
    	wcList.add(sb.toString());
    }
    public static void addWorldConverterComment(String txt) {
    	//wcList.add("# " + txt);
    }
    public static void addWorldConverterItemMap(String src, String dest) {
    	String [] tokens = src.split(":");
    	if (tokens.length == 1) {
    		src = WesterosBlocks.MOD_ID + ":" + tokens[0] + ":0";
    	}
    	else if (tokens.length == 2) {
    		if (tokens[0].equals("minecraft")) {
    			src = "minecraft:" + tokens[1] + ":0";
    		}
    		else {
    			src = WesterosBlocks.MOD_ID + ":" + tokens[0] + ":" + tokens[1];
    		}
    	}
    	else if (tokens.length == 3) {
    		src = tokens[0] + ":" + tokens[1] + ":" + tokens[2];
    	}
//    	itmMapList.add(String.format("%s -> %s:%s", src, WesterosBlocks.MOD_ID, dest));
    	itmMapList.add(String.format("itemIDMapping.put(\"%s\", \"%s:%s\");", src, WesterosBlocks.MOD_ID, dest));
    }
    public static void writeWorldConverterFile(Path dest) throws IOException {
        FileWriter fos = null;
        try {
            fos = new FileWriter(new File(dest.toFile(), "blocks_1.12-1.18__westerosblocks.txt"));
            for (String line : wcList) {
            	fos.write(line + "\r\n");
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    public static void writeWorldConverterItemMapFile(Path dest) throws IOException {
        FileWriter fos = null;
        try {
            fos = new FileWriter(new File(dest.toFile(), "items_1.12-1.18__westerosblocks.txt"));
            for (String line : itmMapList) {
            	fos.write(line + "\r\n");
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    
    private static class TagFile {
    	boolean replace = false;
    	String[] values = {};
    };
    
    public static void writeTagDataFiles(Path dest) throws IOException {
        File tgt = new File(dest.toFile(), "data/minecraft/tags/blocks");
        tgt.mkdirs();
        HashMap<String, ArrayList<String>> blksByTag = new HashMap<String, ArrayList<String>>();
        // Load all the tags
        for (String blockName : WesterosBlocks.customBlocksByName.keySet()) {
        	Block blk = WesterosBlocks.customBlocksByName.get(blockName);
        	if (blk instanceof WesterosBlockLifecycle) {
        		WesterosBlockLifecycle wb = (WesterosBlockLifecycle) blk;
        		String[] tags = wb.getBlockTags();	// Get block tags
        		for (String tag : tags) {
        			ArrayList<String> lst = blksByTag.get(tag.toLowerCase());
        			if (lst == null) {
        				lst = new ArrayList<String>();
        				blksByTag.put(tag.toLowerCase(), lst);
        			}
        			lst.add(WesterosBlocks.MOD_ID + ":" + blockName);
        		}
        	}
        }
        // And write the files for each
        for (String tagID : blksByTag.keySet()) {
			ArrayList<String> lst = blksByTag.get(tagID);
	        FileWriter fos = null;
	        try {
	        	TagFile tf = new TagFile();
	        	tf.values = lst.toArray(new String[lst.size()]);
	            fos = new FileWriter(new File(tgt, tagID + ".json"));
	            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();                
	            gson.toJson(tf, fos);
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }			
        }
    }
    
    public static void writeCustomTagDataFiles(Path dest, WesterosBlockConfig cfg) throws IOException {
        File tgt = new File(dest.toFile(), "data/" + WesterosBlocks.MOD_ID + "/tags/blocks");
        tgt.mkdirs();
        HashMap<String, ArrayList<String>> blksByTag = new HashMap<String, ArrayList<String>>();
        // Declare custom tags - must be record for each
        if (cfg.blockTags != null) {
        	for (WesterosBlockTags tag : cfg.blockTags) {
        		String tagid = tag.customTag.toLowerCase();
    			ArrayList<String> lst = blksByTag.get(tagid);
    			if (lst == null) {
    				lst = new ArrayList<String>();
    				blksByTag.put(tagid, lst);
    			}
    			for (String id : tag.blockNames) {
    				lst.add(id.toLowerCase());
    			}
        	}
        }
        // Load all the custom tags from blocks
        for (String blockName : WesterosBlocks.customBlocksByName.keySet()) {
        	Block blk = WesterosBlocks.customBlocksByName.get(blockName);
        	if (blk instanceof WesterosBlockLifecycle) {
        		WesterosBlockLifecycle wb = (WesterosBlockLifecycle) blk;
        		WesterosBlockDef def = wb.getWBDefinition();
        		if (def.customTags != null) {
            		for (String tag : def.customTags) {
            			ArrayList<String> lst = blksByTag.get(tag.toLowerCase());
            			if (lst == null) {
            				WesterosBlocks.log.error("Invalid customTag " + tag + " on block " + blockName);
            				continue;
            			}
            			lst.add(WesterosBlocks.MOD_ID + ":" + blockName);
            		}        			
        		}
        	}
        }
        // And write the files for each
        for (String tagID : blksByTag.keySet()) {
			ArrayList<String> lst = blksByTag.get(tagID);
	        FileWriter fos = null;
	        try {
	        	TagFile tf = new TagFile();
	        	tf.values = lst.toArray(new String[lst.size()]);
	            fos = new FileWriter(new File(tgt, tagID + ".json"));
	            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();                
	            gson.toJson(tf, fos);
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }			
        }
    }
    public static List<TagKey<Block>> customTags;
    
    public static void declareCustomTags(WesterosBlockConfig cfg) {
        // Declare custom tags - must be record for each
        if (cfg.blockTags != null) {
        	customTags = new ArrayList<TagKey<Block>>();
        	for (WesterosBlockTags tag : cfg.blockTags) {
        		String tagid = tag.customTag.toLowerCase();
        		customTags.add(BlockTags.create(new ResourceLocation(WesterosBlocks.MOD_ID, tagid)));
        	}
        }	
    }
    private static class TintOver {
        String cond;
        String txt;
    }
    private static Map<String, List<TintOver>> tintoverrides = new HashMap<String, List<TintOver>>();
    
    public static void addTintingOverride(String blockname, String cond, String txtfile) {
        List<TintOver> blst = tintoverrides.get(blockname);
        if (blst == null) {
            blst = new ArrayList<TintOver>();
            tintoverrides.put(blockname, blst);
        }
        TintOver to = new TintOver();
        to.cond = cond;
        to.txt = txtfile;
        blst.add(to);
    }
    public static void writeDynmapOverridesFile(Path dest) throws IOException {
        File tgt = new File(dest.toFile(), "assets/" + WesterosBlocks.MOD_ID + "/dynmap");
        tgt.mkdirs();
        PrintStream fw = null;
        try {
            fw = new PrintStream(new File(tgt, "blockstateoverrides.json"));
            fw.println("{");
            fw.println("  \"overrides\": {");
            fw.println("      \"" + WesterosBlocks.MOD_ID + "\": {");
            //TODO - add block state overrides
            fw.println("       }");
            fw.println("   },");
            fw.println("  \"tinting\": {");
            fw.println("      \"" + WesterosBlocks.MOD_ID + "\": {");
            boolean first1 = true;
            for (Entry<String, List<TintOver>> br : tintoverrides.entrySet()) {
                if (!first1) {
                    fw.println("          ,");
                }
                fw.println("          \"" + br.getKey() + "\": [");
                boolean first2 = true;
                for (TintOver toe : br.getValue()) {
                    if (!first2) {
                        fw.println("              ,");
                    }
                    if ((toe.cond != null) && (toe.cond.equals("") == false)) {
                        fw.println("              { \"state\": \"" + toe.cond + "\", \"colormap\": [ \"" + toe.txt + "\" ] }");
                    }
                    else {
                        fw.println("              { \"colormap\": [ \"" + toe.txt + "\" ] }");
                    }
                    first2 = false;
                }
                fw.println("          ]");
                first1 = false;
            }
            fw.println("       }");
            fw.println("   }");
            fw.println("}");
        } finally {
            if (fw != null) fw.close();
        }
    }

    // Template objects for Gson export of block state
    public static class StateObject {
      public Map<String, VarOrVarList> variants;
    	public List<States> multipart;
        
        public void addVariant(String cond, Variant v, Set<String> stateIDs) {
        	if (variants == null) {
        		 variants = new HashMap<String, VarOrVarList>();
        	}
        	ArrayList<String> conds = new ArrayList<String>();
        	if (stateIDs == null) {
        		conds.add(cond);
        	}
        	else {
        		for (String cval : stateIDs) {
        			conds.add(cond + ((cond.length() > 0) ? "," : "") + "state=" + cval);
        		}
        	}
        	// Add to all the matching condIDs
        	for (String condval : conds) {
		    	VarOrVarList existing = variants.get(condval);	// See if exists
		    	if (existing == null) {
		    		variants.put(condval, v);	// Add single
		    	}
		    	else if (existing instanceof Variant) {	// Only one?
		    		VariantList vlist = new VariantList();
		    		vlist.add((Variant) existing);	// Convert to list
		    		vlist.add(v);
		    		variants.put(condval, vlist);
		    	}
		    	else {
		    		((VariantList) existing).add(v);	// Append to list
		    	}
        	}
        }
        public void addStates(States st, Set<String> stateIDs) {
        	if (multipart == null) {
        		multipart = new ArrayList<States>();
        	}
        	if (stateIDs == null) {
        		multipart.add(st);
        	}
        	else {
        		for (String cond : stateIDs) {
        			States newst = new States(st, cond);
            	multipart.add(newst);        			
        		}
        	}
        }
        private void addState(WhenRec rec, Apply a, String cond) {
        	States matchst = null;
        	WhenRec wr = rec;
        	// Add condition to whenrec
        	if (cond != null) {
        		if (rec != null) {
        			wr = new WhenRec(rec, cond);
        		}
        		else {
        			wr = new WhenRec();
        			wr.state = cond;
        		}
        	}
        	for (States st : multipart) {
        		if (((st.when == null) && (wr == null)) ||
        			((st.when != null) && (wr != null) && st.when.equals(wr))) {
        			matchst = st;
        			break;
        		}
        	}
        	// Add new rec, if needed
        	if (matchst == null) {
        		matchst = new States();
        		matchst.when = wr;
        		multipart.add(matchst);
        	}
        	// Now add our apply
        	matchst.apply.add(a);
        }
        
        public void addStates(WhenRec rec, Apply a, Set<String> stateIDs) {
        	if (multipart == null) {
        		multipart = new ArrayList<States>();
        	}
        	if (stateIDs == null) {
        		addState(rec, a, null);
        	}
        	else {
        		for (String cond : stateIDs) {
            		addState(rec, a, cond);
        		}
        	}
        }
    }
    public static interface VarOrVarList {};
    
    public static class Variant implements VarOrVarList {
        public String model;
        public Integer x, y, z;
        public Integer weight;
        public Boolean uvlock;
    }
    public static class VariantList extends ArrayList<Variant> implements VarOrVarList {
		private static final long serialVersionUID = 1L;    	
    }
    public static class States {
    	public List<Apply> apply = new ArrayList<Apply>();
    	public WhenRec when;
    	public States() {}
    	public States(States orig, String cond) {
    		this.apply = orig.apply;
    		if (orig.when != null) {
    			this.when = new WhenRec(orig.when, cond);
    		}
    		else {
    			this.when = new WhenRec();
    			this.when.state = cond;
    		}
    	}
    }    
    public static class WhenRec {
    	String north, south, west, east, up, down, state;
    	public List<WhenRec> OR;
    	
    	public WhenRec() {}
    	public WhenRec(WhenRec orig, String cond) {
    		this.north = orig.north;
    		this.south = orig.south;
    		this.east = orig.east;
    		this.west = orig.west;
				this.up = orig.up;
				this.down = orig.down;
				this.OR = orig.OR;
				this.state = cond;
    	}
    	public void addOR(WhenRec r) {
    		if (OR == null) OR = new ArrayList<WhenRec>();
    		OR.add(r);
    	}
    	private boolean isSame(String a, String b) {
    		if ((a == null) && (b == null)) return true;
    		if ((a != null) && (b != null) && a.equals(b)) return true;
    		return false;
    	}
    	public boolean equals(Object o) {
    		if (this == o) return true;
    		if (o instanceof WhenRec) {
    			WhenRec or = (WhenRec)o;
    			if (isSame(or.north, this.north) &&
					isSame(or.south, this.south) &&
					isSame(or.east, this.east) &&
					isSame(or.west, this.west) &&
					isSame(or.up, this.up) &&
					isSame(or.down, this.down) &&
					isSame(or.state, this.state)) {
    				if ((or.OR == null) && (this.OR == null)) return true;
    				if ((or.OR != null) && (this.OR != null) && (this.OR.equals(or.OR))) return true;
    			}
    		}
    		return false;
    	}
    }
    public static class Apply {
    	String model;
    	Integer x;
    	Integer y;
    	Boolean uvlock;
    	Integer weight;
    }
}
