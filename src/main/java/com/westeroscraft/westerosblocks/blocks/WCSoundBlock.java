package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;
import com.westeroscraft.westerosblocks.tileentity.WCTileEntitySound;


public class WCSoundBlock extends WCSolidBlock implements ITileEntityProvider {

    private static boolean did_te_register = false;
    
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            WCSolidBlock.new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            //TODO: only register TE once - need to see if we need to add legacy ids for compatibility with old world data
            //GameRegistry.registerTileEntity(WCTileEntitySound.class, def.blockName);
            if (!did_te_register) {
                GameRegistry.registerTileEntity(WCTileEntitySound.class, "WCTileEntitySound");
                did_te_register = true;
            }
            
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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean flag = world.isBlockPowered(pos);
        WCTileEntitySound tileentity = (WCTileEntitySound) world.getTileEntity(pos);
        if ((tileentity != null) && (tileentity.previousRedstoneState != flag)) {
            if (flag) {
                tileentity.triggerSound(this, world, pos);
            }
            tileentity.previousRedstoneState = flag;
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            WCTileEntitySound tileentity = (WCTileEntitySound) world.getTileEntity(pos);

            if (tileentity != null)
            {
                tileentity.changeSoundSelection(this, state.getBlock().getMetaFromState(state));
                tileentity.triggerSound(this, world, pos);
            }

            return true;
        }
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {

        if (!world.isRemote)
        {
            WCTileEntitySound tileentity = (WCTileEntitySound)world.getTileEntity(pos);

            if (tileentity != null)
            {
                tileentity.triggerSound(this, world, pos);
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
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int eventID, int eventParm) {
        if (eventID != 0) {
            return true;
        }
        int soundIndex = eventParm;
        int meta = this.getMetaFromState(state);
        List<String> sndids = this.getWBDefinition().getSoundIDList(meta);
        String sndid = null;
        if ((sndids != null) && (sndids.size() > soundIndex)) {
            sndid = sndids.get(soundIndex);
        }
        if (sndid != null) {
            SoundEvent se = getSound(sndid);
            if (se == null) {
                WesterosBlocks.log.warning("Unable to find sound " + sndid);
            }
            else {
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, se, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
            }
        }
        return true;
    }
    private static SoundEvent getSound(String sndid) {
        int cidx = sndid.indexOf(':');
        String modid = WesterosBlocks.MOD_ID;
        if (cidx >= 0) {
            modid = sndid.substring(0,  cidx);
            sndid = sndid.substring(cidx+1);
        }
        ResourceLocation rl = new ResourceLocation(modid, sndid);
        SoundEvent se = SoundEvent.REGISTRY.getObject(rl);
        if (se == null) {
            SoundEvent.registerSound(rl.toString());
            se = SoundEvent.REGISTRY.getObject(rl);
        }
        return se;
    }
    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
}
