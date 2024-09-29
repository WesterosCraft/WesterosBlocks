package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;


public class WCFenceGateBlock extends FenceGateBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	BlockBehaviour.Properties props = def.makeProperties();
            Block blk = new WCFenceGateBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }
    }
    
    private WesterosBlockDef def;
    private boolean locked = false;

    protected WCFenceGateBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(AuxileryUtils.getWoodType(props, def), props);
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.locked) {
            if (player.isCreative() && player.getMainHandItem().isEmpty()) {
                return super.useWithoutItem(state, level, pos, player, hitResult);
            }
            else {
                return InteractionResult.PASS;
            }
        }
        else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    private static String[] TAGS = { "fence_gates" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
