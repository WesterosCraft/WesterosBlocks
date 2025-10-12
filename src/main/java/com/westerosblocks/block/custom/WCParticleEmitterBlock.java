package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class WCParticleEmitterBlock extends Block implements Waterloggable {
    protected static final VoxelShape OFF_SHAPE = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    protected static final VoxelShape ON_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final ParticleEffect particleType;
    private final String particleName;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            String particleName = definition != null ? definition.getParticle() : "flame";

            return new WCParticleEmitterBlock(settings, particleName);
        }
    }

    public WCParticleEmitterBlock(AbstractBlock.Settings settings, String particleName) {
        super(settings);
        this.particleName = particleName;
        this.particleType = particleName != null ? getParticleFromName(particleName) : null;
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false).with(POWERED, false));
    }

    private ParticleEffect getParticleFromName(String name) {
        return switch (name.toLowerCase()) {
            case "flame" -> ParticleTypes.FLAME;
            case "smoke" -> ParticleTypes.SMOKE;
            case "large_smoke" -> ParticleTypes.LARGE_SMOKE;
            case "cloud" -> ParticleTypes.CLOUD;
            case "white_ash" -> ParticleTypes.WHITE_ASH;
            case "dripping_water" -> ParticleTypes.DRIPPING_WATER;
            case "dripping_lava" -> ParticleTypes.DRIPPING_LAVA;
            case "falling_water" -> ParticleTypes.FALLING_WATER;
            case "falling_lava" -> ParticleTypes.FALLING_LAVA;
            case "soul_fire_flame" -> ParticleTypes.SOUL_FIRE_FLAME;
            case "crimson_spore" -> ParticleTypes.CRIMSON_SPORE;
            case "warped_spore" -> ParticleTypes.WARPED_SPORE;
            case "ash" -> ParticleTypes.ASH;
            case "campfire_cosy_smoke" -> ParticleTypes.CAMPFIRE_COSY_SMOKE;
            case "campfire_signal_smoke" -> ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
            default -> ParticleTypes.FLAME;
        };
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(POWERED) && particleType != null && particleName != null) {
            if (particleName.contains("smoke") || particleName.contains("cosy") || particleName.contains("signal")) {
                if (random.nextInt(3) > 0) {
                    double x = pos.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1);
                    double y = pos.getY() + random.nextDouble() + random.nextDouble();
                    double z = pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1);
                    world.addParticle(particleType, x, y, z, 0.0D, 0.07D, 0.0D);
                }
            } else if (particleName.contains("spore")) {
                if (random.nextInt(4) == 0) {
                    double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.8;
                    double y = pos.getY() + 0.2;
                    double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.8;
                    float vx = (random.nextFloat() - 0.5f) * 0.05f;
                    float vy = random.nextFloat() * 0.02f;
                    float vz = (random.nextFloat() - 0.5f) * 0.05f;
                    world.addParticle(particleType, x, y, z, vx, vy, vz);
                }
            } else if (particleName.contains("dripping") || particleName.contains("falling")) {
                if (random.nextInt(2) == 0) {
                    double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                    double y = pos.getY() + 0.8;
                    double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                    float vx = (random.nextFloat() - 0.5f) * 0.1f;
                    float vy = random.nextFloat() * 0.1f;
                    float vz = (random.nextFloat() - 0.5f) * 0.1f;
                    world.addParticle(particleType, x, y, z, vx, vy, vz);
                }
            } else {
                double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                double y = pos.getY() + 0.5;
                double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                world.addParticle(particleType, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(POWERED) ? ON_SHAPE : OFF_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(POWERED) ? VoxelShapes.empty() : OFF_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, POWERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(POWERED, false).with(WATERLOGGED, fluidState.isIn(FluidTags.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(POWERED);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }
}
