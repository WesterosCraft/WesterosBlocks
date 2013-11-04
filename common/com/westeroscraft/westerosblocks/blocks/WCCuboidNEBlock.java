package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCCuboidNEBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x7);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCCuboidNEBlock(def) };
        }
    }
    
    private List<WesterosBlockDef.Cuboid> cuboids_by_meta[];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNEBlock(WesterosBlockDef def) {
        super(def);
        cuboids_by_meta = (List<WesterosBlockDef.Cuboid>[])new List[16];
        for (int i = 0; i < 8; i++) {
            List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(i);
            if (lst == null) continue;
            cuboids_by_meta[i] = lst;
            cuboids_by_meta[i+8] = new ArrayList<WesterosBlockDef.Cuboid>();
            for (WesterosBlockDef.Cuboid c : lst) {
                cuboids_by_meta[i+8].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
            }
            setBoundingBoxFromCuboidList(i+8);
        }
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCCuboidNEItem.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta & 0x7;
    }
        
    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        return cuboids_by_meta[meta];
    }
    
    /**
     * Set active cuboid during render
     */
    @Override
    public void setActiveRenderCuboid(WesterosBlockDef.Cuboid c, RenderBlocks renderer, int meta) {
        super.setActiveRenderCuboid(c, renderer, meta);
        int dir = (meta >> 3);
        if ((c != null) && (dir == 1)) {
            renderer.uvRotateTop = 1;
            renderer.uvRotateBottom = 2;
        }
        else {
            renderer.uvRotateTop = renderer.uvRotateBottom = 0;
        }
    }

}
