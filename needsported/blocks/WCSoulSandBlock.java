package com.westerosblocks.needsported.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoulSandBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCSoulSandBlock extends SoulSandBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	BlockBehaviour.Properties props = def.makeProperties();
            Block blk = new WCSoulSandBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, true, false);
        }
    }
    
    private WesterosBlockDef def;

    protected WCSoulSandBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static String[] TAGS = { "soul_speed_blocks" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
