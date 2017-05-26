package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Enumeration;
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
}
