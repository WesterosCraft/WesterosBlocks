package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WCParticleBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            BlockBehaviour.Properties props = def.makeProperties();

            return def.registerRenderType(def.registerBlock(new WCParticleBlock(props, def)), true, def.nonOpaque);
        }
    }



    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
}
