package com.westeroscraft.westerosblocks;

public class WesterosBlocksSoundDef {
    public String name;
    public String label;
    public String soundResourceName;
    public int numberOfSounds = 0; // If nonzero, sound set for resources 'soundResourceName1' to 'soundResourceNameN'
    public float volume = 1.0F;
    public float pitch = 1.0F;
    
    // Internal fields
    public transient String soundResourceID;
    
    public String getLabel() {
        if (label != null)
            return label;
        else
            return name;
    }
}
