package com.westerosblocks.mixin;

import com.westerosblocks.config.ModConfig;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "getSeaLevel()I", at = @At("TAIL"), cancellable = true)
    private void doGetSeaLevel(CallbackInfoReturnable<Integer> cir) {
        int seaLevelOverride = ModConfig.INSTANCE.seaLevelOverride;

        if (seaLevelOverride != 0) {
            cir.setReturnValue(seaLevelOverride);
        }
    }
}