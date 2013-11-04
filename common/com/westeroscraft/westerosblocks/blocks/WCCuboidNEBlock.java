package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
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
        }
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, WCCuboidNEItem.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return super.getIcon(side, meta & 0x7);
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
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, metadata & 0x7, face);
    }
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, metadata & 0x7, face);
    }
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return def.getLightValue(world, x, y, z, 0x7);
    }
    @Override
    public int getLightOpacity(World world, int x, int y, int z) {
        return def.getLightOpacity(world, x, y, z, 0x7);
    }
        
    /**
     *  Get cuboid list at given meta
     *  @param meta
     */
    public List<WesterosBlockDef.Cuboid> getCuboidList(int meta) {
        return cuboids_by_meta[meta];
    }
}
