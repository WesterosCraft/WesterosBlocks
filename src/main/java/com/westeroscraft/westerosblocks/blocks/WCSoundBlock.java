package com.westeroscraft.westerosblocks.blocks;

 
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

public class WCSoundBlock extends WCSolidBlock {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSoundBlock(props, def)), true, def.nonOpaque);
        }
    }
    
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;    

    public int def_period;
    public int def_addition;
    public int def_starttime;
    public int def_endtime;

    protected WCSoundBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props, def);
        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                String [] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("period")) {
                    def_period = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
                }
                else if (flds[0].equals("random-add")) {
                    def_addition = (int) Math.round(20.0 * Double.parseDouble(flds[1]));
                }
                else if (flds[0].equals("start-time")) {
                    def_starttime = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
                }
                else if (flds[0].equals("end-time")) {
                    def_endtime = ((Integer.parseInt(flds[1]) * 10) + 18000) % 24000;
                }
                else {
                    WesterosBlocks.log.warn("Invalid type attribute '" + flds[0] + "' in " + def.blockName);
                }
            }
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        boolean flag = p_220069_2_.hasNeighborSignal(p_220069_3_);
        if (flag != p_220069_1_.getValue(POWERED)) {
           if (flag) {
              this.playNote(p_220069_2_, p_220069_3_);
           }

           p_220069_2_.setBlock(p_220069_3_, p_220069_1_.setValue(POWERED, Boolean.valueOf(flag)), 3);
        }

     }

     private void playNote(World p_196482_1_, BlockPos p_196482_2_) {
        if (p_196482_1_.isEmptyBlock(p_196482_2_.above())) {
           p_196482_1_.blockEvent(p_196482_2_, this, 0, 0);
        }
     }

     @Override
     public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (p_225533_2_.isClientSide) {
           return ActionResultType.SUCCESS;
        } else {
           this.playNote(p_225533_2_, p_225533_3_);
           return ActionResultType.CONSUME;
        }
     } 
     
     @Override
     public boolean triggerEvent(BlockState p_189539_1_, World p_189539_2_, BlockPos p_189539_3_, int p_189539_4_, int p_189539_5_) {
         //net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(p_189539_2_, p_189539_3_, p_189539_1_, p_189539_1_.getValue(NOTE), p_189539_1_.getValue(INSTRUMENT));
         //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
         //p_189539_1_ = p_189539_1_.setValue(NOTE, e.getVanillaNoteId()).setValue(INSTRUMENT, e.getInstrument());
         //int i = p_189539_1_.getValue(NOTE);
    	 int i = 0;
         //float f = (float)Math.pow(2.0D, (double)(i - 12) / 12.0D);
         //p_189539_2_.playSound((PlayerEntity)null, p_189539_3_, p_189539_1_.getValue(INSTRUMENT).getSoundEvent(), SoundCategory.RECORDS, 3.0F, f);
         p_189539_2_.addParticle(ParticleTypes.NOTE, (double)p_189539_3_.getX() + 0.5D, (double)p_189539_3_.getY() + 1.2D, (double)p_189539_3_.getZ() + 0.5D, (double)i / 24.0D, 0.0D, 0.0D);
         return true;
      }

      protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
         p_206840_1_.add(POWERED);
      } 
}
