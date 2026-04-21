package com.westerosblocks.block.blockentity.client;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.blockentity.custom.WCBigDoorBlockEntity;
import com.westerosblocks.block.custom.WCBigDoorBlock;
import mod.azure.azurelib.common.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.common.render.block.AzBlockEntityRendererConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class WCBigDoorBlockEntityRenderer extends AzBlockEntityRenderer<WCBigDoorBlockEntity> {

    private static final Identifier FALLBACK_MODEL = WesterosBlocks.id("geo/block/bigdoor.geo.json");
    private static final Identifier FALLBACK_TEXTURE = WesterosBlocks.id("textures/block/debug/bigdoor_test.png");

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

    @Override
    public void render(@NotNull WCBigDoorBlockEntity entity, float partialTick, @NotNull MatrixStack poseStack,
                       @NotNull VertexConsumerProvider source, int packedLight, int packedOverlay) {
        Direction facing = entity.getCachedState().get(WCBigDoorBlock.FACING);
        float yaw = facingToYaw(facing);

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        poseStack.translate(-0.5, 0.0, -0.5);

        super.render(entity, partialTick, poseStack, source, packedLight, packedOverlay);

        poseStack.pop();
    }

    private static float facingToYaw(Direction facing) {
        return switch (facing) {
            case SOUTH -> 180.0f;
            case WEST -> 90.0f;
            case EAST -> 270.0f;
            default -> 0.0f;
        };
    }
}
