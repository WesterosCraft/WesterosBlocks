package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCTorchBlock extends TorchBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings floorBlockSettings = def.makeBlockSettings().noCollision().breakInstantly();
            AbstractBlock.Settings wallBlockSettings = def.makeBlockSettings().noCollision().breakInstantly();
            Block wallTorch = new WCWallTorchBlock(wallBlockSettings, def);
            Block floorTorch = new WCTorchBlock(floorBlockSettings, def, wallTorch);

            def.registerRenderType(ModBlocks.registerBlock("wall_" + def.blockName, wallTorch), false, false);
            ModBlocks.getCustomBlocksByName().put("wall_" + def.blockName, wallTorch);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, floorTorch), false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean allow_unsupported = false;
    private boolean no_particle = false;

    private static SimpleParticleType getParticle(String typeStr) {
        if (typeStr != null && typeStr.contains("no-particle")) {
            //TODO
            return ParticleTypes.SMOKE;
//            return new SimpleParticleType(false);
        }
        return ParticleTypes.FLAME;
    }

    private final Block wallBlock;
    protected WCTorchBlock(AbstractBlock.Settings settings, WesterosBlockDef def, Block wallTorch) {
        super(WCTorchBlock.getParticle(def.getType()), settings);
        this.def = def;
        this.wallBlock = wallTorch;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                else if (tok.equals("no-particle")) {
                    no_particle = true;
                }
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        FluidState fluidState = world.getFluidState(pos);

        // Check for wall placement first
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                BlockPos attachPos = pos.offset(opposite);
                if (world.getBlockState(attachPos).isSideSolidFullSquare(world, attachPos, direction)) {
                    return this.wallBlock.getDefaultState()
                            .with(WCWallTorchBlock.FACING, direction);
                }
            }
        }

        // If not wall placement, return normal torch state
        return state;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!this.no_particle) super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    	if (this.allow_unsupported) return true;
        return super.canPlaceAt(state, world, pos);
    }

    private static String[] TAGS = { "wall_post_override" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
