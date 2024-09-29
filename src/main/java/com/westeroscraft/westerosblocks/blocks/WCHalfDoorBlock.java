package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.RegisterEvent;

import javax.annotation.Nullable;

public class WCHalfDoorBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
            def.nonOpaque = true;
            BlockBehaviour.Properties props = def.makeProperties();
            Block blk = new WCHalfDoorBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    private WesterosBlockDef def;
    private boolean locked = false;
    private boolean allow_unsupported = false;
    private final AuxMaterial material;

    protected WCHalfDoorBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                String[] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("locked")) {
                    locked = flds[1].equals("true");
                }
            }
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, Boolean.valueOf(false)));
        this.material = AuxMaterial.getMaterial(props, def);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext selCtx) {
        Direction direction = state.getValue(FACING);
        boolean flag = !state.getValue(OPEN);
        boolean flag1 = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        switch (direction) {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch (pathComputationType) {
            case LAND:
                return state.getValue(OPEN);
            case WATER:
                return false;
            case AIR:
                return state.getValue(OPEN);
            default:
                return false;
        }
    }


    private int getCloseSound() {
        return this.material == AuxMaterial.METAL ? 1011 : 1012;
    }

    private int getOpenSound() {
        return this.material == AuxMaterial.METAL ? 1005 : 1006;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        boolean flag = level.hasNeighborSignal(blockpos);
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(HINGE, this.getHinge(ctx)).setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag));
    }

    private DoorHingeSide getHinge(BlockPlaceContext ctx) {
        BlockGetter BlockGetter = ctx.getLevel();
        BlockPos blockpos = ctx.getClickedPos();
        Direction direction = ctx.getHorizontalDirection();
        BlockPos blockpos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockpos2 = blockpos.relative(direction1);
        BlockState blockstate = BlockGetter.getBlockState(blockpos2);
        BlockPos blockpos3 = blockpos1.relative(direction1);
        BlockState blockstate1 = BlockGetter.getBlockState(blockpos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockpos4 = blockpos.relative(direction2);
        BlockState blockstate2 = BlockGetter.getBlockState(blockpos4);
        BlockPos blockpos5 = blockpos1.relative(direction2);
        BlockState blockstate3 = BlockGetter.getBlockState(blockpos5);
        int i = (blockstate.isCollisionShapeFullBlock(BlockGetter, blockpos2) ? -1 : 0) + (blockstate1.isCollisionShapeFullBlock(BlockGetter, blockpos3) ? -1 : 0) + (blockstate2.isCollisionShapeFullBlock(BlockGetter, blockpos4) ? 1 : 0) + (blockstate3.isCollisionShapeFullBlock(BlockGetter, blockpos5) ? 1 : 0);
        boolean flag = blockstate.is(this);
        boolean flag1 = blockstate2.is(this);
        if ((!flag || flag1) && i <= 0) {
            if ((!flag1 || flag) && i >= 0) {
                int j = direction.getStepX();
                int k = direction.getStepZ();
                Vec3 vec3 = ctx.getClickLocation();
                double d0 = vec3.x - (double) blockpos.getX();
                double d1 = vec3.z - (double) blockpos.getZ();
                return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.locked) {
            return InteractionResult.PASS;
        } else {
            state = state.cycle(OPEN);
            level.setBlock(pos, state, 10);
            level.levelEvent(player, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, player.position());
            // Is this a door we should be planning to close
            if (WesterosBlocks.isAutoRestoreHalfDoor(state.getBlock())) {
                boolean isCreative = (player != null) ? player.isCreative() : false;
                WesterosBlocks.setPendingHalfDoorRestore(level, pos, !state.getValue(OPEN), isCreative);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public boolean isOpen(BlockState state) {
        return state.getValue(OPEN);
    }

    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
        if (state.is(this) && state.getValue(OPEN) != open) {
            level.setBlock(pos, state.setValue(OPEN, Boolean.valueOf(open)), 10);
            this.playSound(level, pos, open);
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos ppos, Block block, BlockPos pos2, boolean chg) {
        boolean flag = level.hasNeighborSignal(ppos);
        if (block != this && flag != state.getValue(POWERED)) {
            if (flag != state.getValue(OPEN)) {
                this.playSound(level, ppos, flag);
                level.gameEvent(flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, ppos, GameEvent.Context.of(state));
            }

            level.setBlock(ppos, state.setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)), 2);
        }

    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (allow_unsupported) return true;
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        return blockstate.isFaceSturdy(reader, blockpos, Direction.UP);
    }

    private void playSound(Level level, BlockPos pos, boolean open) {
        level.levelEvent((Player) null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_52814_) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING))).cycle(HINGE);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(0).getY(), pos.getZ());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(FACING, OPEN, HINGE, POWERED);
    }

    private static String[] TAGS = {"doors"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
