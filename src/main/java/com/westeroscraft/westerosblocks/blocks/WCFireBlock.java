package com.westeroscraft.westerosblocks.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.PatchBlockModel;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class WCFireBlock extends BlockFire implements WesterosBlockLifecycle, WesterosBlockDynmapSupport
{
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(new int[] { 0 }, new int[] { 0 })) {
                return null;
            }
            return new Block[] { new WCFireBlock(def) };
        }
    }
    private WesterosBlockDef def;

    protected WCFireBlock(WesterosBlockDef def) {
        super(def.blockID);
        
        this.def = def;
        def.doStandardContructorSettings(this);
        this.setTickRandomly(false);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }


    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
        {
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    }

    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @SideOnly(Side.CLIENT)
    public Icon getFireIcon(int side)
    {
        return def.doStandardIconGet(side, 0);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return def.doStandardIconGet(0, 0);
    }
    
    public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return false;
    }

    public int getChanceToEncourageFire(World world, int x, int y, int z, int oldChance, ForgeDirection face)
    {
        return 0;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        def.defaultRegisterTextures(mtd);
        // Register textures
        Subblock sb = def.getByMeta(0);
        if ((sb == null) || (sb.textures == null) || (sb.textures.size() < 2)) return;
        String txt1 = sb.textures.get(0);
        String txt2 = sb.textures.get(1);
        BlockTextureRecord btr = mtd.addBlockTextureRecord(this.blockID);
        btr.setTransparencyMode(TransparencyMode.TRANSPARENT);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 0);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 1);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 2);
        btr.setPatchTexture(txt1, TextureModifier.NONE, 3);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 4);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 5);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 6);
        btr.setPatchTexture(txt2, TextureModifier.NONE, 7);
        def.setBlockColorMap(btr, sb);
        /* Make base model */
        PatchBlockModel mod = md.addPatchModel(this.blockID);
        // patchblock:id=51,data=*,patch0=VertX0,patch1=VertX0@90,patch2=VertX0@180,patch3=VertX0@270,patch4=SlopeXUpZTop675,patch5=SlopeXUpZTop675@90,patch6=SlopeXUpZTop675@180,patch4=SlopeXUpZTop675@270
        // patch:id=VertX0,Ox=0.0,Oy=0.0,Oz=1.0,Ux=0.0,Uy=0.0,Uz=0.0,Vx=0.0,Vy=1.0,Vz=1.0,visibility=bottom
        // patch:id=SlopeXUpZTop675,Ox=0.375,Oy=0.0,Oz=0.0,Ux=0.375,Uy=0.0,Uz=1.0,Vx=0.5,Vy=1.0,Vz=0.0,visibility=top
        String patch0 = mod.addPatch(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, SideVisible.BOTH);
        mod.addRotatedPatch(patch0, 0, 90, 0);
        mod.addRotatedPatch(patch0, 0, 180, 0);
        mod.addRotatedPatch(patch0, 0, 270, 0);
        String patch4 = mod.addPatch(0.375, 0.0, 0.0, 0.375, 0.0, 1.0, 0.5, 1.0, 0.0, SideVisible.BOTH);
        mod.addRotatedPatch(patch4, 0, 90, 0);
        mod.addRotatedPatch(patch4, 0, 180, 0);
        mod.addRotatedPatch(patch4, 0, 270, 0);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
}
