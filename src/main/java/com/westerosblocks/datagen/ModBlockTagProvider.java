package com.westerosblocks.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksJsonLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockLifecycle;
import com.westerosblocks.block.WesterosBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagFile;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        WesterosBlocksJsonLoader.WesterosBlocksConfig config = WesterosBlocks.getCustomConfig();
        HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
        HashMap<String, ArrayList<String>> blksByTag = new HashMap<>();

        if (config.blockTags != null) {
            for (WesterosBlockTags tag : config.blockTags) {
                for (String blockName : tag.blockNames) {
                    String namespace = blockName.split(":")[0];
                    TagKey<Block> CUSTOM_TAG_NAME = TagKey.of(RegistryKeys.BLOCK,
                            Identifier.of(namespace, tag.customTag));

                    getOrCreateTagBuilder(CUSTOM_TAG_NAME)
                            .add(Registries.BLOCK.get(Identifier.of(namespace, blockName.split(":")[1])));
                }
            }

            for (String blockName : customBlocks.keySet()) {
                Block blk = customBlocks.get(blockName);
                if (blk instanceof WesterosBlockLifecycle) {
                    WesterosBlockLifecycle wb = (WesterosBlockLifecycle) blk;
                    String[] tags = wb.getBlockTags();	// Get block tags
                    for (String tag : tags) {
                        ArrayList<String> lst = blksByTag.get(tag.toLowerCase());
                        if (lst == null) {
                            lst = new ArrayList<>();
                            blksByTag.put(tag.toLowerCase(), lst);
                        }
                        lst.add(WesterosBlocks.MOD_ID + ":" + blockName);
                    }
                }
            }

            //TODO
            for (String tagID : blksByTag.keySet()) {
                ArrayList<String> lst = blksByTag.get(tagID);

            }
        }



//        public static void writeTagDataFiles(Path dest) throws IOException {
//            File tgt = new File(dest.toFile(), "data/minecraft/tags/blocks");
//            tgt.mkdirs();
//            HashMap<String, ArrayList<String>> blksByTag = new HashMap<String, ArrayList<String>>();
//            // Load all the tags
//            for (String blockName : WesterosBlocks.customBlocksByName.keySet()) {
//                Block blk = WesterosBlocks.customBlocksByName.get(blockName);
//                if (blk instanceof WesterosBlockLifecycle) {
//                    WesterosBlockLifecycle wb = (WesterosBlockLifecycle) blk;
//                    String[] tags = wb.getBlockTags();	// Get block tags
//                    for (String tag : tags) {
//                        ArrayList<String> lst = blksByTag.get(tag.toLowerCase());
//                        if (lst == null) {
//                            lst = new ArrayList<String>();
//                            blksByTag.put(tag.toLowerCase(), lst);
//                        }
//                        lst.add(WesterosBlocks.MOD_ID + ":" + blockName);
//                    }
//                }
//            }
//            // And write the files for each
//            for (String tagID : blksByTag.keySet()) {
//                ArrayList<String> lst = blksByTag.get(tagID);
//                FileWriter fos = null;
//                try {
//                    TagFile tf = new TagFile();
//                    tf.values = lst.toArray(new String[lst.size()]);
//                    fos = new FileWriter(new File(tgt, tagID + ".json"));
//                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//                    gson.toJson(tf, fos);
//                } finally {
//                    if (fos != null) {
//                        fos.close();
//                    }
//                }
//            }
//        }



    }
}
