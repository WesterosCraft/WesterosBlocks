package com.westeroscraft.westerosblocks.blocks;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.WallBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCWallBlock extends WallBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            BlockBehaviour.Properties props = def.makeProperties();
            return def.registerRenderType(def.registerBlock(new WCWallBlock(props, def)), false, false);
        }
    }

    private WesterosBlockDef def;

    public final Map<BlockState, VoxelShape> ourShapeByIndex;
    public final Map<BlockState, VoxelShape> ourCollisionShapeByIndex;

    public static enum WallSize {
        NORMAL, // 16/16 high wall
        SHORT // 13/16 high wall
    };

    public final WallSize wallSize; // "normal", or "short"

    protected WCWallBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String height = def.getTypeValue("size", "normal");
        float wheight;
        if (height.equals("short")) {
            wallSize = WallSize.SHORT;
            wheight = 13;
        } else {
            wallSize = WallSize.NORMAL;
            wheight = 16;
        }
        Map<BlockState, VoxelShape> shapeByIndex = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, wheight, 16.0F);

        this.ourCollisionShapeByIndex = this.collisionShapeByIndex;
        this.ourShapeByIndex = shapeByIndex;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        StateDefinition.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
            CollisionContext p_220053_4_) {
        return this.ourShapeByIndex.get(p_220053_1_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
            CollisionContext p_220071_4_) {
        return this.ourCollisionShapeByIndex.get(p_220071_1_);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState bs = super.getStateForPlacement(ctx);
        return bs;
    }

    private static String[] TAGS = { "walls" };

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
