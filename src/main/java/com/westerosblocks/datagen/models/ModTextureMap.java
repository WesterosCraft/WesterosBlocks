package com.westerosblocks.datagen.models;

import com.westerosblocks.block.WesterosBlockDef;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import static com.westerosblocks.datagen.ModelExport.createBlockIdentifier;

public class ModTextureMap extends TextureMap {

    public static TextureMap customFrontTopSide(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(TextureKey.EAST, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }

    public static TextureMap customTxtN(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(ModTextureKey.TEXTURE_0, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(ModTextureKey.TEXTURE_1, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(ModTextureKey.TEXTURE_2, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(ModTextureKey.TEXTURE_3, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(ModTextureKey.TEXTURE_4, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(0)));
    }
}
