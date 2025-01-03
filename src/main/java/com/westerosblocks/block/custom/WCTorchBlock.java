package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WCTorchBlock extends TorchBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings floorBlockSettings = def.makeBlockSettings().noCollision().breakInstantly();
        	Block floorblock = new WCTorchBlock(floorBlockSettings, def);

            AbstractBlock.Settings wallBlockSettings = def.makeBlockSettings().noCollision().breakInstantly().dropsLike(floorblock);
            //TODO
//        	Block wallblock = new WCWallTorchBlock(wallBlockSettings, def);

            def.registerRenderType(ModBlocks.registerBlock(def.blockName, floorblock), false, false);
//        	def.registerRenderType(ModBlocks.registerBlock(def.blockName, wallblock), false, false);
        	return floorblock;
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
    
    protected WCTorchBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(WCTorchBlock.getParticle(def.getType()), settings);
        this.def = def;
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
