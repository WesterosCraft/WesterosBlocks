package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCCuboidNSEWUDBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x1);
            if (!def.validateMetaValues(new int[] { 0, 1 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCCuboidNSEWUDBlock(def) };
        }
    }
    
    private List<WesterosBlockDef.Cuboid> cuboids_by_meta[];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNSEWUDBlock(WesterosBlockDef def) {
        super(def);
        // Set rotations for effects
        this.metaRotations = new WesterosBlockDef.CuboidRotation[] { WesterosBlockDef.CuboidRotation.NONE,
            WesterosBlockDef.CuboidRotation.ROTY90, WesterosBlockDef.CuboidRotation.ROTY180, WesterosBlockDef.CuboidRotation.ROTY270 };

        cuboids_by_meta = (List<WesterosBlockDef.Cuboid>[])new List[16];
        for (int i = 0; i < 2; i++) {
            List<WesterosBlockDef.Cuboid> lst = def.getCuboidList(i);
            if (lst == null) continue;
            cuboids_by_meta[i] = lst;
            cuboids_by_meta[i+2] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+4] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+6] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+8] = new ArrayList<WesterosBlockDef.Cuboid>();
            cuboids_by_meta[i+10] = new ArrayList<WesterosBlockDef.Cuboid>();
            for (WesterosBlockDef.Cuboid c : lst) {
                cuboids_by_meta[i+2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboids_by_meta[i+4].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboids_by_meta[i+6].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
                cuboids_by_meta[i+8].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTZ90));
                cuboids_by_meta[i+10].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTZ270));
            }
            setBoundingBoxFromCuboidList(i+2);
            setBoundingBoxFromCuboidList(i+4);
            setBoundingBoxFromCuboidList(i+6);
            setBoundingBoxFromCuboidList(i+8);
            setBoundingBoxFromCuboidList(i+10);
        }
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCCuboidNSEWUDItem.class);
        
        return true;
    }
    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return WesterosBlocks.cuboidNSEWUDRenderID;
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta & 1;
    }
    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        return cuboids_by_meta[meta];
    }
    
    @SideOnly(Side.CLIENT)
    protected IIcon getIconInternal(int side, int meta) {
        int[] sidemap = null;
        if (this.currentCuboid != null) {
            sidemap = this.currentCuboid.sideTextures;
        }
        int nside = side;
        if (sidemap != null) {
            if (nside >= sidemap.length) {
                nside = sidemap.length - 1;
            }
            nside = sidemap[nside];
        }
        return def.doStandardIconGet(nside, meta);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
}
