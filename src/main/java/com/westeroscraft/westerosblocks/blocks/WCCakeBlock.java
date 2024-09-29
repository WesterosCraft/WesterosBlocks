package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCCakeBlock extends CakeBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
            Block blk = new WCCakeBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCCakeBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
