package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeProperties();
            // See if we have a state property
            WesterosBlockDef.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            Block blk = new WCCuboidBlock(settings, def, 1);
            return def.registerRenderType(blk, false, false);
        }

    }

    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;
    protected boolean toggleOnUse = false;
    protected int modelsPerState;

    protected WesterosBlockDef def;

    protected VoxelShape[] SHAPE_BY_INDEX;
    protected VoxelShape[] SUPPORT_BY_INDEX;
    protected List<WesterosBlockDef.Cuboid>[] cuboid_by_facing;

    protected WCCuboidBlock(AbstractBlock.Settings settings, WesterosBlockDef def, int modelsPerState) {
        super(settings);
        this.def = def;
        this.modelsPerState = modelsPerState;

        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                    break;
                }
            }
        }
        int cnt = def.states.size();
        this.cuboid_by_facing = new List[cnt * modelsPerState];
        SHAPE_BY_INDEX = new VoxelShape[cnt * modelsPerState];
        for (int i = 0; i < cnt; i++) {
            cuboid_by_facing[i * modelsPerState] = def.states.get(i).getCuboidList();
            for (int j = 1; j < modelsPerState; j++) {
                cuboid_by_facing[i * modelsPerState + j] = new ArrayList<WesterosBlockDef.Cuboid>();
            }
            SHAPE_BY_INDEX[i * modelsPerState] = getBoundingBoxFromCuboidList(cuboid_by_facing[i * modelsPerState]);
        }
        SUPPORT_BY_INDEX = new VoxelShape[cnt];
        for (int i = 0; i < cnt; i++) {
            SUPPORT_BY_INDEX[i] = def.states.get(i).makeSupportBoxShape(null);
        }
        BlockState defbs = getDefaultState().with(WATERLOGGED, Boolean.FALSE);
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        setDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
        }
        if (STATE != null) {
            builder.add(STATE);
        }
        builder.add(WATERLOGGED);
    }

    protected int getIndexFromState(BlockState state) {
        if (STATE != null)
            return modelsPerState * STATE.getIndex(state.get(STATE));
        else
            return 0;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        int idx = 0;
        if (STATE != null)
            idx = STATE.getIndex(state.get(STATE));
        return SUPPORT_BY_INDEX[idx];
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState bs = getDefaultState().with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
        if (STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        return bs;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
            default -> false;
        };
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(this.STATE);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    protected VoxelShape getBoundingBoxFromCuboidList(List<WesterosBlockDef.Cuboid> cl) {
        VoxelShape vs = VoxelShapes.empty();
        if (cl != null) {
            for (WesterosBlockDef.Cuboid c : cl) {
                vs = VoxelShapes.union(vs, VoxelShapes.cuboid(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax));
            }
        }
        return vs;
    }

    public List<WesterosBlockDef.Cuboid> getModelCuboids(int stateIdx) {
        return cuboid_by_facing[modelsPerState * stateIdx];
    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
