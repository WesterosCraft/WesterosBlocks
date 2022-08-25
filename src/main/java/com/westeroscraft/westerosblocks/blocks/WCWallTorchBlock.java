package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.WallTorchBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

public class WCWallTorchBlock extends WallTorchBlock implements WesterosBlockLifecycle {
    private WesterosBlockDef def;
    
    protected WCWallTorchBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, ParticleTypes.FLAME);
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    private static String[] TAGS = { "wall_post_override" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
