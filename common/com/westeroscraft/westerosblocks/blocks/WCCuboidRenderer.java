package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
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
    
    public static void renderCrossedSquaresInvBlock (RenderBlocks renderblocks, Block block, int meta)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.drawCrossedSquares(block, meta, -0.5D, -0.5D, -0.5D, 1.0F);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    


    public void renderInventoryBlock(Block block, int meta, int modelID,
            RenderBlocks renderer) {
        WCCuboidBlock cblock = (WCCuboidBlock) block;
        List<WesterosBlockDef.Cuboid> cubs = cblock.getCuboidList(meta);
        if (cubs != null) {
            int cnt = cubs.size();
            for (int i = 0; i < cnt; i++) {
                WesterosBlockDef.Cuboid cub = cubs.get(i);
                cblock.setActiveRenderCuboid(cub, renderer, meta, i);
                renderer.setRenderBounds(cub.xMin, cub.yMin, cub.zMin, cub.xMax, cub.yMax, cub.zMax);
                if (WesterosBlockDef.SHAPE_CROSSED.equals(cub.shape)) {
                    WCCuboidRenderer.renderCrossedSquaresInvBlock(renderer, block, meta);
                }
                else {
                    WCCuboidRenderer.renderStandardInvBlock(renderer, block, meta);
                }
            }
            cblock.setActiveRenderCuboid(null, renderer, meta, -1);
        }
    }
    
    private void prepLighting(IBlockAccess world, Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        // Use mixed brightness for block
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        // Compute the color multiplier
        int mult = block.colorMultiplier(world, x, y, z);
        float red = (float)(mult >> 16 & 255) / 255.0F;
        float green = (float)(mult >> 8 & 255) / 255.0F;
        float blue = (float)(mult & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float nred = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
            float ngreen = (red * 30.0F + green * 70.0F) / 100.0F;
            float nblue = (red * 30.0F + green * 70.0F) / 100.0F;
            red = nred;
            green = ngreen;
            blue = nblue;
        }
        tessellator.setColorOpaque_F(red, green, blue);
    }
    private enum CalcUV {
        XMIN,
        YMIN,
        ZMIN,
        INV_XMAX,
        INV_YMAX,
        INV_ZMAX
    };    
    private double calcInterpUV(Icon ico, CalcUV f, WesterosBlockDef.Cuboid cub, boolean get_v, boolean min) {
        double v = 0.0;
        switch (f) {
            case XMIN:
                if (min)
                    v = 16.0 * cub.xMin;
                else
                    v = 16.0 * cub.xMax;
                break;
            case YMIN:
                if (min)
                    v = 16.0 * cub.yMin;
                else
                    v = 16.0 * cub.yMax;
                break;
            case ZMIN:
                if (min)
                    v = 16.0 * cub.zMin;
                else
                    v = 16.0 * cub.zMax;
                break;
            case INV_XMAX:
                if (min)
                    v = 16.0 * (1.0 - cub.xMax);
                else
                    v = 16.0 * (1.0 - cub.xMin);
                break;
            case INV_YMAX:
                if (min)
                    v = 16.0 * (1.0 - cub.yMax);
                else
                    v = 16.0 * (1.0 - cub.yMin);
                break;
            case INV_ZMAX:
                if (min)
                    v = 16.0 * (1.0 - cub.zMax);
                else
                    v = 16.0 * (1.0 - cub.zMin);
                break;
        }
        if (get_v)
            return ico.getInterpolatedV(v);
        else
            return ico.getInterpolatedU(v);
    }
    private double calcMinU(Icon ico, int side, int rot, WesterosBlockDef.Cuboid cub) {
        return calcInterpUV(ico, uvCalc[side][rot][0], cub, false, true);
    }
    private double calcMaxU(Icon ico, int side, int rot, WesterosBlockDef.Cuboid cub) {
        return calcInterpUV(ico, uvCalc[side][rot][0], cub, false, false);
    }
    private double calcMinV(Icon ico, int side, int rot, WesterosBlockDef.Cuboid cub) {
        return calcInterpUV(ico, uvCalc[side][rot][1], cub, true, true);
    }
    private double calcMaxV(Icon ico, int side, int rot, WesterosBlockDef.Cuboid cub) {
        return calcInterpUV(ico, uvCalc[side][rot][1], cub, true, false);
    }
    
    // Mapping of which coordinate (min(0) or max(1) to use for each corner in vertex sequence
    // index is [side][vertex][x(0),y(1),z(2)]
    private static final int sideToCorner[][][] = {
        { { 0, 0, 1 }, { 0, 0, 0 }, { 1, 0, 0 }, { 1, 0, 1 } }, // Side 0
        { { 0, 1, 0 }, { 0, 1, 1 }, { 1, 1, 1 }, { 1, 1, 0 } }, // Side 1
        { { 1, 1, 0 }, { 1, 0, 0 }, { 0, 0, 0 }, { 0, 1, 0 } }, // Side 2
        { { 0, 1, 1 }, { 0, 0, 1 }, { 1, 0, 1 }, { 1, 1, 1 } }, // Side 3
        { { 0, 1, 0 }, { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 1 } }, // Side 4
        { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 0, 0 }, { 1, 1, 0 } }  // Side 5
    };
    // Get UV calculation functions, by side and rotation
    // [side][rot][umin,vmin]
    private static final CalcUV uvCalc[][][] = {
        {   // Side 0
            { CalcUV.XMIN, CalcUV.INV_ZMAX },       // Rot 0
            { CalcUV.INV_ZMAX, CalcUV.INV_XMAX },   // Rot 90
            { CalcUV.INV_XMAX, CalcUV.ZMIN },       // Rot 180
            { CalcUV.ZMIN, CalcUV.XMIN }            // Rot 270
        },
        {   // Side 1
            { CalcUV.XMIN, CalcUV.ZMIN },           // Rot 0
            { CalcUV.ZMIN, CalcUV.INV_XMAX },       // Rot 90
            { CalcUV.INV_XMAX, CalcUV.INV_ZMAX },   // Rot 180
            { CalcUV.INV_ZMAX, CalcUV.XMIN }        // Rot 270
        },
        {   // Side 2
            { CalcUV.INV_XMAX, CalcUV.INV_YMAX },   // Rot 0
            { CalcUV.INV_YMAX, CalcUV.XMIN },       // Rot 90
            { CalcUV.XMIN, CalcUV.YMIN },           // Rot 180
            { CalcUV.YMIN, CalcUV.INV_XMAX }        // Rot 270
        },
        {   // Side 3
            { CalcUV.XMIN, CalcUV.INV_YMAX },       // Rot 0
            { CalcUV.INV_YMAX, CalcUV.INV_XMAX },   // Rot 90
            { CalcUV.INV_XMAX, CalcUV.YMIN },       // Rot 180
            { CalcUV.YMIN, CalcUV.XMIN }            // Rot 270
        },
        {   // Side 4
            { CalcUV.ZMIN, CalcUV.INV_YMAX },       // Rot 0
            { CalcUV.INV_YMAX, CalcUV.INV_ZMAX },   // Rot 90
            { CalcUV.INV_ZMAX, CalcUV.YMIN },       // Rot 180
            { CalcUV.YMIN, CalcUV.ZMIN }            // Rot 270
        },
        {   // Side 5
            { CalcUV.INV_ZMAX, CalcUV.INV_YMAX },   // Rot 0
            { CalcUV.INV_YMAX, CalcUV.ZMIN },       // Rot 90
            { CalcUV.ZMIN, CalcUV.YMIN },           // Rot 180
            { CalcUV.YMIN, CalcUV.INV_ZMAX }        // Rot 270
        }
    };
    private void renderCuboidSide(WCCuboidBlock block, WesterosBlockDef.Cuboid cub, RenderBlocks renderer, int meta, int side) {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = renderer.getBlockIconFromSideAndMetadata(block, side, meta);    // Get icon for side
        int rot = cub.sideRotations[side] / 90;
        // Get interpolated U and V ranges
        double umin = calcMinU(icon, side, rot, cub);
        double umax = calcMaxU(icon, side, rot, cub);
        double vmin = calcMinV(icon, side, rot, cub);
        double vmax = calcMaxV(icon, side, rot, cub);
        // Add corners
        int[][] c = sideToCorner[side];
        tessellator.addVertexWithUV(xr[c[(4-rot)%4][0]], yr[c[(4-rot)%4][1]], zr[c[(4-rot)%4][2]], umin, vmin);
        tessellator.addVertexWithUV(xr[c[(5-rot)%4][0]], yr[c[(5-rot)%4][1]], zr[c[(5-rot)%4][2]], umin, vmax);
        tessellator.addVertexWithUV(xr[c[(6-rot)%4][0]], yr[c[(6-rot)%4][1]], zr[c[(6-rot)%4][2]], umax, vmax);
        tessellator.addVertexWithUV(xr[c[(7-rot)%4][0]], yr[c[(7-rot)%4][1]], zr[c[(7-rot)%4][2]], umax, vmin);
    }
    // Get absolute coordinates of corners
    private double xr[] = new double[2];
    private double yr[] = new double[2];
    private double zr[] = new double[2];

    private void renderCuboid(WCCuboidBlock block, WesterosBlockDef.Cuboid cub, RenderBlocks renderer, int meta, int x, int y, int z) {
        // Get absolute coordinates of corners
        xr[0] = x + cub.xMin;
        xr[1] = x + cub.xMax;
        yr[0] = y + cub.yMin;
        yr[1] = y + cub.yMax;
        zr[0] = z + cub.zMin;
        zr[1] = z + cub.zMax;
        // Render each side
        for (int side = 0; side < 6; side++) {
            renderCuboidSide(block, cub, renderer, meta, side);
        }
    }
    
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        WCCuboidBlock cblock = (WCCuboidBlock) block;
        int meta = world.getBlockMetadata(x, y, z);
        List<WesterosBlockDef.Cuboid> cubs = cblock.getCuboidList(meta);
        if (cubs != null) {
            // Prep lighting for block
            prepLighting(world, block, x, y, z);
            
            int cnt = cubs.size();
            for (int i = 0; i < cnt; i++) {
                WesterosBlockDef.Cuboid cub = cubs.get(i);
                cblock.setActiveRenderCuboid(cub, renderer, meta, i);
                renderer.setRenderBounds(cub.xMin, cub.yMin, cub.zMin, cub.xMax, cub.yMax, cub.zMax);
                if (WesterosBlockDef.SHAPE_CROSSED.equals(cub.shape)) {
                    renderer.renderCrossedSquares(cblock, x, y, z);
                }
                else {
                    renderCuboid(cblock, cub, renderer, meta, x, y, z);
                }
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
