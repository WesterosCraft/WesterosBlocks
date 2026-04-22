package com.westerosblocks.block.blockentity.client;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.blockentity.custom.WCBigDoorBlockEntity;
import mod.azure.azurelib.common.animation.AzAnimatorConfig;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzBlockAnimator;
import mod.azure.azurelib.common.model.AzBone;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Drives the door bones directly from the BE's swing progress.
 * No AzAnimationControllers are registered — we write bone rotations in
 * setCustomAnimations, which runs each render frame after the (empty) controller
 * update. This guarantees visual and server state never desync: both sides
 * compute from the same swing progress on the same BlockEntity.
 *
 * The animations file is an empty stub: AzAnimator#getAnimationLocation is
 * abstract so we must return an Identifier, but with zero controllers the
 * animation cache is never queried. Keep the JSON empty so no-one mistakes
 * stale keyframes for the source of truth — the angle constant in
 * WCBigDoorBlockEntity.MAX_DOOR_ANGLE_DEGREES is authoritative.
 */
public class WCBigDoorBlockAnimator extends AzBlockAnimator<WCBigDoorBlockEntity> {

    private static final Identifier ANIMATIONS = WesterosBlocks.id("animations/block/bigdoor.animation.json");
    private static final String LEFT_DOOR = "left_door";
    private static final String RIGHT_DOOR = "right_door";

    public WCBigDoorBlockAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<WCBigDoorBlockEntity> container) {
        // Intentionally no controllers. We drive bones directly in setCustomAnimations.
    }

    @Override
    public @NotNull Identifier getAnimationLocation(WCBigDoorBlockEntity animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(WCBigDoorBlockEntity be, float partialTicks) {
        var bakedModel = context().boneCache().getBakedModel();
        if (bakedModel == null) {
            return;
        }
        float angle = be.getDoorAngleRadians(partialTicks);

        // AzureLib's cube baker flips bedrock +X to Minecraft -X, so the
        // geo-named "left_door" (bedrock x=-24..0) actually renders on the
        // east/right side of the door, and "right_door" on the west/left.
        // Positive rotY is CCW viewed from above; to swing the doors AWAY
        // from the player (toward FACING) we need left_door=-angle and
        // right_door=+angle. The previous (left=+, right=-) swung them
        // toward the player.
        AzBone leftDoor = bakedModel.getBoneOrNull(LEFT_DOOR);
        if (leftDoor != null) {
            leftDoor.setRotY(-angle);
        }

        AzBone rightDoor = bakedModel.getBoneOrNull(RIGHT_DOOR);
        if (rightDoor != null) {
            rightDoor.setRotY(angle);
        }
    }
}
