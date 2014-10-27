package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.tileentity.WCTileEntitySound;

import cpw.mods.fml.common.registry.GameRegistry;

public class WCSoundBlock extends WCSolidBlock implements ITileEntityProvider {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            GameRegistry.registerTileEntity(WCTileEntitySound.class, def.blockName);
            
            return new Block[] { new WCSoundBlock(def) };
        }
    }
    
    public int[] def_period_by_meta = new int[16];
    public int[] def_addition_by_meta = new int[16];
    public int[] def_starttime_by_meta = new int[16];
    public int[] def_endtime_by_meta = new int[16];

    protected WCSoundBlock(WesterosBlockDef def) {
        super(def);
        for (int i = 0; i < 16; i++) {
            String type = def.getType(i);
            if (type != null) {
                String[] toks = type.split(",");
                for (String tok : toks) {
                    String [] flds = tok.split(":");
                    if (flds.length < 2) continue;
                    if (flds[0].equals("period")) {
                        def_period_by_meta[i] = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
                    }
                    else if (flds[0].equals("random-add")) {
                        def_addition_by_meta[i] = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
                    }
                    else if (flds[0].equals("start-time")) {
                        def_starttime_by_meta[i] = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
                    }
                    else if (flds[0].equals("end-time")) {
                        def_endtime_by_meta[i] = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
                    }
                    else {
                        WesterosBlocks.log.warning("Invalid type attribute '" + flds[0] + "' in " + def.blockName + "[" + i + "]");
                    }
                }
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock)
    {
        boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
        WCTileEntitySound tileentity = (WCTileEntitySound) world.getTileEntity(x, y, z);

        if ((tileentity != null) && (tileentity.previousRedstoneState != flag)) {
            if (flag) {
                tileentity.triggerSound(this, world, x, y, z);
            }
            tileentity.previousRedstoneState = flag;
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            WCTileEntitySound tileentity = (WCTileEntitySound) world.getTileEntity(x, y, z);

            if (tileentity != null)
            {
                tileentity.changeSoundSelection(this, world.getBlockMetadata(x,  y,  z));
                tileentity.triggerSound(this, world, x, y, z);
            }

            return true;
        }
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            WCTileEntitySound tileentity = (WCTileEntitySound)world.getTileEntity(x, y, z);

            if (tileentity != null)
            {
                tileentity.triggerSound(this, world, x, y, z);
            }
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World par1World, int xxx)
    {
        return new WCTileEntitySound();
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
     * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
     */
    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int eventParm)
    {
        if (eventID != 0) {
            return true;
        }
        int soundIndex = eventParm;
        int meta = world.getBlockMetadata(x,  y,  z);
        List<String> sndids = this.getWBDefinition().getSoundIDList(meta);
        String sndid = null;
        if ((sndids != null) && (sndids.size() > soundIndex)) {
            sndid = sndids.get(soundIndex);
        }
        if (sndid != null) {
            int cidx = sndid.indexOf(':');
            if (cidx < 0) {
                sndid = "westerosblocks:" + sndid;
            }
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, sndid, 1.0F, 1.0F);
        }
        return true;
    }
    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
        par1World.removeTileEntity(par2, par3, par4);
    }
}
