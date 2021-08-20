package com.westeroscraft.westerosblocks.mixin;

import net.minecraft.client.world.DimensionRenderInfo;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(DimensionRenderInfo.class) 
public abstract class MixinDimensionRenderInfo
{	
	// This constructor is fake and never used
	protected MixinDimensionRenderInfo()
	{
	}
	
	// Replace method
	public float getCloudHeight()
	{
		return 255.0f;
	}
}
