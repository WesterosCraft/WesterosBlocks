package com.westeroscraft.westerosblocks.render;

import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import org.lwjgl.opengl.GL11;

// Custom half-door renderer
public class WCHalfDoorRenderer implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID,
            RenderBlocks renderer) {
        
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        boolean flag = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        int i1 = block.getMixedBrightnessForBlock(world, x, y, z);
        tessellator.setBrightness(renderer.renderMinY > 0.0D ? i1 : block.getMixedBrightnessForBlock(world, x, y - 1, z));
        tessellator.setColorOpaque_F(f, f, f);
        renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 0));
        flag = true;
        tessellator.setBrightness(renderer.renderMaxY < 1.0D ? i1 : block.getMixedBrightnessForBlock(world, x, y + 1, z));
        tessellator.setColorOpaque_F(f1, f1, f1);
        renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, world, x, y, z, 1));
        flag = true;
        tessellator.setBrightness(renderer.renderMinZ > 0.0D ? i1 : block.getMixedBrightnessForBlock(world, x, y, z - 1));
        tessellator.setColorOpaque_F(f2, f2, f2);
        IIcon icon = renderer.getBlockIcon(block, world, x, y, z, 2);
        renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, icon);
        flag = true;
        renderer.flipTexture = false;
        tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? i1 : block.getMixedBrightnessForBlock(world, x, y, z + 1));
        tessellator.setColorOpaque_F(f2, f2, f2);
        icon = renderer.getBlockIcon(block, world, x, y, z, 3);
        renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, icon);
        flag = true;
        renderer.flipTexture = false;
        tessellator.setBrightness(renderer.renderMinX > 0.0D ? i1 : block.getMixedBrightnessForBlock(world, x - 1, y, z));
        tessellator.setColorOpaque_F(f3, f3, f3);
        icon = renderer.getBlockIcon(block, world, x, y, z, 4);
        renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, icon);
        flag = true;
        renderer.flipTexture = false;
        tessellator.setBrightness(renderer.renderMaxX < 1.0D ? i1 : block.getMixedBrightnessForBlock(world, x + 1, y, z));
        tessellator.setColorOpaque_F(f3, f3, f3);
        icon = renderer.getBlockIcon(block, world, x, y, z, 5);
        renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, icon);
        flag = true;
        renderer.flipTexture = false;
        return flag;
    }

    @Override
    public int getRenderId() {
        return WesterosBlocks.halfdoorRenderID;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }


}
