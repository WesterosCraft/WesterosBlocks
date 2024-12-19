//package com.westerosblocks.needsported.mixin;
//
//import com.westeroscraft.westerosblocks.Config;
//import net.minecraft.client.renderer.DimensionSpecialEffects;
//import net.minecraft.world.level.Level;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import com.westeroscraft.westerosblocks.WesterosBlocks;
//
//@Mixin(Level.class)
//public abstract class MixinLevel
//{
//	// This constructor is fake and never used
//	protected MixinLevel()
//	{
//	}
//
//	@Inject(method = "getSeaLevel()I", at = @At("TAIL"), cancellable=true)
//	private void doGetSeaLevel(CallbackInfoReturnable<Integer> ci) {
//		Integer override = Config.seaLevelOverride;
//		if (override.intValue() != 0) {
//			ci.setReturnValue(override);
//		}
//	}
//
//}
