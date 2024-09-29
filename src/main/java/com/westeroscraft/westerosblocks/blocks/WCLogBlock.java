package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCLogBlock extends RotatedPillarBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
            BlockBehaviour.Properties props = def.makeProperties();

            Block blk = new WCLogBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }
    }

    protected WesterosBlockDef def;

    protected WCLogBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static String[] TAGS = {"logs"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
