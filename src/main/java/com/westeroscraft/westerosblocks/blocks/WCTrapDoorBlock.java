package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.TrapDoorBlock;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCTrapDoorBlock extends TrapDoorBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCTrapDoorBlock(props, def)), false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean locked = false;
    
    protected WCTrapDoorBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String [] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("locked")) {
                    locked = flds[1].equals("true");
                }
            }
        }
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (this.locked) {
           return ActionResultType.PASS;
        }
        else {
        	return super.use(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
        }
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, 0, (def.alphaRender ? TransparencyMode.TRANSPARENT : TransparencyMode.OPAQUE));
    }
    private static String[] TAGS = { "trapdoors" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
