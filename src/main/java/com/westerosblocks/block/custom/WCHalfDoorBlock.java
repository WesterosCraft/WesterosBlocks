//package com.westerosblocks.block.custom;
//
//import com.sun.jdi.Mirror;
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.*;
//import net.minecraft.block.*;
//import net.minecraft.block.enums.DoorHinge;
//import net.minecraft.data.client.VariantSettings.Rotation;
//import net.minecraft.entity.ai.pathing.NavigationType;
//import net.minecraft.item.ItemPlacementContext;
//import net.minecraft.state.StateManager;
//import net.minecraft.state.property.BooleanProperty;
//import net.minecraft.state.property.DirectionProperty;
//import net.minecraft.state.property.EnumProperty;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.BlockMirror;
//import net.minecraft.util.BlockRotation;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.shape.VoxelShape;
//import net.minecraft.util.shape.VoxelShapes;
//import net.minecraft.world.BlockView;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldView;
//import net.minecraft.world.event.GameEvent;
//
//public class WCHalfDoorBlock extends Block implements WesterosBlockLifecycle {
//
//    public static class Factory extends WesterosBlockFactory {
//        @Override
//        public Block buildBlockClass(WesterosBlockDef def) {
//            def.nonOpaque = true;
//            AbstractBlock.Settings settings = def.makeBlockSettings();
//            Block blk = new WCHalfDoorBlock(settings, def);
//            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
//        }
//    }
//
//    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
//    public static final BooleanProperty OPEN = Properties.OPEN;
//    public static final EnumProperty<DoorHinge> HINGE = Properties.DOOR_HINGE;
//    public static final BooleanProperty POWERED = Properties.POWERED;
//    protected static final VoxelShape SOUTH_AABB = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
//    protected static final VoxelShape NORTH_AABB = VoxelShapes.cuboid(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
//    protected static final VoxelShape WEST_AABB = VoxelShapes.cuboid(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
//    protected static final VoxelShape EAST_AABB = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
//
//    private WesterosBlockDef def;
//    private boolean locked = false;
//    private boolean allow_unsupported = false;
//    private final AbstractBlock.Settings material;
//
//    protected WCHalfDoorBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
//        super(settings);
//        this.def = def;
//        String type = def.getType();
//        if (type != null) {
//            String[] toks = type.split(",");
//            for (String tok : toks) {
//                if (tok.equals("allow-unsupported")) {
//                    allow_unsupported = true;
//                }
//                String[] flds = tok.split(":");
//                if (flds.length < 2) continue;
//                if (flds[0].equals("locked")) {
//                    locked = flds[1].equals("true");
//                }
//            }
//        }
//        setDefaultState(getDefaultState().with(FACING, Direction.NORTH)
//                .with(OPEN, Boolean.FALSE)
//                .with(HINGE, DoorHinge.LEFT)
//                .with(POWERED, Boolean.FALSE));
//        this.material = WesterosBlockSettings.get(def.material);
//    }
//
//    @Override
//    public WesterosBlockDef getWBDefinition() {
//        return def;
//    }
//
//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        Direction direction = state.get(FACING);
//        boolean flag = !state.get(OPEN);
//        boolean flag1 = state.get(HINGE) == DoorHinge.RIGHT;
//        return switch (direction) {
//            default -> flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
//            case SOUTH -> flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
//            case WEST -> flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
//            case NORTH -> flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
//        };
//    }
//
//    @Override
//    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
//        return VoxelShapes.empty();
//    }
//
//    @Override
//    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
//        return switch (type) {
//            case LAND -> state.get(OPEN);
//            case WATER -> false;
//            case AIR -> state.get(OPEN);
//            default -> false;
//        };
//    }
//
//
//    private int getCloseSound() {
//        return this.material == WesterosBlockSettings.get("metal") ? 1011 : 1012;
//    }
//
//    // todo implmeent metal blocksetting
//    private int getOpenSound() {
//        return this.material == WesterosBlockSettings.get("metal") ? 1005 : 1006;
//    }
//
//    @Override
//    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        BlockPos blockpos = ctx.getBlockPos();
//        World world = ctx.getWorld();
//        boolean flag = world.hasNeighborSignal(blockpos);
//        return getDefaultState()
//                .with(FACING, ctx.getHorizontalDirection().getOpposite())
//                .with(HINGE, this.getHinge(ctx))
//                .with(POWERED, flag)
//                .with(OPEN, flag);
//    }
//
//    private DoorHinge getHinge(BlockPlaceContext ctx) {
//        BlockGetter BlockGetter = ctx.getLevel();
//        BlockPos blockpos = ctx.getBlockPos();
//        Direction direction = ctx.getHorizontalDirection();
//        BlockPos blockpos1 = blockpos.up();
//        Direction direction1 = direction.getCounterClockWise();
//        BlockPos blockpos2 = blockpos.offset(direction1);
//        BlockState blockstate = BlockGetter.getBlockState(blockpos2);
//        BlockPos blockpos3 = blockpos1.offset(direction1);
//        BlockState blockstate1 = BlockGetter.getBlockState(blockpos3);
//        Direction direction2 = direction.getClockWise();
//        BlockPos blockpos4 = blockpos.offset(direction2);
//        BlockState blockstate2 = BlockGetter.getBlockState(blockpos4);
//        BlockPos blockpos5 = blockpos1.offset(direction2);
//        BlockState blockstate3 = BlockGetter.getBlockState(blockpos5);
//        int i = (blockstate.isCollisionShapeFullBlock(BlockGetter, blockpos2) ? -1 : 0) + (blockstate1.isCollisionShapeFullBlock(BlockGetter, blockpos3) ? -1 : 0) + (blockstate2.isCollisionShapeFullBlock(BlockGetter, blockpos4) ? 1 : 0) + (blockstate3.isCollisionShapeFullBlock(BlockGetter, blockpos5) ? 1 : 0);
//        boolean flag = blockstate.is(this);
//        boolean flag1 = blockstate2.is(this);
//        if ((!flag || flag1) && i <= 0) {
//            if ((!flag1 || flag) && i >= 0) {
//                int j = direction.getStepX();
//                int k = direction.getStepZ();
//                Vec3 vec3 = ctx.getClickLocation();
//                double d0 = vec3.x - (double) blockpos.getX();
//                double d1 = vec3.z - (double) blockpos.getZ();
//                return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
//            } else {
//                return DoorHingeSide.LEFT;
//            }
//        } else {
//            return DoorHingeSide.RIGHT;
//        }
//    }
//
//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
//        if (this.locked) {
//            return InteractionResult.PASS;
//        } else {
//            state = state.cycle(OPEN);
//            level.setBlock(pos, state, 10);
//            level.levelEvent(player, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
//            level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, player.position());
//            // Is this a door we should be planning to close
//            if (WesterosBlocks.isAutoRestoreHalfDoor(state.getBlock())) {
//                boolean isCreative = (player != null) ? player.isCreative() : false;
//                WesterosBlocks.setPendingHalfDoorRestore(level, pos, !state.getValue(OPEN), isCreative);
//            }
//            return InteractionResult.sidedSuccess(level.isClientSide);
//        }
//    }
//
//    public boolean isOpen(BlockState state) {
//        return state.get(OPEN);
//    }
//
//    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
//        if (state.is(this) && state.getValue(OPEN) != open) {
//            level.setBlock(pos, state.setValue(OPEN, Boolean.valueOf(open)), 10);
//            this.playSound(level, pos, open);
//            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
//        }
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, Level level, BlockPos ppos, Block block, BlockPos pos2, boolean chg) {
//        boolean flag = level.hasNeighborSignal(ppos);
//        if (block != this && flag != state.getValue(POWERED)) {
//            if (flag != state.getValue(OPEN)) {
//                this.playSound(level, ppos, flag);
//                level.gameEvent(flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, ppos, GameEvent.Context.of(state));
//            }
//
//            level.setBlock(ppos, state.setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)), 2);
//        }
//
//    }
//
//    @Override
//    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
//        if (allow_unsupported) return true;
//        BlockPos blockpos = pos.down();
//        BlockState blockstate = world.getBlockState(blockpos);
//        return blockstate.isSideSolidFullSquare(world, blockpos, Direction.UP);
//    }
//
//    private void playSound(Level level, BlockPos pos, boolean open) {
//        level.levelEvent((Player) null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
//    }
//
//    @Override
//    public PushReaction getPistonPushReaction(BlockState p_52814_) {
//        return PushReaction.DESTROY;
//    }
//
//    @Override
//    protected BlockState rotate(BlockState state, BlockRotation rotation) {
//        return state.with(FACING, rotation.rotate(state.get(FACING)));
//    }
//
//    @Override
//    protected BlockState mirror(BlockState state, BlockMirror mirror) {
//        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.get(FACING))).cycle(HINGE);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public long getSeed(BlockState state, BlockPos pos) {
//        return Mth.getSeed(pos.getX(), pos.down(0).getY(), pos.getZ());
//    }
//
//    @Override
//    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(FACING, OPEN, HINGE, POWERED);
//    }
//
//    private static String[] TAGS = {"doors"};
//
//    @Override
//    public String[] getBlockTags() {
//        return TAGS;
//    }
//}
