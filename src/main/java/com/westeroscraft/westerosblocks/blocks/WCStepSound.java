package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockStepSound;

import net.minecraft.block.StepSound;

public class WCStepSound extends StepSound {
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
    
    public String getBreakSound()
    {
        return this.breakSound;
    }

    public String getStepSound()
    {
        return this.stepSoundName;
    }

    public String getPlaceSound()
    {
        return this.placeSound;
    }
}
