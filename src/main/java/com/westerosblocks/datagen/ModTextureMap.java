package com.westerosblocks.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;

import java.util.Optional;

public class ModTextureMap {
    public static TextureMap customAll(Block block, String suffix) {
        return new TextureMap().put(TextureKey.ALL, TextureMap.getSubId(block, suffix));
    }
}
