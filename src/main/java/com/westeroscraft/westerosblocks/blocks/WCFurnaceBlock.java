package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.network.WesterosBlocksChannelHandler;
import com.westeroscraft.westerosblocks.network.WesterosBlocksMessageDest;
import com.westeroscraft.westerosblocks.tileentity.WCFurnaceTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Custom furnace block
// Distinct meta mapping, to allow 2 custom furnaces per block ID
//  bit 0 = which custom furnace (0=first, 1=second)
//  bit 1-2 = orientation (same as standard, but minus 2 on value (0-3 vs 2-5)
//  bit 3 = active (1) vs idle (0)
public class WCFurnaceBlock extends BlockFurnace implements WesterosBlockLifecycle, WesterosBlockDynmapSupport, WesterosBlocksMessageDest {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Limit to 0, 1
            def.setMetaMask(0x1);
            if (!def.validateMetaValues(new int[] { 0, 1 }, null)) {
                return null;
            }
            GameRegistry.registerTileEntity(WCFurnaceTileEntity.class, def.blockName);

            return new Block[] { new WCFurnaceBlock(def) };
        }
    }

    private WesterosBlockDef def;
    private boolean alwaysOn[] = new boolean[2];
    
    protected WCFurnaceBlock(WesterosBlockDef def) {
        super(false);
        this.def = def;
        if (def.lightOpacity == WesterosBlockDef.DEF_INT) {
            def.lightOpacity = 0;    // Workaround MC's f*cked up lighting exceptions
        }
        def.doStandardContructorSettings(this);
        for (int i = 0; i < 2; i++) {
            String type = def.getType(i);
            if (type != null) {
                String[] toks = type.split(",");
                for (String tok : toks) {
                    String [] flds = tok.split(":");
                    if (flds.length < 2) continue;
                    if (flds[0].equals("always-on")) {
                        alwaysOn[i] = flds[1].equals("true");
                    }
                }
            }
        }
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
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        def.doStandardRegisterIcons(iconRegister);
    }

    
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);    // Save meta (need low bit)
            Block zn_id = world.getBlock(x, y, z - 1);
            Block zp_id = world.getBlock(x, y, z + 1);
            Block xn_id = world.getBlock(x - 1, y, z);
            Block xp_id = world.getBlock(x + 1, y, z);
            byte dir = 1;

            if (zn_id.func_149730_j() && !zp_id.func_149730_j()) {
                dir = 1;
            }

            if (zp_id.func_149730_j() && !zn_id.func_149730_j()) {
                dir = 0;
            }

            if (xn_id.func_149730_j() && !xp_id.func_149730_j()) {
                dir = 3;
            }

            if (xp_id.func_149730_j() && !xn_id.func_149730_j()) {
                dir = 2;
            }
            world.setBlockMetadataWithNotify(x, y, z, (dir << 1) | (meta & 0x1), 2);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int idx;
        int metaside = ((meta >> 1) & 0x3) + 2;
        if (side < 2) { // Top/bottom
            idx = side;
        }
        else if (metaside != side) {
            idx = 2;    // Side/back
        }
        else if ((meta & 0x8) != 0) {
            idx = 3;    // Active furnace
        }
        else {
            idx = 4;    // Inactive furnace
        }
        return def.doStandardIconGet(idx, meta);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itm, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, Item.getIdFromItem(itm), tab, list);
    }
    
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return def.getFireSpreadSpeed(world, x, y, z, face);
    }
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return def.getFlammability(world, x, y, z, face);
    }
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean active = alwaysOn[meta & 0x1] || ((meta & 0x8) != 0);
        if (active) {
            return def.getLightValue(world, x, y, z);
        }
        return 0;
    }
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        return def.getLightOpacity(this, world, x, y, z);
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
    @Override
    public Item getItemDropped(int meta, Random rnd, int par3) {
        return Item.getItemFromBlock(this);
    }
    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(int meta) {
        return meta & 0x1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        def.doRandomDisplayTick(world, x, y, z, random);

        int meta = world.getBlockMetadata(x, y, z);
        boolean active = alwaysOn[meta & 0x1] || ((meta & 0x8) != 0);
        
        if (active) {
            int dir = ((meta >> 1) & 0x3) + 2;  // Compute face we're facing
            float f = (float)x + 0.5F;
            float f1 = (float)y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            if (dir == 4)
            {
                world.spawnParticle("smoke", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
            }
            else if (dir == 5)
            {
                world.spawnParticle("smoke", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
            }
            else if (dir == 2)
            {
                world.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
            }
            else if (dir == 3)
            {
                world.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new WCFurnaceTileEntity();
    }

    /**
     * Update which block ID the furnace is using depending on whether or not it is burning
     */
    public static void updateFurnaceBlockState(boolean toActive, World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        
        if (toActive) {
            meta |= 0x8;
        }
        else {
            meta &= 0x7;
        }
        
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }

    private static final byte MSG_OPENWINDOW = 0x00;
    @Override
    public void deliverMessage(INetHandler handler, EntityPlayer player, byte[] msg) {
        if (msg[0] == MSG_OPENWINDOW) {
            EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
            WCFurnaceTileEntity te = new WCFurnaceTileEntity();
            p.func_146101_a(te);
            p.openContainer.windowId = msg[1];
        }
    }
    
    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            WCFurnaceTileEntity te = (WCFurnaceTileEntity)par1World.getTileEntity(par2, par3, par4);

            if (te != null) {
                EntityPlayerMP p = (EntityPlayerMP) player;
                p.getNextWindowId();
                byte[] data = { (byte) p.currentWindowId };
                WesterosBlocksChannelHandler.sendBlockMessage(this, p, MSG_OPENWINDOW, data);
                p.openContainer = new ContainerFurnace(p.inventory, te);
                p.openContainer.windowId = p.currentWindowId;
                p.openContainer.addCraftingToCrafters(p);
            }

            return true;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item)
    {
        int facing = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int meta = item.getItemDamage() & 0x1;
        switch (facing) {
            case 0:
                meta += (0 << 1);
                break;
            case 1:
                meta += (3 << 1);
                break;
            case 2:
                meta += (1 << 1);
                break;
            case 3:
                meta += (2 << 1);
                break;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);

        if (item.hasDisplayName())
        {
            ((TileEntityFurnace)world.getTileEntity(x, y, z)).func_145951_a(item.getDisplayName());
        }
    }

}
