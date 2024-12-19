package com.westerosblocks.needsported.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCLeavesBlock extends LeavesBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
            if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
                def.lightOpacity = 1;
            }
        	BlockBehaviour.Properties props = def.makeProperties().noOcclusion().isSuffocating((state, reader, pos) -> false).isViewBlocking((state, reader, pos) -> false);
            Block blk = new WCLeavesBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, true, true);
        }
    }
    protected WesterosBlockDef def;
    private final boolean nodecay;
    public final boolean betterfoliage;
    public final boolean overlay;
    
    protected WCLeavesBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
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
