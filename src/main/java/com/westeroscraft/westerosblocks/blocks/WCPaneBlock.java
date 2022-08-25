package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCPaneBlock extends IronBarsBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties().noOcclusion();
        	return def.registerRenderType(def.registerBlock(new WCPaneBlock(props, def)), false, true);
        }
    }
    
    private WesterosBlockDef def;
    private boolean legacy_model;
    private boolean bars_model;

    protected WCPaneBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("legacy-model")) {
                    legacy_model = true;
                }
                if (tok.equals("bars-model")) {
                    bars_model = true;
                }
            }
        }
    }
    
    public boolean isLegacyModel() {
    	return legacy_model;
    }

    public boolean isBarsModel() {
        return bars_model;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    
    private static String[] TAGS = {  };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
