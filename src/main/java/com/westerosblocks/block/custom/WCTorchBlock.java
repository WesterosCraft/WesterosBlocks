package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
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

public class WCTorchBlock extends TorchBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings floorBlockSettings = def.makeProperties().noCollision().breakInstantly();
        	Block floorblock = new WCTorchBlock(floorBlockSettings, def);
        	@SuppressWarnings("deprecation")
            AbstractBlock.Settings wallBlockSettings = def.makeProperties().noCollision().breakInstantly().dropsLike(floorblock);
        	Block wallblock = new WCWallTorchBlock(wallBlockSettings, def);
//        	def.registerWallOrFloorBlock(floorblock, wallblock, helper);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), floorblock);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, "wall_" + def.blockName), wallblock);
            def.registerBlockItem(def.blockName, floorblock);
            def.registerBlockItem("wall_" + def.blockName, wallblock);
            def.registerRenderType(floorblock, false, false);
        	def.registerRenderType(wallblock, false, false);
        	return floorblock;
        }
    }
    
    private WesterosBlockDef def;
    private boolean allow_unsupported = false;
    private boolean no_particle = false;

    private static SimpleParticleType getParticle(String typeStr) {
        if (typeStr != null && typeStr.contains("no-particle")) {
            return new SimpleParticleType(false);
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rnd) {
        if (!this.no_particle) super.animateTick(state, level, pos, rnd);
    }

    @Override
    public boolean canSurvive(BlockState p_49395_, LevelReader p_49396_, BlockPos p_49397_) {
    	if (this.allow_unsupported) return true;
        return super.canSurvive(p_49395_, p_49396_, p_49397_);
    }

    private static String[] TAGS = { "wall_post_override" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
