package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockStepSound;

import net.minecraft.block.Block;

public class WCStepSound extends Block.SoundType {
    private String breakSound;
    private String placeSound;

    public WCStepSound(WesterosBlockStepSound ss) {
        super(ss.stepSound, ss.volume, ss.pitch);
        if (ss.breakSound != null) {
            this.breakSound = ss.breakSound;
        }
        else {
            this.breakSound = ss.stepSound;
        }
        if (ss.placeSound != null) {
            this.placeSound = ss.placeSound;
        }
        else {
            this.placeSound = this.breakSound;
        }
    }
    
    @Override
    public String getBreakSound()
    {
        return this.breakSound;
    }

    @Override
    public String getStepResourcePath()
    {
        return this.soundName;
    }

    @Override
    public String func_150496_b()
    {
        return this.placeSound;
    }
}
