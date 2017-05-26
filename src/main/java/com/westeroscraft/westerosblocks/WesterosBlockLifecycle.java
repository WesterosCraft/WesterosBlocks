package com.westeroscraft.westerosblocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Used to define the interfaces for properly priming one of our custom block definitions
//   Block definitions must also have constructor with WesterosBlockDef as parameter
public interface WesterosBlockLifecycle {
    
    /**
     * Initialize block : called right after block class is constructed
     * 
     * @return true if successfully initialized, false if not (this includes if the constructor failed)
     */
    public boolean initializeBlockDefinition();
    /**
     * Register block : this is called after all of our block definitions have been initialized.
     * Used for GameRegistry and LanguageRegistry calls
     * @return true if successfully registered, false if not
     */
    public boolean registerBlockDefinition();
    /**
     * Get definition for block
     */
    public WesterosBlockDef getWBDefinition();
    /**
     * Block coloring handlers
     */
	@SideOnly(Side.CLIENT)
    public IBlockColor getBlockColor();
	/**
	 * Get nonrendering properties
	 */
	public IProperty<?>[] getNonRenderingProperties();
}
