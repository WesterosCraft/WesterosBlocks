package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
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
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // Handle null definition (from BlockBuilder) with sensible defaults
            boolean allowUnsupported = definition != null && definition.isAllowUnsupported();
            boolean noClimb = definition != null && definition.getNoClimb() != null && definition.getNoClimb();

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
