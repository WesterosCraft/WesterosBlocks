package com.westerosblocks.block.blockentity.client;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.blockentity.custom.WCBigDoorBlockEntity;
import com.westerosblocks.block.custom.WCBigDoorBlock;
import mod.azure.azurelib.common.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.common.render.block.AzBlockEntityRendererConfig;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.Identifier;

/**
 * The base AzBlockEntityModelRenderer already centers and rotates the geo by
 * the FACING property (N=0°, W=90°, S=180°, E=270° around Y). Do NOT add a
 * second translate/rotate here or the non-NORTH facings will be double-rotated.
 */
public class WCBigDoorBlockEntityRenderer extends AzBlockEntityRenderer<WCBigDoorBlockEntity> {

    private static final Identifier FALLBACK_MODEL = WesterosBlocks.id("geo/block/bigdoor.geo.json");
    private static final Identifier FALLBACK_TEXTURE = WesterosBlocks.id("textures/block/doors/bigdoor_test.png");

    public WCBigDoorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(AzBlockEntityRendererConfig.<WCBigDoorBlockEntity>builder(
                        WCBigDoorBlockEntityRenderer::resolveModel,
                        WCBigDoorBlockEntityRenderer::resolveTexture)
                .setAnimatorProvider(WCBigDoorBlockAnimator::new)
                .build());
    }

    private static Identifier resolveModel(WCBigDoorBlockEntity entity) {
        if (entity.getCachedState().getBlock() instanceof WCBigDoorBlock block) {
            return block.getGeoLocation();
        }
        return FALLBACK_MODEL;
    }

    private static Identifier resolveTexture(WCBigDoorBlockEntity entity) {
        if (entity.getCachedState().getBlock() instanceof WCBigDoorBlock block) {
            return block.getTextureLocation();
        }
        return FALLBACK_TEXTURE;
    }
}
