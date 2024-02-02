package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.WallTorchBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

public class WCWallTorchBlock extends WallTorchBlock implements WesterosBlockLifecycle {
    private WesterosBlockDef def;
    private boolean allow_unsupported = false;

    private static ParticleOptions getParticle(String typeStr) {
        if (typeStr != null && typeStr.contains("no-particle")) {
            return new SimpleParticleType(false);
        }
        return ParticleTypes.FLAME;
    }
    
    protected WCWallTorchBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, WCWallTorchBlock.getParticle(def.getType()));
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
            }
        }
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
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
