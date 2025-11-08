package com.westerosblocks.item.client;


import mod.azure.azurelib.common.render.item.AzItemRenderer;
import mod.azure.azurelib.common.render.item.AzItemRendererConfig;
import net.minecraft.util.Identifier;

public class ModShieldRenderer extends AzItemRenderer {
    public ModShieldRenderer(Identifier geoPath, Identifier texPath) {
        super(
                AzItemRendererConfig.builder(geoPath, texPath).useNewOffset(true).build()
        );
    }
}
