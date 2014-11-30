package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import org.lwjgl.opengl.GL11;

// Custom crossed renderer - handle alphaBlend properly
public class WCCrossedRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
            RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        int j;
        float f1;
        float f2;
        float f3;

        if (renderer.useInventoryTint)
        {
            j = block.getRenderColor(metadata);

            f1 = (float)(j >> 16 & 255) / 255.0F;
            f2 = (float)(j >> 8 & 255) / 255.0F;
            f3 = (float)(j & 255) / 255.0F;
            GL11.glColor4f(f1, f2, f3, 1.0F);
        }

        j = block.getRenderType();
        renderer.setRenderBoundsFromBlock(block);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.drawCrossedSquares(block, metadata, -0.5D, -0.5D, -0.5D, 1.0F);
        tessellator.draw();
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        float f = 1.0F;
        int l = block.colorMultiplier(world, x, y, z);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        double d0 = (double)x;
        double d1 = (double)y;
        double d2 = (double)z;

        this.drawCrossedSquares(renderer, block, world.getBlockMetadata(x, y, z), d0, d1, d2, 1.0F);
        
        return true;
    }
    
    private void drawCrossedSquares(RenderBlocks renderer, Block block, int meta, double x, double y, double z, float par9)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = renderer.getBlockIconFromSideAndMetadata(block, 0, meta);
        boolean isRenderPassZero = (block.getRenderBlockPass() == 0);

        if (renderer.hasOverrideBlockTexture())
        {
            icon = renderer.overrideBlockTexture;
        }

        double d3 = (double)icon.getMinU();
        double d4 = (double)icon.getMinV();
        double d5 = (double)icon.getMaxU();
        double d6 = (double)icon.getMaxV();
        double d7 = 0.45D * (double)par9;
        double d8 = x + 0.5D - d7;
        double d9 = x + 0.5D + d7;
        double d10 = z + 0.5D - d7;
        double d11 = z + 0.5D + d7;
        tessellator.addVertexWithUV(d8, y + (double)par9, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d9, y + 0.0D, d11, d5, d6);
        tessellator.addVertexWithUV(d9, y + (double)par9, d11, d5, d4);
        if (isRenderPassZero) {
            tessellator.addVertexWithUV(d9, y + (double)par9, d11, d3, d4);
            tessellator.addVertexWithUV(d9, y + 0.0D, d11, d3, d6);
            tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
            tessellator.addVertexWithUV(d8, y + (double)par9, d10, d5, d4);
        }
        tessellator.addVertexWithUV(d8, y + (double)par9, d11, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d11, d3, d6);
        tessellator.addVertexWithUV(d9, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d9, y + (double)par9, d10, d5, d4);
        if (isRenderPassZero) {
            tessellator.addVertexWithUV(d9, y + (double)par9, d10, d3, d4);
            tessellator.addVertexWithUV(d9, y + 0.0D, d10, d3, d6);
            tessellator.addVertexWithUV(d8, y + 0.0D, d11, d5, d6);
            tessellator.addVertexWithUV(d8, y + (double)par9, d11, d5, d4);
        }
    }


    public boolean shouldRender3DInInventory() {
        return true;
    }

    public int getRenderId() {
        return WesterosBlocks.crossedRenderID;
    }


}
