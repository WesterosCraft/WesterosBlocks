package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;


public class WCDoorBlock extends DoorBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCDoorBlock(props, def)), false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean locked = false;
    private boolean allow_unsupported = false;

    protected WCDoorBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
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
    public InteractionResult use(BlockState p_225533_1_, Level p_225533_2_, BlockPos p_225533_3_, Player p_225533_4_, InteractionHand p_225533_5_, BlockHitResult p_225533_6_) {
        if (this.locked) {
           return InteractionResult.PASS;
        }
        else {
        	return super.use(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
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
