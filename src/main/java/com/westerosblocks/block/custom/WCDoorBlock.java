package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModBlockSetType;
import net.minecraft.block.*;
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

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            String woodType = definition != null ? definition.getWoodType() : "oak";
            boolean locked = definition != null && definition.isLocked();
            boolean allowUnsupported = definition != null && definition.isAllowUnsupported();
            return new WCDoorBlock(settings, woodType, locked, allowUnsupported);
        }
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
        // Check global config option first
//        if (WesterosBlocks.CONFIG.doorsCanSurviveOnAnySurface && (state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) {
//            return true;
//        }
        // Then check block-specific allowUnsupported setting
        if (allowUnsupported && (state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }
}
