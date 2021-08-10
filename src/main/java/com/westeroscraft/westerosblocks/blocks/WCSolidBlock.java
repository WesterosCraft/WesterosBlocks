package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

public class WCSolidBlock extends Block implements WesterosBlockDynmapSupport, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCSolidBlock(props, def)), true, def.nonOpaque);
        }
    }    
    protected WesterosBlockDef def;
    protected VoxelShape collisionbox;
    
    protected WCSolidBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        collisionbox = def.makeCollisionBoxShape();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return collisionbox;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return collisionbox;
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return collisionbox;
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return collisionbox;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        def.defaultRegisterTextures(mtd);
        def.defaultRegisterTextureBlock(mtd, (def.alphaRender ? TransparencyMode.TRANSPARENT : TransparencyMode.OPAQUE));
    }
    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
