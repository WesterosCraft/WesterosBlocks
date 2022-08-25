package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.TrapDoorBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCTrapDoorBlock extends TrapDoorBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCTrapDoorBlock(props, def)), false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean locked = false;
    
    protected WCTrapDoorBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
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
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
		BlockHitResult ctx) {
        if (this.locked) {
           return InteractionResult.PASS;
        }
        else {
        	return super.use(state, world, pos, player, hand, ctx);
        }
    }

    private static String[] TAGS = { "trapdoors" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
