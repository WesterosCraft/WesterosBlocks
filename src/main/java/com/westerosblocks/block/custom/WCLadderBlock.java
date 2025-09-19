package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.Map;

public class WCLadderBlock extends LadderBlock {
    private boolean allowUnsupported;
    private boolean noClimb;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                boolean allowUnsupported = (Boolean) paramMap.getOrDefault("allowUnsupported", false);
                boolean noClimb = (Boolean) paramMap.getOrDefault("noClimb", false);

                return new WCLadderBlock(settings.nonOpaque(), allowUnsupported, noClimb);
            }

            // Fallback for legacy parameter style
            boolean allowUnsupported = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            boolean noClimb = params.length > 1 && params[1] instanceof Boolean ? (Boolean) params[1] : false;

            return new WCLadderBlock(settings.nonOpaque(), allowUnsupported, noClimb);
        }
    }

    protected WCLadderBlock(AbstractBlock.Settings settings, boolean allowUnsupported, boolean noClimb) {
        super(settings);
        this.allowUnsupported = allowUnsupported;
        this.noClimb = noClimb;
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return allowUnsupported || super.canPlaceAt(state, world, pos);
    }

}
