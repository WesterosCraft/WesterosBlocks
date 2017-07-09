package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
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

    private static Properties nls = new Properties();
    public static void addNLSString(String id, String val) {
        nls.put(id, val);
    }
    public static void writeNLSFile(File dest) throws IOException {
        File tgt = new File(dest, "assets/" + WesterosBlocks.MOD_ID + "/lang");
        tgt.mkdirs();
        PrintStream fw = null;
        try {
            fw = new PrintStream(new File(tgt, "en_US.lang"));
            Properties tmp = new Properties() {
				private static final long serialVersionUID = 1L;
				@Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            tmp.putAll(nls);
            tmp.store(fw, null);
        } finally {
            if (fw != null) fw.close();
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
    public static void writeDynmapOverridesFile(File dest) throws IOException {
        File tgt = new File(dest, "assets/" + WesterosBlocks.MOD_ID + "/dynmap");
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
