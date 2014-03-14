package com.westeroscraft.westerosblocks.blocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dynmap.modsupport.ModTextureDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.WesterosBlocksMessageDest;
import com.westeroscraft.westerosblocks.WesterosBlocksPacketHandler;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCFurnaceBlock extends BlockFurnace implements WesterosBlockLifecycle, WesterosBlockDynmapSupport, WesterosBlocksMessageDest {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            // Limit to 0
            def.setMetaMask(0x0);
            if (!def.validateMetaValues(new int[] { 0 }, null)) {
                return null;
            }
            if (def.blockIDs.length != 2) {
                WesterosBlocks.log.severe(String.format("Block '%s' does not have a valid number of blockIDs: 2 are required", def.blockName));
                return null;
            }
            if (keepFurnaceInvetoryField == null) {
                try {
                    keepFurnaceInvetoryField = BlockFurnace.class.getField("field_72288_c");
                    keepFurnaceInvetoryField.setAccessible(true);
                } catch (NoSuchFieldException fnfx) {
                    WesterosBlocks.log.severe(String.format("Block '%s' - fannot find keepFurnaceInventory", def.blockName));
                    return null;
                }
            }
            GameRegistry.registerTileEntity(WCFurnaceTileEntity.class, def.blockName);

            WCFurnaceBlock inactive = new WCFurnaceBlock(def, false);
            WCFurnaceBlock active = new WCFurnaceBlock(def, true);
            inactive.otherBlock = active;
            active.otherBlock = inactive;
            return new Block[] { inactive, active };
        }
    }

    private static final int INACTIVE_IDX = 0;
    private static final int ACTIVE_IDX = 1;
    
    private WesterosBlockDef def;
    private WCFurnaceBlock otherBlock;
    private boolean active;
    private boolean alwaysOn = false;
    private static Field keepFurnaceInvetoryField;
    
    protected WCFurnaceBlock(WesterosBlockDef def, boolean is_active) {
        super(def.blockIDs[is_active?ACTIVE_IDX:INACTIVE_IDX], is_active);
        this.def = def;
        this.active = is_active;
        def.doStandardContructorSettings(this, is_active?ACTIVE_IDX:INACTIVE_IDX);
        if (def.lightOpacity < 0) {
            this.setLightOpacity(0);    // Workaround MC's f*cked up lighting exceptions
        }
        if (is_active) {
            this.setCreativeTab(null);
        }
        String type = def.getType(0);
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String [] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("always-on")) {
                    alwaysOn = flds[1].equals("true");
                }
            }
        }
    }

    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);

        return true;
    }

    public boolean registerBlockDefinition() {
        if (this.active) {
            def.doStandardRegisterActions(this, MultiBlockItem.class, null, ACTIVE_IDX);
        }
        else {
            def.doStandardRegisterActions(this, MultiBlockItem.class, null, INACTIVE_IDX);
        }
        
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
        int idx;
        if (side < 2) { // Top/bottom
            idx = side;
        }
        else if (meta != side) {
            idx = 2;    // Side/back
        }
        else if (active) {
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
    public void getSubBlocks(int id, CreativeTabs tab, List list) {
        def.getStandardSubBlocks(this, id, tab, list);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void addCreativeItems(ArrayList itemList) {
        def.getStandardCreativeItems(this, itemList);
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
        if (active || alwaysOn) {
            return def.getLightValue(world, x, y, z);
        }
        return 0;
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
    @Override
    public int idDropped(int meta, Random rnd, int par3) {
        if (this.active) {
            return this.otherBlock.blockID;
        }
        else {
            return blockID;
        }
    }
    @Override
    public int idPicked(World world, int x, int y, int z) {
        if (!active) {
            return blockID;
        }
        else {
            return otherBlock.blockID;
        }
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(blockID, 2, meta);
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return (def.alphaRender?1:0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        def.doRandomDisplayTick(par1World, par2, par3, par4, par5Random);

        if (this.active || this.alwaysOn)
        {
            int l = par1World.getBlockMetadata(par2, par3, par4);
            float f = (float)par2 + 0.5F;
            float f1 = (float)par3 + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)par4 + 0.5F;
            float f3 = 0.52F;
            float f4 = par5Random.nextFloat() * 0.6F - 0.3F;

            if (l == 4)
            {
                par1World.spawnParticle("smoke", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
            }
            else if (l == 5)
            {
                par1World.spawnParticle("smoke", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
            }
            else if (l == 2)
            {
                par1World.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
            }
            else if (l == 3)
            {
                par1World.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
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
    public TileEntity createNewTileEntity(World par1World)
    {
        return new WCFurnaceTileEntity();
    }

    private static void setKeepFurnaceInventory(boolean k) {
        try {
            keepFurnaceInvetoryField.set(null, k);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }
    /**
     * Update which block ID the furnace is using depending on whether or not it is burning
     */
    public static void updateFurnaceBlockState(boolean toActive, World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        int blkid = world.getBlockId(x, y, z);
        WCFurnaceBlock blk = (WCFurnaceBlock) Block.blocksList[blkid];
        TileEntity tileentity = world.getBlockTileEntity(x, y, z);
        setKeepFurnaceInventory(true);

        if (toActive != blk.active) {   // Different state?
            world.setBlock(x, y, z, blk.otherBlock.blockID);
        }

        setKeepFurnaceInventory(false);
        
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setBlockTileEntity(x, y, z, tileentity);
        }
    }

    private static final byte MSG_OPENWINDOW = 0x00;
    @Override
    public void deliverMessage(INetworkManager manager, Player player,
            byte[] msg, int msgoff) {
        if (msg[msgoff] == MSG_OPENWINDOW) {
            EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
            WCFurnaceTileEntity te = new WCFurnaceTileEntity();
            if (te.isInvNameLocalized()) {
                te.setGuiDisplayName(te.getInvName());
            }
            p.displayGUIFurnace(te);
            p.openContainer.windowId = msg[msgoff+1];
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
            WCFurnaceTileEntity te = (WCFurnaceTileEntity)par1World.getBlockTileEntity(par2, par3, par4);

            if (te != null) {
                EntityPlayerMP p = (EntityPlayerMP) player;
                p.incrementWindowID();
                byte[] data = { (byte) p.currentWindowId };
                WesterosBlocksPacketHandler.sendBlockMessage(this, p, MSG_OPENWINDOW, data);
                p.openContainer = new ContainerFurnace(p.inventory, te);
                p.openContainer.windowId = p.currentWindowId;
                p.openContainer.addCraftingToCrafters(p);
            }

            return true;
        }
    }

}
