package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class WCDoorBlock extends DoorBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties();
            Block blk = new WCDoorBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean locked = false;
    private boolean allow_unsupported = false;

    protected WCDoorBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(AuxileryUtils.getBlockSetType(settings, def), settings);
        this.def = def;
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
            	if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {


        if (this.locked) {
            return InteractionResult.PASS;
        }
        else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    public boolean canSurvive(BlockState p_52783_, LevelReader p_52784_, BlockPos p_52785_) {
    	if (allow_unsupported && (p_52783_.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER)) return true;
    	return super.canSurvive(p_52783_, p_52784_, p_52785_);
     }

    private static String[] TAGS = { "doors" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
