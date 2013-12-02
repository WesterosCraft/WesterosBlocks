package com.westeroscraft.westerosblocks;

import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModSupportAPI;
import org.dynmap.modsupport.ModTextureDefinition;

public class DynmapSupport {
    private ModSupportAPI api;
    private ModTextureDefinition tdef;
    private ModModelDefinition mdef;
    
    public DynmapSupport(String modid, String modver) {
        api = ModSupportAPI.getAPI();
        // If API available, get texture definition for our mod
        if (api != null) {
            tdef = api.getModTextureDefinition(modid, modver);
            if (tdef != null) {
                mdef = tdef.getModelDefinition();
            }
        }
    }
    
    public final ModTextureDefinition getTextureDef() {
        return tdef;
    }
    public final ModModelDefinition getModelDef() {
        return mdef;
    }
    
    // Publish completed definitions 
    public boolean complete() {
        if (tdef != null) {
            tdef.publishDefinition();
            tdef = null;
        }
        if (mdef != null) {
            mdef.publishDefinition();
            mdef = null;
        }
        return true;
    }
}
