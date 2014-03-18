package com.westeroscraft.westerosblocks.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion(value = "1.6.4")
@TransformerExclusions({ "com.westeroscraft.westerosblocks.asm" })
public class FMLLoadingPlugin  implements cpw.mods.fml.relauncher.IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ WorldTypeClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        
    }

}
