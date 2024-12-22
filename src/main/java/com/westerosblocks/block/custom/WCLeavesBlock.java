package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;

public class WCLeavesBlock extends LeavesBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
                def.lightOpacity = 1;
            }
            AbstractBlock.Settings settings = def.makeProperties().noOcclusion().isSuffocating((state, reader, pos) -> false).isViewBlocking((state, reader, pos) -> false);
            Block blk = new WCLeavesBlock(settings, def);
            return def.registerRenderType(blk, true, true);
        }
    }
    protected WesterosBlockDef def;
    private final boolean nodecay;
    public final boolean betterfoliage;
    public final boolean overlay;
    
    protected WCLeavesBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String typ = def.getType();
    	betterfoliage = (typ != null) && typ.contains("better-foliage");
    	overlay = (typ != null) && typ.contains("overlay");
    	nodecay = (typ != null) && typ.contains("no-decay");
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, Integer.valueOf(7)).setValue(PERSISTENT, Boolean.valueOf(!nodecay)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static String[] TAGS = { "leaves" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
}
