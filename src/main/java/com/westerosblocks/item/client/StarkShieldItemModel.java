package com.westerosblocks.item.client;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.item.custom.StarkShieldItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class StarkShieldItemModel extends GeoModel<StarkShieldItem> {

    @SuppressWarnings("removal")
    @Override
    @Deprecated
    public Identifier getModelResource(StarkShieldItem starkShieldItem) {
        return WesterosBlocks.id("geo/stark_shield.geo.json");
    }

    @SuppressWarnings("removal")
    @Override
    @Deprecated
    public Identifier getTextureResource(StarkShieldItem starkShieldItem) {
        return WesterosBlocks.id("textures/item/stark_shield.png");
    }

    @Override
    public Identifier getAnimationResource(StarkShieldItem starkShieldItem) {
        return WesterosBlocks.id("animations/stark_shield.animation.json");
    }
}
