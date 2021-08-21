package com.westeroscraft.westerosblocks.mixin;

import net.minecraft.client.world.DimensionRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionRenderInfo.class) 
public abstract class MixinDimensionRenderInfo
{	
	@Shadow float cloudLevel;

	// This constructor is fake and never used
	protected MixinDimensionRenderInfo()
	{
	}

	@Inject(method = "getCloudHeight()F", at = @At("TAIL"), cancellable=true)	
	private void doGetCloudHeight(CallbackInfoReturnable<Float> ci) {
		if (cloudLevel == 128.0F) {
			ci.setReturnValue(255.0F);
		}
	}

}
