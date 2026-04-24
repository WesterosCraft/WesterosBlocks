package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.block.blockentity.custom.WCFurnaceBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


public class WCFurnaceBlock extends FurnaceBlock implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean alwaysOn;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            boolean alwaysOn = definition.isAlwaysOn();
            int light = definition.getLuminance();
            if (light > 0) {
                settings = settings.luminance(state -> {
                    if (alwaysOn) return light;
                    return state.contains(LIT) && state.get(LIT) ? light : 0;
                });
            }
            return new WCFurnaceBlock(settings, definition, alwaysOn);
        }
    }

    protected WCFurnaceBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean alwaysOn) {
        super(settings);
        this.def = def;
        this.alwaysOn = alwaysOn;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(LIT, false));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean lit = state.get(LIT);
        boolean active = alwaysOn || lit;

        if (active) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.1D) {
                world.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
                        SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double d4 = random.nextDouble() * 0.6D - 0.3D;
            double d5 = axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52D : d4;
            double d6 = random.nextDouble() * 6.0D / 16.0D;
            double d7 = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52D : d4;

            world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        String actualBlockName = Registries.BLOCK.getId(this).getPath();
        return new WCFurnaceBlockEntity(pos, state, actualBlockName);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NamedScreenHandlerFactory) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
