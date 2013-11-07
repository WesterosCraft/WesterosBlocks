package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import org.lwjgl.opengl.GL11;

// Custom fence renderer - lets us render fence items with per-meta specific textures in inventory
public class WCCuboidRenderer implements ISimpleBlockRenderingHandler {

    public static void renderStandardInvBlock (RenderBlocks renderblocks, Block block, int meta)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public void renderInventoryBlock(Block block, int meta, int modelID,
            RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        WCCuboidBlock cblock = (WCCuboidBlock) block;
        List<WesterosBlockDef.Cuboid> cubs = cblock.getCuboidList(meta);
        if (cubs != null) {
            int cnt = cubs.size();
            for (int i = 0; i < cnt; i++) {
                WesterosBlockDef.Cuboid cub = cubs.get(i);
                cblock.setActiveRenderCuboid(cub, renderer, meta, i);
                renderer.setRenderBounds(cub.xMin, cub.yMin, cub.zMin, cub.xMax, cub.yMax, cub.zMax);
                WCCuboidRenderer.renderStandardInvBlock(renderer, block, meta);
            }
            cblock.setActiveRenderCuboid(null, renderer, meta, -1);
        }
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        WCCuboidBlock cblock = (WCCuboidBlock) block;
        WesterosBlockDef def = cblock.getWBDefinition();
        int meta = world.getBlockMetadata(x, y, z);
        List<WesterosBlockDef.Cuboid> cubs = cblock.getCuboidList(meta);
        if (cubs != null) {
            int cnt = cubs.size();
            for (int i = 0; i < cnt; i++) {
                WesterosBlockDef.Cuboid cub = cubs.get(i);
                cblock.setActiveRenderCuboid(cub, renderer, meta, i);
                renderer.setRenderBounds(cub.xMin, cub.yMin, cub.zMin, cub.xMax, cub.yMax, cub.zMax);
                renderer.renderStandardBlock(cblock, x, y, z);
            }
            cblock.setActiveRenderCuboid(null, renderer, meta, -1);
        }
        return true;
    }

    public int getRenderId() {
        return WesterosBlocks.cuboidRenderID;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

}
