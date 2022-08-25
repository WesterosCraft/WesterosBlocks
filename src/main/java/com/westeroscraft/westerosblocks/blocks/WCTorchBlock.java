package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCTorchBlock extends TorchBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties floorprops = def.makeProperties().noCollission().instabreak();
        	Block floorblock = new WCTorchBlock(floorprops, def);
        	BlockBehaviour.Properties wallprops = def.makeProperties().noCollission().instabreak().dropsLike(floorblock);
        	Block wallblock = new WCWallTorchBlock(wallprops, def);
        	def.registerWallOrFloorBlock(floorblock, wallblock);
        	
        	def.registerRenderType(floorblock, false, false);
        	def.registerRenderType(wallblock, false, false);
        	return floorblock;
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCTorchBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
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
