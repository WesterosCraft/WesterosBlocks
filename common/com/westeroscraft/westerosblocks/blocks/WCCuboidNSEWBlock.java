package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCCuboidNSEWBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x3);
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCCuboidNSEWBlock(def) };
        }
    }
    
    private List<WesterosBlockDef.Cuboid> cuboids_by_meta[];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNSEWBlock(WesterosBlockDef def) {
        super(def);
        cuboids_by_meta = (List<WesterosBlockDef.Cuboid>[])new List[16];
        for (int i = 0; i < 4; i++) {
            List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(i);
            if (lst == null) continue;
            cuboids_by_meta[i] = lst;
            cuboids_by_meta[i+4] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+8] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+12] = new ArrayList<WesterosBlockDef.Cuboid>();
            for (WesterosBlockDef.Cuboid c : lst) {
                cuboids_by_meta[i+4].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboids_by_meta[i+8].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboids_by_meta[i+12].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
            }
            setBoundingBoxFromCuboidList(i+4);
            setBoundingBoxFromCuboidList(i+8);
            setBoundingBoxFromCuboidList(i+12);
        }
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCCuboidNSEWItem.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, id, tab, list);
    }

    @Override
    public int damageDropped(int meta) {
        return meta & 3;
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
    public void setActiveRenderCuboid(WesterosBlockDef.Cuboid c, RenderBlocks renderer, int meta, int index) {
        super.setActiveRenderCuboid(c, renderer, meta, index);
        int dir = (meta >> 2);
        if (c != null) {
            if (dir == 1) {
                renderer.uvRotateTop = 1;
                renderer.uvRotateBottom = 1;
            }
            else if (dir == 2) {
                renderer.uvRotateTop = 3;
                renderer.uvRotateBottom = 3;
            }
            else if (dir == 3) {
                renderer.uvRotateTop = 2;
                renderer.uvRotateBottom = 2;
            }
            else {
                renderer.uvRotateTop = renderer.uvRotateBottom = 0;
            }
        }
        else {
            renderer.uvRotateTop = renderer.uvRotateBottom = 0;
        }
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
}
