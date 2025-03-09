package com.westerosblocks.mixin;

import net.minecraft.client.render.DimensionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO not sure if we really need this, handled in datapack now
@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {
    @Inject(method = "getCloudsHeight()F", at = @At("TAIL"), cancellable = true)
    private void modifyCloudHeight(CallbackInfoReturnable<Float> cir) {
        float currentHeight = cir.getReturnValue();
        if (currentHeight < 639.0F) {
            cir.setReturnValue(639.0F);
        }
    }
}
