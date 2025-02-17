package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WCWallBlock extends WallBlock implements ModBlockLifecycle {

    public static final BooleanProperty UP = Properties.UP;
    public static EnumProperty<WallShape> EAST_WALL = Properties.EAST_WALL_SHAPE;
    public static EnumProperty<WallShape> NORTH_WALL = Properties.NORTH_WALL_SHAPE;
    public static EnumProperty<WallShape> SOUTH_WALL = Properties.SOUTH_WALL_SHAPE;
    public static EnumProperty<WallShape> WEST_WALL = Properties.WEST_WALL_SHAPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape POST_TEST = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape NORTH_TEST = Block.createCuboidShape(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape SOUTH_TEST = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_TEST = Block.createCuboidShape(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape EAST_TEST = Block.createCuboidShape(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);

    private final ModBlock def;
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;
    public final boolean unconnect;

    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    protected static IntProperty tempCONNECTSTATE;
    public final boolean connectstate;

    protected static ModBlock.StateProperty tempSTATE;
    protected ModBlock.StateProperty STATE;

    protected boolean toggleOnUse = false;

    public final VoxelShape[] ourShapeByIndex;
    public final VoxelShape[] ourCollisionShapeByIndex;

    private static VoxelShape[] ourCollisionShapeByIndexShared = null;
    private static VoxelShape[] ourShapeByIndexSharedShort = null;
    private static VoxelShape[] ourShapeByIndexSharedNormal = null;

    public enum WallSize {
        NORMAL, // 16/16 high wall
        SHORT // 13/16 high wall
    }

    public final WallSize wallSize; // "normal", or "short"

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.applyCustomProperties();
            // See if we have a state property
            ModBlock.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            // Process types
            String t = def.getType();
            boolean doUnconnect = false;
            boolean doConnectstate = false;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                    String[] parts = tok.split(":");
                    // See if we have unconnect
                    if (parts[0].equals("unconnect")) {
                        doUnconnect = true;
                        tempUNCONNECT = UNCONNECT;
                    }
                    // See if we have connectstate
                    if (parts[0].equals("connectstate")) {
                        doConnectstate = true;
                        tempCONNECTSTATE = CONNECTSTATE;
                    }
                }
            }

            Block blk = new WCWallBlock(settings, def, doUnconnect, doConnectstate);

            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    protected WCWallBlock(AbstractBlock.Settings settings, ModBlock def, boolean doUnconnect, boolean doConnectstate) {
        super(settings);
        this.def = def;

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

        String height = def.getTypeValue("size", "normal");
        float wallHeight;
        if (height.equals("short")) {
            wallSize = WallSize.SHORT;
            wallHeight = 13;
        } else {
            wallSize = WallSize.NORMAL;
            wallHeight = 16;
        }
        unconnect = doUnconnect;
        connectstate = doConnectstate;

        // Initialize default state
        BlockState defbs = super.getDefaultState() // Use super to get base WallBlock state
                .with(UP, Boolean.TRUE)
                .with(NORTH_WALL, WallShape.NONE)
                .with(EAST_WALL, WallShape.NONE)
                .with(SOUTH_WALL, WallShape.NONE)
                .with(WEST_WALL, WallShape.NONE)
                .with(WATERLOGGED, Boolean.FALSE);

        if (unconnect) {
            defbs = defbs.with(UNCONNECT, Boolean.FALSE);
        }
        if (connectstate) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);

        // Initialize shape arrays
        if (ourCollisionShapeByIndexShared == null) {
            ourCollisionShapeByIndexShared = makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
        }
        this.ourCollisionShapeByIndex = ourCollisionShapeByIndexShared;

        if (height.equals("short")) {
            if (ourShapeByIndexSharedShort == null) {
                ourShapeByIndexSharedShort = makeShapes(4.0F, 3.0F, 16.0F, 0.0F, wallHeight, 16.0F);
            }
            this.ourShapeByIndex = ourShapeByIndexSharedShort;
        } else {
            if (ourShapeByIndexSharedNormal == null) {
                ourShapeByIndexSharedNormal = makeShapes(4.0F, 3.0F, 16.0F, 0.0F, wallHeight, 16.0F);
            }
            this.ourShapeByIndex = ourShapeByIndexSharedNormal;
        }
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (unconnect && state.get(UNCONNECT)) {
            return this.ourCollisionShapeByIndex[getStateIndex(state)];
        }
        VoxelShape parentShape = super.getCollisionShape(state, world, pos, context);
        return parentShape != null ? parentShape : VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.ourShapeByIndex[getStateIndex(state)];
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if (tempUNCONNECT != null) {
            builder.add(tempUNCONNECT);
            tempUNCONNECT = null;
        }
        if (tempCONNECTSTATE != null) {
            builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
        }
        if (STATE != null) {
            builder.add(STATE);
        }
    }

    private static VoxelShape applyWallShape(VoxelShape voxelShape, WallShape wallShape, VoxelShape p_58036_,
                                             VoxelShape p_58037_) {
        if (wallShape == WallShape.TALL) {
            return VoxelShapes.union(voxelShape, p_58037_);
        } else {
            return wallShape == WallShape.LOW ? VoxelShapes.union(voxelShape, p_58036_) : voxelShape;
        }
    }

    private static int getStateIndex(BlockState bs) {
        return getStateIndex(bs.get(UP), bs.get(EAST_WALL).ordinal(),
                bs.get(WEST_WALL).ordinal(), bs.get(NORTH_WALL).ordinal(), bs.get(SOUTH_WALL).ordinal());
    }

    private static int getStateIndex(boolean up, int east, int west, int north, int south) {
        return (up ? 1 : 0) + (east * 2) + (west * 6) + (north * 18) + (south * 54);
    }

    private static VoxelShape[] makeShapes(float minX, float minY, float minZ, float maxX,
                                           float maxY, float maxZ) {
        float f = 8.0F - minX;
        float f1 = 8.0F + minX;
        float f2 = 8.0F - minY;
        float f3 = 8.0F + minY;
        VoxelShape voxelshape = createCuboidShape(f, 0.0D, f, f1, minZ, f1);
        VoxelShape voxelshape1 = createCuboidShape(f2, maxX, 0.0D, f3, maxY,
                f3);
        VoxelShape voxelshape2 = createCuboidShape(f2, maxX, f2, f3, maxY,
                16.0D);
        VoxelShape voxelshape3 = createCuboidShape(0.0D, maxX, f2, f3, maxY,
                f3);
        VoxelShape voxelshape4 = createCuboidShape(f2, maxX, f2, 16.0D, maxY,
                f3);
        VoxelShape voxelshape5 = createCuboidShape(f2, maxX, 0.0D, f3, maxZ,
                f3);
        VoxelShape voxelshape6 = createCuboidShape(f2, maxX, f2, f3, maxZ,
                16.0D);
        VoxelShape voxelshape7 = createCuboidShape(0.0D, maxX, f2, f3, maxZ,
                f3);
        VoxelShape voxelshape8 = createCuboidShape(f2, maxX, f2, 16.0D, maxZ,
                f3);
        VoxelShape[] map = new VoxelShape[2 * 3 * 3 * 3 * 3];

        for (Boolean up : UP.getValues()) {
            for (WallShape east : EAST_WALL.getValues()) {
                for (WallShape north : NORTH_WALL.getValues()) {
                    for (WallShape west : WEST_WALL.getValues()) {
                        for (WallShape south : SOUTH_WALL.getValues()) {
                            VoxelShape shape = VoxelShapes.empty();
                            shape = applyWallShape(shape, east, voxelshape4, voxelshape8);
                            shape = applyWallShape(shape, west, voxelshape3, voxelshape7);
                            shape = applyWallShape(shape, north, voxelshape1, voxelshape5);
                            shape = applyWallShape(shape, south, voxelshape2, voxelshape6);
                            if (up) {
                                shape = VoxelShapes.union(shape, voxelshape);
                            }
                            map[getStateIndex(up, east.ordinal(), west.ordinal(),
                                    north.ordinal(), south.ordinal())] = shape;
                        }
                    }
                }
            }
        }
        return map;
    }

    private boolean connectsTo(BlockState blockState, boolean p_58022_, Direction direction) {
        Block block = blockState.getBlock();
        boolean flag = block instanceof FenceGateBlock && FenceGateBlock.canWallConnect(blockState, direction);
        return blockState.isIn(BlockTags.WALLS) || !cannotConnect(blockState) && p_58022_
                || block == Blocks.IRON_BARS
                || flag;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockpos = ctx.getBlockPos();
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.up();
        BlockState blockstate = world.getBlockState(blockpos1);
        BlockState blockstate1 = world.getBlockState(blockpos2);
        BlockState blockstate2 = world.getBlockState(blockpos3);
        BlockState blockstate3 = world.getBlockState(blockpos4);
        BlockState blockstate4 = world.getBlockState(blockpos5);
        boolean flag = this.connectsTo(blockstate, blockstate.isSideSolidFullSquare(world, blockpos1, Direction.SOUTH),
                Direction.SOUTH);
        boolean flag1 = this.connectsTo(blockstate1, blockstate1.isSideSolidFullSquare(world, blockpos2, Direction.WEST),
                Direction.WEST);
        boolean flag2 = this.connectsTo(blockstate2, blockstate2.isSideSolidFullSquare(world, blockpos3, Direction.NORTH),
                Direction.NORTH);
        boolean flag3 = this.connectsTo(blockstate3, blockstate3.isSideSolidFullSquare(world, blockpos4, Direction.EAST),
                Direction.EAST);
        BlockState blockstate5 = this.getDefaultState().with(WATERLOGGED,
                fluidstate.getFluid() == Fluids.WATER);
        return this.updateShape(world, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (unconnect && state.get(UNCONNECT)) {
            return state;
        }
        if (direction == Direction.DOWN) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
            return direction == Direction.UP ? this.topUpdate(world, state, neighborPos, neighborState)
                    : this.sideUpdate(world, pos, state, neighborPos, neighborState, direction);
        }
    }

    private static boolean isConnected(BlockState blockState, EnumProperty<WallShape> wallShapeEnumProperty) {
        return blockState.get(wallShapeEnumProperty) != WallShape.NONE;
    }

    private static boolean isCovered(VoxelShape shape1, VoxelShape shape2) {
        return !VoxelShapes.combine(shape2, shape1, BooleanBiFunction.ONLY_FIRST).isEmpty();
    }

    private BlockState topUpdate(WorldAccess world, BlockState p_57976_, BlockPos p_57977_, BlockState p_57978_) {
        boolean flag = isConnected(p_57976_, NORTH_WALL);
        boolean flag1 = isConnected(p_57976_, EAST_WALL);
        boolean flag2 = isConnected(p_57976_, SOUTH_WALL);
        boolean flag3 = isConnected(p_57976_, WEST_WALL);
        return this.updateShape(world, p_57976_, p_57977_, p_57978_, flag, flag1, flag2, flag3);
    }

    private BlockState sideUpdate(WorldAccess world, BlockPos pos, BlockState blockState, BlockPos blockPos2,
                                  BlockState blockState2, Direction dir) {
        Direction direction = dir.getOpposite();
        boolean flag = dir == Direction.NORTH
                ? this.connectsTo(blockState2, blockState2.isSideSolidFullSquare(world, blockPos2, direction), direction)
                : isConnected(blockState, NORTH_WALL);
        boolean flag1 = dir == Direction.EAST
                ? this.connectsTo(blockState2, blockState2.isSideSolidFullSquare(world, blockPos2, direction), direction)
                : isConnected(blockState, EAST_WALL);
        boolean flag2 = dir == Direction.SOUTH
                ? this.connectsTo(blockState2, blockState2.isSideSolidFullSquare(world, blockPos2, direction), direction)
                : isConnected(blockState, SOUTH_WALL);
        boolean flag3 = dir == Direction.WEST
                ? this.connectsTo(blockState2, blockState2.isSideSolidFullSquare(world, blockPos2, direction), direction)
                : isConnected(blockState, WEST_WALL);
        BlockPos blockpos = pos.up();
        BlockState blockstate = world.getBlockState(blockpos);
        return this.updateShape(world, blockState, blockpos, blockstate, flag, flag1, flag2, flag3);
    }

    private BlockState updateShape(WorldAccess world, BlockState p_57981_, BlockPos p_57982_, BlockState p_57983_,
                                   boolean p_57984_, boolean p_57985_, boolean p_57986_, boolean p_57987_) {
        VoxelShape voxelshape = p_57983_.getCollisionShape(world, p_57982_).getFace(Direction.DOWN);
        BlockState blockstate = this.updateSides(p_57981_, p_57984_, p_57985_, p_57986_, p_57987_, voxelshape);
        return blockstate.with(UP, this.shouldRaisePost(blockstate, p_57983_, voxelshape));
    }

    private boolean shouldRaisePost(BlockState blockState, BlockState blockState2, VoxelShape voxelShape) {
        boolean flag = blockState2.getBlock() instanceof WallBlock && blockState2.get(UP);
        if (flag) {
            return true;
        } else {
            WallShape wallside = blockState.get(NORTH_WALL);
            WallShape wallside1 = blockState.get(SOUTH_WALL);
            WallShape wallside2 = blockState.get(EAST_WALL);
            WallShape wallside3 = blockState.get(WEST_WALL);
            boolean flag1 = wallside1 == WallShape.NONE;
            boolean flag2 = wallside3 == WallShape.NONE;
            boolean flag3 = wallside2 == WallShape.NONE;
            boolean flag4 = wallside == WallShape.NONE;
            boolean flag5 = flag4 && flag1 && flag2 && flag3 || flag4 != flag1 || flag2 != flag3;
            if (flag5) {
                return true;
            } else {
                boolean flag6 = wallside == WallShape.TALL && wallside1 == WallShape.TALL
                        || wallside2 == WallShape.TALL && wallside3 == WallShape.TALL;
                if (flag6) {
                    return false;
                } else {
                    return blockState2.isIn(BlockTags.WALL_POST_OVERRIDE) || isCovered(voxelShape, POST_TEST);
                }
            }
        }
    }

    private BlockState updateSides(BlockState p_58025_, boolean p_58026_, boolean p_58027_, boolean p_58028_,
                                   boolean p_58029_, VoxelShape p_58030_) {
        return p_58025_.with(NORTH_WALL, this.makeWallState(p_58026_, p_58030_, NORTH_TEST))
                .with(EAST_WALL, this.makeWallState(p_58027_, p_58030_, EAST_TEST))
                .with(SOUTH_WALL, this.makeWallState(p_58028_, p_58030_, SOUTH_TEST))
                .with(WEST_WALL, this.makeWallState(p_58029_, p_58030_, WEST_TEST));
    }

    private WallShape makeWallState(boolean p_58042_, VoxelShape p_58043_, VoxelShape p_58044_) {
        if (p_58042_) {
            return isCovered(p_58043_, p_58044_) ? WallShape.TALL : WallShape.LOW;
        } else {
            return WallShape.NONE;
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> state.with(NORTH_WALL, state.get(SOUTH_WALL))
                    .with(EAST_WALL, state.get(WEST_WALL))
                    .with(SOUTH_WALL, state.get(NORTH_WALL))
                    .with(WEST_WALL, state.get(EAST_WALL));
            case COUNTERCLOCKWISE_90 -> state.with(NORTH_WALL, state.get(EAST_WALL))
                    .with(EAST_WALL, state.get(SOUTH_WALL))
                    .with(SOUTH_WALL, state.get(WEST_WALL))
                    .with(WEST_WALL, state.get(NORTH_WALL));
            case CLOCKWISE_90 -> state.with(NORTH_WALL, state.get(WEST_WALL))
                    .with(EAST_WALL, state.get(NORTH_WALL))
                    .with(SOUTH_WALL, state.get(EAST_WALL))
                    .with(WEST_WALL, state.get(SOUTH_WALL));
            default -> state;
        };
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state
                    .with(NORTH_WALL, state.get(SOUTH_WALL))
                    .with(SOUTH_WALL, state.get(NORTH_WALL));
            case FRONT_BACK -> state
                    .with(EAST_WALL, state.get(WEST_WALL))
                    .with(WEST_WALL, state.get(EAST_WALL));
            default -> super.mirror(state, mirror);
        };
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.toggleOnUse) {
            Hand hand = player.getActiveHand();
            if ((this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
                state = state.cycle(this.STATE);
                world.setBlockState(pos, state, 10);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }

        return super.onUse(state, world, pos, player, hit);
    }

    private static final String[] TAGS = {"walls"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }
}
