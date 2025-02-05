package com.westerosblocks.block.custom;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import com.mojang.serialization.MapCodec;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Optional;

public class WCBedBlock extends HorizontalFacingBlock implements ModBlockLifecycle {
    public static final MapCodec<WCBedBlock> CODEC = createCodec(WCBedBlock::create);

    @Override
    public MapCodec<WCBedBlock> getCodec() {
        return CODEC;
    }

    // TODO: not sure if best way to create this?
    private static WCBedBlock create(AbstractBlock.Settings settings) {
        return new WCBedBlock(settings, null);
    }

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeBlockSettings().nonOpaque();
            Block blk = new WCBedBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }


    public static final EnumProperty<BedPart> PART = Properties.BED_PART;
    public static final BooleanProperty OCCUPIED = Properties.OCCUPIED;
//    protected static final int HEIGHT = 9;
    protected static final VoxelShape BASE = createCuboidShape(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    protected static final VoxelShape LEG_NORTH_WEST = createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D);
    protected static final VoxelShape LEG_SOUTH_WEST = createCuboidShape(0.0D, 0.0D, 13.0D, 3.0D, 3.0D, 16.0D);
    protected static final VoxelShape LEG_NORTH_EAST = createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 3.0D, 3.0D);
    protected static final VoxelShape LEG_SOUTH_EAST = createCuboidShape(13.0D, 0.0D, 13.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape NORTH_SHAPE = VoxelShapes.union(BASE, LEG_NORTH_WEST, LEG_NORTH_EAST);
    protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(BASE, LEG_SOUTH_WEST, LEG_SOUTH_EAST);
    protected static final VoxelShape WEST_SHAPE = VoxelShapes.union(BASE, LEG_NORTH_WEST, LEG_SOUTH_WEST);
    protected static final VoxelShape EAST_SHAPE = VoxelShapes.union(BASE, LEG_NORTH_EAST, LEG_SOUTH_EAST);
    private final DyeColor color;

    private final ModBlock def;

    public enum BedType {
        NORMAL, RAISED, HAMMOCK
    }

    public final BedType bedType;

    protected WCBedBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.color = DyeColor.RED;
        this.def = def;
        String type = def.getTypeValue("shape", "normal");
        this.bedType = switch (type) {
            case "raised" -> BedType.RAISED;
            case "hammock" -> BedType.HAMMOCK;
            default -> BedType.NORMAL;
        };
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(PART, BedPart.FOOT)
                .with(OCCUPIED, false)
                .with(FACING, Direction.NORTH));
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private static final String[] TAGS = {"beds"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

    public static Direction getBedOrientation(BlockView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.getBlock() instanceof BedBlock ? blockState.get(FACING) : null;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        } else {
            if (state.get(PART) != BedPart.HEAD) {
                pos = pos.offset(state.get(FACING));
                state = world.getBlockState(pos);
                if (!state.isOf(this)) {
                    return ActionResult.CONSUME;
                }
            }

            if (!canSetSpawn(world)) {
                world.removeBlock(pos, false);
                BlockPos blockpos = pos.offset(state.get(FACING).getOpposite());
                if (world.getBlockState(blockpos).isOf(this)) {
                    world.removeBlock(blockpos, false);
                }

                world.createExplosion(null,
                        world.getDamageSources().badRespawnPoint(Vec3d.of(pos).add(0.5, 0.5, 0.5)),
                        null,
                        (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, World.ExplosionSourceType.BLOCK);
            } else if (state.get(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(world, pos)) {
                    player.sendMessage(Text.translatable("block.minecraft.bed.occupied"), true);
                }

                return ActionResult.SUCCESS;
            } else {
                player.trySleep(pos).ifLeft((failure) -> {
                    if (failure != null) {
                        player.sendMessage(failure.getMessage(), true);
                    }

                });
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    public static boolean canSetSpawn(World world) {
        return world.getDimension().bedWorks();
    }

    private boolean kickVillagerOutOfBed(World world, BlockPos pos) {
        Box box = new Box(pos);
        List<VillagerEntity> list = world.getNonSpectatingEntities(VillagerEntity.class, box)
                .stream()
                .filter(LivingEntity::isSleeping)
                .toList();

        if (list.isEmpty()) {
            return false;
        } else {
            list.getFirst().wakeUp();
            return true;
        }
    }

    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance * 0.5F);
    }

    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3d velocity = entity.getVelocity();
        if (velocity.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
            entity.setVelocity(velocity.x, -velocity.y * 0.66F * d0, velocity.z);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getNeighbourDirection(state.get(PART), state.get(FACING))) {
            return neighborState.isOf(this) && neighborState.get(PART) != state.get(PART) ? state.with(OCCUPIED, neighborState.get(OCCUPIED)) : Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    private static Direction getNeighbourDirection(BedPart bedPart, Direction direction) {
        return bedPart == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && player.isCreative()) {
            BedPart bedpart = state.get(PART);
            if (bedpart == BedPart.FOOT) {
                BlockPos otherPos = pos.offset(getNeighbourDirection(bedpart, state.get(FACING)));
                BlockState otherState = world.getBlockState(otherPos);
                if (otherState.isOf(this) && otherState.get(PART) == BedPart.HEAD) {
                    world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 35);
                    world.syncWorldEvent(player, 2001, otherPos, Block.getRawIdFromState(otherState));
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing();
        BlockPos blockpos = ctx.getBlockPos();
        BlockPos blockpos1 = blockpos.offset(direction);
        World world = ctx.getWorld();
        return world.getBlockState(blockpos1).canReplace(ctx) && world.getWorldBorder().contains(blockpos1) ? this.getDefaultState().with(FACING, direction) : null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = getConnectedDirection(state).getOpposite();
        return switch (direction) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> EAST_SHAPE;
        };
    }

    public static Direction getConnectedDirection(BlockState p_49558_) {
        Direction direction = p_49558_.get(FACING);
        return p_49558_.get(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    private static boolean isBunkBed(BlockView world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() instanceof BedBlock;
    }

    public static Optional<Vec3d> findStandUpPosition(EntityType<?> type, WorldView world, BlockPos pos, float yaw) {
        Direction facing = world.getBlockState(pos).get(FACING);
        Direction clockwise = facing.rotateYClockwise();
        Direction exitDir = clockwise.asRotation() == yaw ? clockwise.getOpposite() : clockwise;

        if (isBunkBed(world, pos)) {
            return findBunkBedStandUpPosition(type, world, pos, facing, exitDir);
        } else {
            int[][] offsets = getBedStandUpOffsets(facing, exitDir);
            Optional<Vec3d> optional = findStandUpPositionAtOffset(type, world, pos, offsets, true);
            return optional.isPresent() ? optional : findStandUpPositionAtOffset(type, world, pos, offsets, false);
        }
    }

    private static Optional<Vec3d> findBunkBedStandUpPosition(EntityType<?> type, WorldView world, BlockPos pos, Direction facing, Direction exitDir) {
        int[][] surroundOffsets = getBedSurroundStandUpOffsets(facing, exitDir);
        Optional<Vec3d> surroundPos = findStandUpPositionAtOffset(type, world, pos, surroundOffsets, true);

        if (surroundPos.isPresent()) return surroundPos;

        BlockPos lowerPos = pos.down();
        Optional<Vec3d> lowerSurroundPos = findStandUpPositionAtOffset(type, world, lowerPos, surroundOffsets, true);

        if (lowerSurroundPos.isPresent()) return lowerSurroundPos;

        int[][] aboveOffsets = getBedAboveStandUpOffsets(facing);
        Optional<Vec3d> abovePos = findStandUpPositionAtOffset(type, world, pos, aboveOffsets, true);

        if (abovePos.isPresent()) return abovePos;

        Optional<Vec3d> surroundUnsafePos = findStandUpPositionAtOffset(type, world, pos, surroundOffsets, false);
        if (surroundUnsafePos.isPresent()) return surroundUnsafePos;

        Optional<Vec3d> lowerUnsafePos = findStandUpPositionAtOffset(type, world, lowerPos, surroundOffsets, false);
        return lowerUnsafePos.isPresent() ? lowerUnsafePos : findStandUpPositionAtOffset(type, world, pos, aboveOffsets, false);
    }

    private static Optional<Vec3d> findStandUpPositionAtOffset(EntityType<?> type, WorldView world, BlockPos pos, int[][] offsets, boolean safe) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int[] offset : offsets) {
            mutablePos.set(pos.getX() + offset[0], pos.getY(), pos.getZ() + offset[1]);
            Vec3d dismountPos = new Vec3d(
                    mutablePos.getX() + 0.5,
                    mutablePos.getY() + 0.6,
                    mutablePos.getZ() + 0.5
            );

            Box entityBounds = type.getDimensions().getBoxAt(dismountPos.x, dismountPos.y, dismountPos.z);
            if (world.getBlockCollisions(null, entityBounds).iterator().hasNext()) {
                continue;
            }
            BlockPos belowPos = mutablePos.down();
            if (!world.getBlockState(belowPos).blocksMovement() && safe) {
                continue;
            }

            return Optional.of(dismountPos);
        }

        return Optional.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);
        if (!world.isClient) {
            BlockPos headPos = pos.offset(state.get(FACING));
            world.setBlockState(headPos, state.with(PART, BedPart.HEAD), Block.NOTIFY_ALL);
            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, Block.NOTIFY_ALL);
        }
    }

    public DyeColor getColor() {
        return this.color;
    }

    public long getRenderingSeed(BlockState state, BlockPos pos) {
        BlockPos blockpos = pos.offset(state.get(FACING), state.get(PART) == BedPart.HEAD ? 0 : 1);
        return MathHelper.hashCode(blockpos.getX(), pos.getY(), blockpos.getZ());
    }

    private static int[][] getBedStandUpOffsets(Direction dir1, Direction dir2) {
        return ArrayUtils.addAll(getBedSurroundStandUpOffsets(dir1, dir2), getBedAboveStandUpOffsets(dir1));
    }

    private static int[][] getBedSurroundStandUpOffsets(Direction dir1, Direction dir2) {
        return new int[][]{
                {dir2.getOffsetX(), dir2.getOffsetZ()},
                {dir2.getOffsetX() - dir1.getOffsetX(), dir2.getOffsetZ() - dir1.getOffsetZ()},
                {dir2.getOffsetX() - dir1.getOffsetX() * 2, dir2.getOffsetZ() - dir1.getOffsetZ() * 2},
                {-dir1.getOffsetX() * 2, -dir1.getOffsetZ() * 2},
                {-dir2.getOffsetX() - dir1.getOffsetX() * 2, -dir2.getOffsetZ() - dir1.getOffsetZ() * 2},
                {-dir2.getOffsetX() - dir1.getOffsetX(), -dir2.getOffsetZ() - dir1.getOffsetZ()},
                {-dir2.getOffsetX(), -dir2.getOffsetZ()}, {-dir2.getOffsetX() + dir1.getOffsetX(),
                -dir2.getOffsetZ() + dir1.getOffsetZ()}, {dir1.getOffsetX(), dir1.getOffsetZ()},
                {dir2.getOffsetX() + dir1.getOffsetX(), dir2.getOffsetZ() + dir1.getOffsetZ()}};
    }

    private static int[][] getBedAboveStandUpOffsets(Direction direction) {
        return new int[][]{{0, 0}, {-direction.getOffsetX(), -direction.getOffsetZ()}};
    }
}
