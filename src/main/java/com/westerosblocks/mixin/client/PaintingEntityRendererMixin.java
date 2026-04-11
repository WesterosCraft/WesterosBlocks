package com.westerosblocks.mixin.client;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Changes painting rendering from entitySolid to entityTranslucentCull so that
 * alpha channels in painting textures (and back.png) are respected. Without this,
 * transparent pixels in painting textures render as opaque black in 1.21.1.
 */
@Environment(EnvType.CLIENT)
@Mixin(PaintingEntityRenderer.class)
public class PaintingEntityRendererMixin {

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer westerosblocks$redirectPaintingToTranslucent(Identifier textureId) {
        if (WesterosBlocks.CONFIG != null && WesterosBlocks.CONFIG.translucencyPaintings) {
            return RenderLayer.getEntityTranslucentCull(textureId);
        }
        return RenderLayer.getEntitySolid(textureId);
    }
}
