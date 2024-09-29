package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCSandBlock extends ColoredFallingBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	BlockBehaviour.Properties props = def.makeProperties();
            Block blk = new WCSandBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, true, false);
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCSandBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(new ColorRGBA(14406560), props);	// TODO: configurable dust color
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    private static String[] TAGS = { "sand" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
