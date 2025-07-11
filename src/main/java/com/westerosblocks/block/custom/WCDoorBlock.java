package com.westerosblocks.block.custom;

import com.westerosblocks.util.ModBlockSetType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCDoorBlock extends DoorBlock {

    private final boolean locked;
    private final boolean allowUnsupported;

    public WCDoorBlock(AbstractBlock.Settings settings, String woodType,
            boolean locked, boolean allowUnsupported) {
        super(ModBlockSetType.getBlockSetType(woodType), settings);
        this.locked = locked;
        this.allowUnsupported = allowUnsupported;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allowUnsupported && (state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }

    /**
     * Gets whether this door is locked.
     *
     * @return true if the door is locked
     */
    public boolean isLocked() {
        return locked;
    }
}