package com.westeroscraft.westerosblocks.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.world.DimensionRenderInfo;

@Mixin(DimensionRenderInfo.Overworld.class) 
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
