package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public abstract class ModelExport {
    private File destdir;
    private File blockstatedir;
    private File blockmodeldir;
    private Block block;
    
    public ModelExport(Block block, File dest) {
        this.block = block;
        this.destdir = dest;
        this.blockstatedir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/blockstates");
        this.blockstatedir.mkdirs();
        this.blockmodeldir = new File(destdir, "assets/" + WesterosBlocks.MOD_ID + "/models/block");
        this.blockmodeldir.mkdirs();
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
}
