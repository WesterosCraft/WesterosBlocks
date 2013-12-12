package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockDef.Subblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCLogBlock extends BlockLog implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            def.setMetaMask(0x3);
            // Make sure meta values only for 0-3 (other bits are orientation
            if (!def.validateMetaValues(new int[] { 0, 1, 2, 3 }, null)) {
                return null;
            }
            return new Block[] { new WCLogBlock(def) };
        }
    }
    
    private WesterosBlockDef def;
    
    protected WCLogBlock(WesterosBlockDef def) {
        super(def.blockID);
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        int orient = meta & 12;
        int type = meta & 3;
        // Map side to texture index (0=topbottom, 1=side)
        switch (orient) {
            case 0: // Up/down
                if (side < 2)
                    side = 0;
                else
                    side = 1;
                break;
            case 4: // East/west
                if ((side == 4) || (side == 5))
                    side = 0;
                else
                    side = 1;
                break;
            case 8: // North/south
                if ((side == 2) || (side == 3))
                    side = 0;
                else
                    side = 1;
                break;
            case 12: // all sides
                side = 1;
                break;
        }
        return def.doStandardIconGet(side, type);
    }

    @Override
    public int idDropped(int metadata, Random rand, int unused) {
        return blockID;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, id, tab, list);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void addCreativeItems(ArrayList itemList) {
        def.getStandardCreativeItems(this, itemList);
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta & 0x3;
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, metadata, face);
    }
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, metadata, face);
    }
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return def.getLightValue(world, x, y, z);
    }
    @Override
    public int getLightOpacity(World world, int x, int y, int z) {
        return def.getLightOpacity(world, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor() {
        return def.getBlockColor();
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int meta)
    {
        return def.getRenderColor(meta);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        return def.colorMultiplier(access, x, y, z);
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        // Modifiers for each orientation
        TextureModifier tmod[][] = { 
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE },
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.ROT90, TextureModifier.ROT270 },
            { TextureModifier.ROT90, TextureModifier.ROT270, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE, TextureModifier.NONE }
        };
        // Texture index for each orientation
        int txtid[][] = {
            { 0, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 0 },
            { 1, 1, 0, 0, 1, 1 },
            { 1, 1, 1, 1, 1, 1 }
        };

        for (WesterosBlockDef.Subblock sb : def.subBlocks) {
            if (sb == null) continue;
            if (sb.textures == null) continue;
            for (int i = 0; i < 4; i++) {   // 4 orientations
                BlockTextureRecord mtr = mtd.addBlockTextureRecord(this.blockID);
                mtr.setMetaValue(4*i + sb.meta);
                for (int face = 0; face < 6; face++) {
                    int fidx = txtid[i][face];
                    if (fidx >= sb.textures.size()) {
                        fidx = sb.textures.size() - 1;
                    }
                    String txt = sb.textures.get(fidx);
                    mtr.setSideTexture(txt.replace(':', '_'), tmod[i][face], BlockSide.valueOf("FACE_" + face));
                }
            }
        }
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
        def.doRandomDisplayTick(world, x, y, z, rnd);
        super.randomDisplayTick(world, x, y, z, rnd);
    }
}
