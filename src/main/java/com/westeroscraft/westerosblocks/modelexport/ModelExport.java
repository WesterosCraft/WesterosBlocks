package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public abstract class ModelExport {
    private File destdir;
    private File blockstatedir;
    private File blockmodeldir;
    private File itemmodeldir;
    
    public ModelExport(Block block, WesterosBlockDef def, File dest) {
        this.destdir = dest;
        this.blockstatedir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/blockstates");
        this.blockstatedir.mkdirs();
        this.blockmodeldir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/models/block");
        this.blockmodeldir.mkdirs();
        this.itemmodeldir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/models/item");
        this.itemmodeldir.mkdirs();
    }
    public void writeBlockStateFile(String blockname, Object obj) throws IOException {
        File f = new File(blockstatedir, blockname + ".json");
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
    public void writeBlockModelFile(String model, Object obj) throws IOException {
        File f = new File(blockmodeldir, model + ".json");
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
            return WesterosBlocks.MOD_ID + ":blocks/" + id;
        }
    }
    public abstract void doBlockStateExport() throws IOException;
    public abstract void doModelExports() throws IOException;

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

}
