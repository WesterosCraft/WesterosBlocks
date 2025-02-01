package com.westerosblocks.block.custom;

import com.mojang.serialization.MapCodec;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import com.westerosblocks.block.entity.custom.WCFurnaceBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WCFurnaceBlock extends AbstractFurnaceBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            boolean alwaysOn = def.getTypeValue("always-on").equals("true");
            AbstractBlock.Settings settings = def.makeBlockSettings()
                    .luminance(state -> (alwaysOn || state.get(Properties.LIT)) ?
                            (int)(16 * def.lightValue) : 0);

            Block block = new WCFurnaceBlock(settings, def);

            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, block), true, def.nonOpaque);
        }
    }

    private final WesterosBlockDef def;
    private final boolean alwaysOn;

    protected WCFurnaceBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        this.alwaysOn = def.getTypeValue("always-on").equals("true");
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(LIT, false));
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean lit = state.get(LIT);
        boolean active = alwaysOn || lit;

        if (active) {
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;

            if (random.nextDouble() < 0.1) {
                world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
                        SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double xOffset = random.nextDouble() * 0.6 - 0.3;
            double dx = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52 : xOffset;
            double dy = random.nextDouble() * 6.0 / 16.0;
            double dz = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : xOffset;

            world.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0, 0, 0);
            world.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0, 0, 0);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WCFurnaceBlockEntity(pos, state, def.blockName);
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> getCodec() {
        return null;
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WCFurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
