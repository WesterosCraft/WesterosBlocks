package com.westeroscraft.westerosblocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.westeroscraft.westerosblocks.blocks.WCFluidCTMRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

public class FluidCTMFix extends BlockStationary {
    private static Method ctmMethod = null;
    
    public static boolean checkForCTMSupport() {
        Class<?> cls;
        try {
            cls = Class.forName("com.prupe.mcpatcher.ctm.CTMUtils");
            // public static Icon getTile(RenderBlocks renderBlocks, Block block, int i, int j, int k, int face, Icon icon, Tessellator tessellator) {
            ctmMethod = cls.getDeclaredMethod("getTile", new Class[] { RenderBlocks.class, Block.class, int.class, int.class, int.class, int.class, Icon.class, Tessellator.class });
            WesterosBlocks.log.info("CTM support found for water fix");
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        return (ctmMethod != null);
    }
    
    public FluidCTMFix(int id, Material mat) {
        super(id, mat);
        disableStats();
    }
    @Override
    public int getRenderType() {
        return WesterosBlocks.fluidCTMRenderID;    // Use custom to make inventory render correctly
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta)
    {
        Icon ico = super.getIcon(side, meta);
        if ((side == 1) && (ctmMethod != null) && (WCFluidCTMRenderer.World != null)) {
            try {
                ico = (Icon) ctmMethod.invoke(null, WCFluidCTMRenderer.Renderer, this, WCFluidCTMRenderer.X,
                        WCFluidCTMRenderer.Y, WCFluidCTMRenderer.Z, side, ico, WCFluidCTMRenderer.Tessell);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return ico;
    }

}
