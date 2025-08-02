package com.westerosblocks.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

public class ModTextureMap {
    public static TextureMap customAll(Block block, String suffix) {
        return new TextureMap().put(TextureKey.ALL, Identifier.of("westerosblocks", "block/" + suffix));
    }

}
