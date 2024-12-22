package com.westerosblocks.block.custom;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Optional;

public class WCBedBlock extends HorizontalFacingBlock implements WesterosBlockLifecycle {
    public static final MapCodec<WCBedBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(propertiesCodec(instance))
            .apply(instance, WCBedBlock::new));

    @Override
    public MapCodec<WCBedBlock> getCodec() {
        return CODEC;
    }

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeProperties().nonOpaque();
            Block blk = new WCBedBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }


    public static final EnumProperty<BedPart> PART = Properties.BED_PART;
    public static final BooleanProperty OCCUPIED = Properties.OCCUPIED;
    protected static final int HEIGHT = 9;
    protected static final VoxelShape BASE = Block.createCuboidShape(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    protected static final VoxelShape LEG_NORTH_WEST = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D);
    protected static final VoxelShape LEG_SOUTH_WEST = Block.createCuboidShape(0.0D, 0.0D, 13.0D, 3.0D, 3.0D, 16.0D);
    protected static final VoxelShape LEG_NORTH_EAST = Block.createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 3.0D, 3.0D);
    protected static final VoxelShape LEG_SOUTH_EAST = Block.createCuboidShape(13.0D, 0.0D, 13.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape NORTH_SHAPE = VoxelShapes.union(BASE, LEG_NORTH_WEST, LEG_NORTH_EAST);
    protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(BASE, LEG_SOUTH_WEST, LEG_SOUTH_EAST);
    protected static final VoxelShape WEST_SHAPE = VoxelShapes.union(BASE, LEG_NORTH_WEST, LEG_SOUTH_WEST);
    protected static final VoxelShape EAST_SHAPE = VoxelShapes.union(BASE, LEG_NORTH_EAST, LEG_SOUTH_EAST);
    private final DyeColor color;

    private WesterosBlockDef def;

    public static enum BedType {
        NORMAL, RAISED, HAMMOCK
    }

    public final BedType bedType;

    protected WCBedBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
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
    public MapCodec<? extends Properties.HORIZONTAL_FACING> codec() {
        return null;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private static String[] TAGS = {"beds"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }


    @Nullable
    public static Direction getBedOrientation(BlockGetter p_49486_, BlockPos p_49487_) {
        BlockState blockstate = p_49486_.getBlockState(p_49487_);
        return blockstate.getBlock() instanceof BedBlock ? blockstate.get(FACING) : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (state.get(PART) != BedPart.HEAD) {
                pos = pos.relative(state.get(FACING));
                state = level.getBlockState(pos);
                if (!state.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(level)) {
                level.removeBlock(pos, false);
                BlockPos blockpos = pos.relative(state.get(FACING).getOpposite());
                if (level.getBlockState(blockpos).is(this)) {
                    level.removeBlock(blockpos, false);
                }

                level.explode((Entity) null, level.damageSources().badRespawnPointExplosion(blockpos.getCenter()), (ExplosionDamageCalculator) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, Level.ExplosionInteraction.BLOCK);
            } else if (state.get(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level, pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                player.startSleepInBed(pos).ifLeft((p_49477_) -> {
                    if (p_49477_ != null) {
                        player.displayClientMessage(p_49477_.getMessage(), true);
                    }

                });
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    //    public InteractionResult use(BlockState p_49515_, Level p_49516_, BlockPos p_49517_, Player p_49518_, InteractionHand p_49519_, BlockHitResult p_49520_) {
//        if (p_49516_.isClientSide) {
//            return InteractionResult.CONSUME;
//        } else {
//            if (p_49515_.getValue(PART) != BedPart.HEAD) {
//                p_49517_ = p_49517_.relative(p_49515_.getValue(FACING));
//                p_49515_ = p_49516_.getBlockState(p_49517_);
//                if (!p_49515_.is(this)) {
//                    return InteractionResult.CONSUME;
//                }
//            }
//
//            if (!canSetSpawn(p_49516_)) {
//                p_49516_.removeBlock(p_49517_, false);
//                BlockPos blockpos = p_49517_.relative(p_49515_.getValue(FACING).getOpposite());
//                if (p_49516_.getBlockState(blockpos).is(this)) {
//                    p_49516_.removeBlock(blockpos, false);
//                }
//
//                p_49516_.explode((Entity) null, p_49516_.damageSources().badRespawnPointExplosion(blockpos.getCenter()), (ExplosionDamageCalculator) null, (double) p_49517_.getX() + 0.5D, (double) p_49517_.getY() + 0.5D, (double) p_49517_.getZ() + 0.5D, 5.0F, true, Level.ExplosionInteraction.BLOCK);
//            } else if (p_49515_.getValue(OCCUPIED)) {
//                if (!this.kickVillagerOutOfBed(p_49516_, p_49517_)) {
//                    p_49518_.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
//                }
//
//                return InteractionResult.SUCCESS;
//            } else {
//                p_49518_.startSleepInBed(p_49517_).ifLeft((p_49477_) -> {
//                    if (p_49477_ != null) {
//                        p_49518_.displayClientMessage(p_49477_.getMessage(), true);
//                    }
//
//                });
//                return InteractionResult.SUCCESS;
//            }
//        }
//    }

    public static boolean canSetSpawn(Level p_49489_) {
        return p_49489_.dimensionType().bedWorks();
    }

    private boolean kickVillagerOutOfBed(Level p_49491_, BlockPos p_49492_) {
        List<Villager> list = p_49491_.getEntitiesOfClass(Villager.class, new AABB(p_49492_), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.get(0).stopSleeping();
            return true;
        }
    }

    public void fallOn(Level p_152169_, BlockState p_152170_, BlockPos p_152171_, Entity p_152172_, float p_152173_) {
        super.fallOn(p_152169_, p_152170_, p_152171_, p_152172_, p_152173_ * 0.5F);
    }

    public void updateEntityAfterFallOn(BlockGetter p_49483_, Entity p_49484_) {
        if (p_49484_.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(p_49483_, p_49484_);
        } else {
            this.bounceUp(p_49484_);
        }

    }

    private void bounceUp(Entity p_49457_) {
        Vec3 vec3 = p_49457_.getDeltaMovement();
        if (vec3.y < 0.0D) {
            double d0 = p_49457_ instanceof LivingEntity ? 1.0D : 0.8D;
            p_49457_.setDeltaMovement(vec3.x, -vec3.y * (double) 0.66F * d0, vec3.z);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState p_49525_, Direction p_49526_, BlockState p_49527_, LevelAccessor p_49528_, BlockPos p_49529_, BlockPos p_49530_) {
        if (p_49526_ == getNeighbourDirection(p_49525_.get(PART), p_49525_.get(FACING))) {
            return p_49527_.is(this) && p_49527_.get(PART) != p_49525_.get(PART) ? p_49525_.setValue(OCCUPIED, p_49527_.get(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(p_49525_, p_49526_, p_49527_, p_49528_, p_49529_, p_49530_);
        }
    }

    private static Direction getNeighbourDirection(BedPart p_49534_, Direction p_49535_) {
        return p_49534_ == BedPart.FOOT ? p_49535_ : p_49535_.getOpposite();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart bedpart = state.get(PART);
            if (bedpart == BedPart.FOOT) {
                BlockPos blockpos = pos.relative(getNeighbourDirection(bedpart, state.get(FACING)));
                BlockState blockstate = level.getBlockState(blockpos);
                if (blockstate.is(this) && blockstate.get(PART) == BedPart.HEAD) {
                    level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_49479_) {
        Direction direction = p_49479_.getHorizontalDirection();
        BlockPos blockpos = p_49479_.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        Level level = p_49479_.getLevel();
        return level.getBlockState(blockpos1).canBeReplaced(p_49479_) && level.getWorldBorder().isWithinBounds(blockpos1) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    public VoxelShape getShape(BlockState p_49547_, BlockGetter p_49548_, BlockPos p_49549_, CollisionContext p_49550_) {
        Direction direction = getConnectedDirection(p_49547_).getOpposite();
        switch (direction) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return EAST_SHAPE;
        }
    }

    public static Direction getConnectedDirection(BlockState p_49558_) {
        Direction direction = p_49558_.get(FACING);
        return p_49558_.get(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    public static DoubleBlockCombiner.BlockType getBlockType(BlockState p_49560_) {
        BedPart bedpart = p_49560_.get(PART);
        return bedpart == BedPart.HEAD ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND;
    }

    private static boolean isBunkBed(BlockGetter p_49542_, BlockPos p_49543_) {
        return p_49542_.getBlockState(p_49543_.below()).getBlock() instanceof BedBlock;
    }

    public static Optional<Vec3> findStandUpPosition(EntityType<?> p_49459_, CollisionGetter p_49460_, BlockPos p_49461_, float p_49462_) {
        Direction direction = p_49460_.getBlockState(p_49461_).get(FACING);
        Direction direction1 = direction.getClockWise();
        Direction direction2 = direction1.isFacingAngle(p_49462_) ? direction1.getOpposite() : direction1;
        if (isBunkBed(p_49460_, p_49461_)) {
            return findBunkBedStandUpPosition(p_49459_, p_49460_, p_49461_, direction, direction2);
        } else {
            int[][] aint = bedStandUpOffsets(direction, direction2);
            Optional<Vec3> optional = findStandUpPositionAtOffset(p_49459_, p_49460_, p_49461_, aint, true);
            return optional.isPresent() ? optional : findStandUpPositionAtOffset(p_49459_, p_49460_, p_49461_, aint, false);
        }
    }

    private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> p_49464_, CollisionGetter p_49465_, BlockPos p_49466_, Direction p_49467_, Direction p_49468_) {
        int[][] aint = bedSurroundStandUpOffsets(p_49467_, p_49468_);
        Optional<Vec3> optional = findStandUpPositionAtOffset(p_49464_, p_49465_, p_49466_, aint, true);
        if (optional.isPresent()) {
            return optional;
        } else {
            BlockPos blockpos = p_49466_.below();
            Optional<Vec3> optional1 = findStandUpPositionAtOffset(p_49464_, p_49465_, blockpos, aint, true);
            if (optional1.isPresent()) {
                return optional1;
            } else {
                int[][] aint1 = bedAboveStandUpOffsets(p_49467_);
                Optional<Vec3> optional2 = findStandUpPositionAtOffset(p_49464_, p_49465_, p_49466_, aint1, true);
                if (optional2.isPresent()) {
                    return optional2;
                } else {
                    Optional<Vec3> optional3 = findStandUpPositionAtOffset(p_49464_, p_49465_, p_49466_, aint, false);
                    if (optional3.isPresent()) {
                        return optional3;
                    } else {
                        Optional<Vec3> optional4 = findStandUpPositionAtOffset(p_49464_, p_49465_, blockpos, aint, false);
                        return optional4.isPresent() ? optional4 : findStandUpPositionAtOffset(p_49464_, p_49465_, p_49466_, aint1, false);
                    }
                }
            }
        }
    }

    private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> p_49470_, CollisionGetter p_49471_, BlockPos p_49472_, int[][] p_49473_, boolean p_49474_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int[] aint : p_49473_) {
            blockpos$mutableblockpos.set(p_49472_.getX() + aint[0], p_49472_.getY(), p_49472_.getZ() + aint[1]);
            Vec3 vec3 = DismountHelper.findSafeDismountLocation(p_49470_, p_49471_, blockpos$mutableblockpos, p_49474_);
            if (vec3 != null) {
                return Optional.of(vec3);
            }
        }

        return Optional.empty();
    }

    public PushReaction getPistonPushReaction(BlockState p_49556_) {
        return PushReaction.DESTROY;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49532_) {
        p_49532_.add(FACING, PART, OCCUPIED);
    }

    public void setPlacedBy(Level p_49499_, BlockPos p_49500_, BlockState p_49501_, @Nullable LivingEntity p_49502_, ItemStack p_49503_) {
        super.setPlacedBy(p_49499_, p_49500_, p_49501_, p_49502_, p_49503_);
        if (!p_49499_.isClientSide) {
            BlockPos blockpos = p_49500_.relative(p_49501_.get(FACING));
            p_49499_.setBlock(blockpos, p_49501_.setValue(PART, BedPart.HEAD), 3);
            p_49499_.blockUpdated(p_49500_, Blocks.AIR);
            p_49501_.updateNeighbourShapes(p_49499_, p_49500_, 3);
        }

    }

    public DyeColor getColor() {
        return this.color;
    }

    public long getSeed(BlockState p_49522_, BlockPos p_49523_) {
        BlockPos blockpos = p_49523_.relative(p_49522_.get(FACING), p_49522_.get(PART) == BedPart.HEAD ? 0 : 1);
        return Mth.getSeed(blockpos.getX(), p_49523_.getY(), blockpos.getZ());
    }

    public boolean isPathfindable(BlockState p_49510_, BlockGetter p_49511_, BlockPos p_49512_, PathComputationType p_49513_) {
        return false;
    }

    private static int[][] bedStandUpOffsets(Direction p_49539_, Direction p_49540_) {
        return ArrayUtils.addAll((int[][]) bedSurroundStandUpOffsets(p_49539_, p_49540_), (int[][]) bedAboveStandUpOffsets(p_49539_));
    }

    private static int[][] bedSurroundStandUpOffsets(Direction p_49552_, Direction p_49553_) {
        return new int[][]{{p_49553_.getStepX(), p_49553_.getStepZ()}, {p_49553_.getStepX() - p_49552_.getStepX(), p_49553_.getStepZ() - p_49552_.getStepZ()}, {p_49553_.getStepX() - p_49552_.getStepX() * 2, p_49553_.getStepZ() - p_49552_.getStepZ() * 2}, {-p_49552_.getStepX() * 2, -p_49552_.getStepZ() * 2}, {-p_49553_.getStepX() - p_49552_.getStepX() * 2, -p_49553_.getStepZ() - p_49552_.getStepZ() * 2}, {-p_49553_.getStepX() - p_49552_.getStepX(), -p_49553_.getStepZ() - p_49552_.getStepZ()}, {-p_49553_.getStepX(), -p_49553_.getStepZ()}, {-p_49553_.getStepX() + p_49552_.getStepX(), -p_49553_.getStepZ() + p_49552_.getStepZ()}, {p_49552_.getStepX(), p_49552_.getStepZ()}, {p_49553_.getStepX() + p_49552_.getStepX(), p_49553_.getStepZ() + p_49552_.getStepZ()}};
    }

    private static int[][] bedAboveStandUpOffsets(Direction p_49537_) {
        return new int[][]{{0, 0}, {-p_49537_.getStepX(), -p_49537_.getStepZ()}};
    }

}
