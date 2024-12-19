package com.westerosblocks.needsported.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;

import java.util.Random;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCTorchBlock extends TorchBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	BlockBehaviour.Properties floorprops = def.makeProperties().noCollission().instabreak();
        	Block floorblock = new WCTorchBlock(floorprops, def);
        	@SuppressWarnings("deprecation")
			BlockBehaviour.Properties wallprops = def.makeProperties().noCollission().instabreak().dropsLike(floorblock);
        	Block wallblock = new WCWallTorchBlock(wallprops, def);
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
    
    protected WCTorchBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(WCTorchBlock.getParticle(def.getType()), props);
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
