//package com.westerosblocks.needsported.mixin;
//
//import net.minecraft.client.renderer.DimensionSpecialEffects;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(DimensionSpecialEffects.class)
//public abstract class MixinDimensionSpecialEffects
//{
//	@Shadow float cloudLevel;
//
//	// This constructor is fake and never used
//	protected MixinDimensionSpecialEffects()
//	{
//	}
//
//	@Inject(method = "getCloudHeight()F", at = @At("TAIL"), cancellable=true)
//	private void doGetCloudHeight(CallbackInfoReturnable<Float> ci) {
//		if (cloudLevel < 639.0F) {
//			ci.setReturnValue(639.0F);
//		}
//	}
//
//}
