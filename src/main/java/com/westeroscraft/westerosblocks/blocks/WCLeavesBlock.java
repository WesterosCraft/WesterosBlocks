package com.westeroscraft.westerosblocks.blocks;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCLeavesBlock extends LeavesBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
                def.lightOpacity = 1;
            }
        	AbstractBlock.Properties props = def.makeProperties().noOcclusion().isSuffocating((state, reader, pos) -> false).isViewBlocking((state, reader, pos) -> false);
        	return def.registerRenderType(def.registerBlock(new WCLeavesBlock(props, def)), true, true);
        }
    }
    protected WesterosBlockDef def;
    private boolean nodecay;
    
    protected WCLeavesBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String typ = def.getType();
        if ((typ != null) && (typ.contains("no-decay"))) {
            nodecay = true;
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, Integer.valueOf(7)).setValue(PERSISTENT, Boolean.valueOf(!nodecay)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        String blkname = def.getBlockName();
        String topbot = def.getTextureByIndex(2);
        String sides = def.getTextureByIndex(3);
        BlockTextureRecord btr = mtd.addBlockTextureRecord(blkname);
        btr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        btr.setSideTexture(topbot, BlockSide.TOP);
        btr.setSideTexture(topbot, BlockSide.BOTTOM);
        btr.setSideTexture(sides, BlockSide.ALLSIDES);
        def.setBlockColorMap(btr);
    }
    private static String[] TAGS = { "leaves" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
}
