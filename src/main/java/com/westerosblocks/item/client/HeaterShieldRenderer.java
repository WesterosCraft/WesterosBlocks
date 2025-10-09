package com.westerosblocks.item.client;

import mod.azure.azurelib.rewrite.render.item.AzItemRenderer;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererConfig;
import net.minecraft.util.Identifier;

public class HeaterShieldRenderer extends AzItemRenderer {
    public HeaterShieldRenderer(Identifier geoPath, Identifier texPath) {
        super(
                AzItemRendererConfig.builder(geoPath, texPath).useNewOffset(true).build()
        );
    }
}
