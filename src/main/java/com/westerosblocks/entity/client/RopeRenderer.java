package com.westerosblocks.entity.client;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.entity.custom.RopeEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RopeRenderer extends EntityRenderer<RopeEntity, RopeRenderer.RopeRenderState> {

    private static final Identifier ROPE_TEXTURE = Identifier.of(WesterosBlocks.MOD_ID, "textures/entity/straight_rope.png");
    private static final Identifier CHAIN_TEXTURE = Identifier.of(WesterosBlocks.MOD_ID, "textures/entity/chain.png");
    private static final float HALF_WIDTH = 0.06F;
    private static final int SEGMENTS = 40;
    private static final float TEXTURE_REPEAT_PER_BLOCK = 1.0F;

    public RopeRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public static class RopeRenderState extends EntityRenderState {
        public Vec3d startLocal = Vec3d.ZERO;
        public Vec3d endLocal = Vec3d.ZERO;
        public float sagAmount = 0.0f;
        public int variant = 0;
        public float dirUvOffset = 0.0f;
    }

    @Override
    public RopeRenderState createRenderState() {
        return new RopeRenderState();
    }

    @Override
    public void updateRenderState(RopeEntity entity, RopeRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        Vec3d startWorld = entity.getStartWorldPoint();
        Vec3d endWorld = entity.getEndWorldPoint();
        Vec3d entityPos = entity.getPos();
        state.startLocal = startWorld.subtract(entityPos);
        state.endLocal = endWorld.subtract(entityPos);
        state.variant = entity.getVariant();

        Vec3d delta = state.endLocal.subtract(state.startLocal);
        double ax = Math.abs(delta.x);
        double ay = Math.abs(delta.y);
        double az = Math.abs(delta.z);
        state.dirUvOffset = (ax + az) > ay ? 1.00F : 0.0F;

        state.sagAmount = switch (entity.getTensionLevel()) {
            case 0 -> 5.50f;
            case 1 -> 4.00f;
            case 2 -> 3.00f;
            case 3 -> 2.00f;
            case 4 -> 1.00f;
            case 5 -> 0.50f;
            default -> 0.0f;
        };
    }

    @Override
    public void render(RopeRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        Vec3d startLocal = state.startLocal;
        Vec3d endLocal = state.endLocal;
        Vec3d delta = endLocal.subtract(startLocal);
        double length = delta.length();
        if (length < 1e-6) {
            return;
        }

        Vec3d direction = delta.normalize();
        Vec3d arbitraryUp = Math.abs(direction.y) > 0.95
                ? new Vec3d(1, 0, 0)
                : new Vec3d(0, 1, 0);

        Vec3d right = direction.crossProduct(arbitraryUp).normalize();
        Vec3d up = right.crossProduct(direction).normalize();

        float sagAmount = state.sagAmount;
        float dirUvOffset = state.dirUvOffset;
        int variant = state.variant;

        Identifier texture = variant == 1 ? CHAIN_TEXTURE : ROPE_TEXTURE;
        RenderLayer layer = RenderLayer.getEntityCutoutNoCull(texture);

        // Use submitCustom for custom vertex rendering
        final Vec3d finalRight = right;
        final Vec3d finalUp = up;
        final Vec3d finalStartLocal = startLocal;
        final Vec3d finalEndLocal = endLocal;

        matrices.push();
        queue.submitCustom(matrices, layer, (entry, buffer) -> {
            Vec3d prevPoint = getRopePoint(finalStartLocal, finalEndLocal, 0.0f, sagAmount);
            float vCoord = dirUvOffset;

            for (int i = 1; i <= SEGMENTS; i++) {
                float t = (float) i / SEGMENTS;
                Vec3d currPoint = getRopePoint(finalStartLocal, finalEndLocal, t, sagAmount);
                float segmentLength = (float) currPoint.subtract(prevPoint).length();
                float vNext = vCoord + segmentLength * TEXTURE_REPEAT_PER_BLOCK;
                drawTiledRibbon(buffer, entry, prevPoint, currPoint, finalRight, HALF_WIDTH, vCoord, vNext);

                float crossPlaneOffset = variant == 1 ? 0.50F : 0.0F;
                drawTiledRibbon(buffer, entry, prevPoint, currPoint, finalUp, HALF_WIDTH, vCoord + crossPlaneOffset, vNext + crossPlaneOffset);

                prevPoint = currPoint;
                vCoord = vNext;
            }
        });
        matrices.pop();

        super.render(state, matrices, queue, cameraState);
    }

    private static Vec3d getRopePoint(Vec3d start, Vec3d end, float t, float sagStrength) {
        double x = MathHelper.lerp(t, start.x, end.x);
        double y = MathHelper.lerp(t, start.y, end.y);
        double z = MathHelper.lerp(t, start.z, end.z);
        y -= Math.sin(t * Math.PI) * sagStrength;
        return new Vec3d(x, y, z);
    }

    private static void drawTiledRibbon(VertexConsumer buffer, MatrixStack.Entry entry,
                                        Vec3d p0, Vec3d p1, Vec3d normalAxis, float halfWidth,
                                        float v0, float v1) {
        int tile0 = (int) Math.floor(v0);
        int tile1 = (int) Math.floor(v1);
        float frac0 = MathHelper.fractionalPart(v0);
        float frac1 = MathHelper.fractionalPart(v1);

        if (tile0 == tile1) {
            drawSingleQuad(buffer, entry, p0, p1, normalAxis, halfWidth, frac0, frac1);
        } else {
            float boundaryV = tile0 + 1.0f;
            float alpha = (boundaryV - v0) / (v1 - v0);
            alpha = MathHelper.clamp(alpha, 0.0f, 1.0f);
            Vec3d mid = p0.add(p1.subtract(p0).multiply(alpha));
            drawSingleQuad(buffer, entry, p0, mid, normalAxis, halfWidth, frac0, 1.0f);
            drawTiledRibbon(buffer, entry, mid, p1, normalAxis, halfWidth, boundaryV, v1);
        }
    }

    private static void drawSingleQuad(VertexConsumer vc, MatrixStack.Entry entry,
                                       Vec3d p0, Vec3d p1, Vec3d axis, float halfWidth,
                                       float v0, float v1) {
        Vec3d offset = axis.multiply(halfWidth);
        Vec3d a0 = p0.add(offset);
        Vec3d b0 = p0.subtract(offset);
        Vec3d a1 = p1.add(offset);
        Vec3d b1 = p1.subtract(offset);
        float uSpan = 3.0f / 32.0f;
        float uCenter = 0.5f;
        float uLeft = MathHelper.clamp(uCenter - uSpan * 0.5f, 0.0f, 1.0f);
        float uRight = MathHelper.clamp(uCenter + uSpan * 0.5f, 0.0f, 1.0f);
        Vec3d normalVec = axis.normalize();
        float nx = (float) normalVec.x;
        float ny = (float) normalVec.y;
        float nz = (float) normalVec.z;

        // In the new API, submitCustom provides a MatrixStack.Entry-relative VertexConsumer
        // Use vertex(entry, x, y, z) for transformed coordinates
        vc.vertex(entry, (float) a0.x, (float) a0.y, (float) a0.z)
                .color(255, 255, 255, 255)
                .texture(uLeft, v0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(entry, nx, ny, nz);
        vc.vertex(entry, (float) b0.x, (float) b0.y, (float) b0.z)
                .color(255, 255, 255, 255)
                .texture(uRight, v0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(entry, nx, ny, nz);
        vc.vertex(entry, (float) b1.x, (float) b1.y, (float) b1.z)
                .color(255, 255, 255, 255)
                .texture(uRight, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(entry, nx, ny, nz);
        vc.vertex(entry, (float) a1.x, (float) a1.y, (float) a1.z)
                .color(255, 255, 255, 255)
                .texture(uLeft, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(entry, nx, ny, nz);
    }
}
