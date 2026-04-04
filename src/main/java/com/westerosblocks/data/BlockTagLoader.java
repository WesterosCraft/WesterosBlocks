package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlockTagLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;

    public BlockTagLoader(String filePath) {
        this.filePath = filePath;
    }

    public BlockTagDefinition loadDefinition() {
        WesterosBlocks.LOGGER.info("Loading block tags from: {}", filePath);

        try {
            ModContainer container = FabricLoader.getInstance().getModContainer(WesterosBlocks.MOD_ID).orElse(null);
            if (container == null) {
                WesterosBlocks.LOGGER.error("Could not find mod container for {}", WesterosBlocks.MOD_ID);
                return null;
            }

            for (Path rootPath : container.getRootPaths()) {
                String pathWithoutSlash = filePath.startsWith("/") ? filePath.substring(1) : filePath;
                Path resolved = rootPath.resolve(pathWithoutSlash);

                if (Files.exists(resolved) && Files.isRegularFile(resolved)) {
                    String content = Files.readString(resolved);
                    BlockTagDefinition definition = GSON.fromJson(content, BlockTagDefinition.class);
                    if (definition != null && definition.hasBlockTags()) {
                        WesterosBlocks.LOGGER.info("Loaded {} block tag definitions", definition.getBlockTags().size());
                        return definition;
                    }
                }
            }

            WesterosBlocks.LOGGER.warn("Block tags file not found at: {}", filePath);
        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to read block tags file: {}", filePath, e);
        } catch (JsonSyntaxException e) {
            WesterosBlocks.LOGGER.error("Invalid JSON in block tags file: {}", filePath, e);
        }

        return null;
    }
}
