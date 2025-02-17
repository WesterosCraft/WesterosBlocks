package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

import java.util.List;

public class WCCuboidNSEWUDBlock extends WCCuboidBlock implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.applyCustomProperties();
            // See if we have a state property
            ModBlock.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            Block blk = new WCCuboidNSEWUDBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.DOWN, Direction.UP);

    protected WCCuboidNSEWUDBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def, 6);

        int stateCount = def.states.size();
        for (int stateIdx = 0; stateIdx < stateCount; stateIdx++) {
            int baseOffset = stateIdx * this.modelsPerState;

            for (ModBlock.Cuboid cuboid : cuboid_by_facing[baseOffset]) {
                cuboid_by_facing[baseOffset + 1].add(cuboid.rotateCuboid(ModBlock.CuboidRotation.ROTY90));
                cuboid_by_facing[baseOffset + 2].add(cuboid.rotateCuboid(ModBlock.CuboidRotation.ROTY180));
                cuboid_by_facing[baseOffset + 3].add(cuboid.rotateCuboid(ModBlock.CuboidRotation.ROTY270));
                cuboid_by_facing[baseOffset + 4].add(cuboid.rotateCuboid(ModBlock.CuboidRotation.ROTZ90));
                cuboid_by_facing[baseOffset + 5].add(cuboid.rotateCuboid(ModBlock.CuboidRotation.ROTZ270));
            }
        }

        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
            }
        }

        BlockState defaultState = this.getDefaultState()
                .with(FACING, Direction.EAST)
                .with(WATERLOGGED, false);
        if (STATE != null) {
            defaultState = defaultState.with(STATE, STATE.defValue);
        }
        setDefaultState(defaultState);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public List<ModBlock.Cuboid> getModelCuboids(int stateIdx) {
        return cuboid_by_facing[modelsPerState * stateIdx + 3];
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected int getIndexFromState(BlockState state) {
        int off = super.getIndexFromState(state);
        return switch (state.get(FACING)) {
            case EAST -> off;
            case SOUTH -> off + 1;
            case WEST -> off + 2;
            case NORTH -> off + 3;
            case DOWN -> off + 4;
            case UP -> off + 5;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide().getOpposite();
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState blockState = this.getDefaultState()
                .with(FACING, direction)
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

        if (STATE != null) {
            blockState = blockState.with(STATE, STATE.defValue);
        }

        return blockState;
    }

//    @Override
//    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
//        Direction[] adirection = ctx.getPlacementDirections();
//        Direction dir = Direction.EAST;    // Default
//        for (Direction d : adirection) {
//            dir = d;
//            break;
//        }
//        BlockState bs = this.getDefaultState().with(FACING, dir).with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
//        if (STATE != null) {
//            bs = bs.with(STATE, STATE.defValue);
//        }
//        return bs;
//    }
}
