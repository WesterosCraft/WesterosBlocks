package com.westerosblocks.entity.client;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.entity.custom.RopeEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RopeRenderer extends EntityRenderer<RopeEntity> {

    private static final Identifier ROPE_TEXTURE = Identifier.of(WesterosBlocks.MOD_ID, "textures/entity/straight_rope.png");
    private static final Identifier CHAIN_TEXTURE = Identifier.of(WesterosBlocks.MOD_ID, "textures/entity/chain.png");
    private static final float HALF_WIDTH = 0.06F;
    private static final int SEGMENTS = 40;
    private static final float TEXTURE_REPEAT_PER_BLOCK = 1.0F;

    public RopeRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(RopeEntity rope, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        Vec3d startWorld = rope.getStartWorldPoint();
        Vec3d endWorld = rope.getEndWorldPoint();
        Vec3d entityPos = rope.getPos();
        Vec3d startLocal = startWorld.subtract(entityPos);
        Vec3d endLocal = endWorld.subtract(entityPos);
        Vec3d delta = endLocal.subtract(startLocal);
        double length = delta.length();
        double ax = Math.abs(delta.x);
        double ay = Math.abs(delta.y);
        double az = Math.abs(delta.z);
        float dirUvOffset = (ax + az) > ay ? 1.00F : 0.0F;
        if (length < 1e-6) {
            return;
        }
        Vec3d direction = delta.normalize();
        Vec3d arbitraryUp = Math.abs(direction.y) > 0.95
                ? new Vec3d(1, 0, 0)
                : new Vec3d(0, 1, 0);

        Vec3d right = direction.crossProduct(arbitraryUp).normalize();
        Vec3d up = right.crossProduct(direction).normalize();

        float sagAmount = switch (rope.getTensionLevel()) {
            case 0 -> 5.50f;
            case 1 -> 4.00f;
            case 2 -> 3.00f;
            case 3 -> 2.00f;
            case 4 -> 1.00f;
            case 5 -> 0.50f;
            default -> 0.0f;
        };
        Identifier texture = getTexture(rope);
        RenderLayer layer = RenderLayer.getEntityCutoutNoCull(texture);
        VertexConsumer buffer = vertexConsumers.getBuffer(layer);

        matrices.push();
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f modelMatrix = entry.getPositionMatrix();

        Vec3d prevPoint = getRopePoint(startLocal, endLocal, 0.0f, sagAmount);
        float vCoord = dirUvOffset;

        for (int i = 1; i <= SEGMENTS; i++) {
            float t = (float) i / SEGMENTS;
            Vec3d currPoint = getRopePoint(startLocal, endLocal, t, sagAmount);
            float segmentLength = (float) currPoint.subtract(prevPoint).length();
            float vNext = vCoord + segmentLength * TEXTURE_REPEAT_PER_BLOCK;
            drawTiledRibbon(buffer, modelMatrix, entry, prevPoint, currPoint, right, HALF_WIDTH, vCoord, vNext, light);

            float crossPlaneOffset = rope.getVariant() == 1 ? 0.50F : 0.0F;
            drawTiledRibbon(buffer, modelMatrix, entry, prevPoint, currPoint, up, HALF_WIDTH, vCoord + crossPlaneOffset, vNext + crossPlaneOffset, light);

            prevPoint = currPoint;
            vCoord = vNext;
        }
        if (isLookingAtThisRope(rope)) {
            VertexConsumer lineVc = vertexConsumers.getBuffer(RenderLayer.getLines());
            Box localBox = rope.getBoundingBox().offset(-entityPos.x, -entityPos.y, -entityPos.z);
            WorldRenderer.drawBox(matrices, lineVc, localBox, 1f, 1f, 1f, 1f);
        }
        matrices.pop();
        super.render(rope, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static Vec3d getRopePoint(Vec3d start, Vec3d end, float t, float sagStrength) {
        double x = MathHelper.lerp(t, start.x, end.x);
        double y = MathHelper.lerp(t, start.y, end.y);
        double z = MathHelper.lerp(t, start.z, end.z);
        y -= Math.sin(t * Math.PI) * sagStrength;
        return new Vec3d(x, y, z);
    }

    private static void drawTiledRibbon(VertexConsumer buffer, Matrix4f mat, MatrixStack.Entry entry,
                                        Vec3d p0, Vec3d p1, Vec3d normalAxis, float halfWidth,
                                        float v0, float v1, int light) {
        int tile0 = (int) Math.floor(v0);
        int tile1 = (int) Math.floor(v1);
        float frac0 = MathHelper.fractionalPart(v0);
        float frac1 = MathHelper.fractionalPart(v1);

        if (tile0 == tile1) {
            drawSingleQuad(buffer, mat, entry, p0, p1, normalAxis, halfWidth, frac0, frac1, light);
        } else {
            float boundaryV = tile0 + 1.0f;
            float alpha = (boundaryV - v0) / (v1 - v0);
            alpha = MathHelper.clamp(alpha, 0.0f, 1.0f);
            Vec3d mid = p0.add(p1.subtract(p0).multiply(alpha));
            drawSingleQuad(buffer, mat, entry, p0, mid, normalAxis, halfWidth, frac0, 1.0f, light);
            drawTiledRibbon(buffer, mat, entry, mid, p1, normalAxis, halfWidth, boundaryV, v1, light);
        }
    }

    private static void drawSingleQuad(VertexConsumer vc, Matrix4f mat, MatrixStack.Entry entry,
                                       Vec3d p0, Vec3d p1, Vec3d axis, float halfWidth,
                                       float v0, float v1, int light) {
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

        vc.vertex(mat, (float) a0.x, (float) a0.y, (float) a0.z)
                .color(255, 255, 255, 255)
                .texture(uLeft, v0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(nx, ny, nz);
        vc.vertex(mat, (float) b0.x, (float) b0.y, (float) b0.z)
                .color(255, 255, 255, 255)
                .texture(uRight, v0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(nx, ny, nz);
        vc.vertex(mat, (float) b1.x, (float) b1.y, (float) b1.z)
                .color(255, 255, 255, 255)
                .texture(uRight, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(nx, ny, nz);
        vc.vertex(mat, (float) a1.x, (float) a1.y, (float) a1.z)
                .color(255, 255, 255, 255)
                .texture(uLeft, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(nx, ny, nz);
    }

    @Override
    public Identifier getTexture(RopeEntity entity) {
        return entity.getVariant() == 1 ? CHAIN_TEXTURE : ROPE_TEXTURE;
    }

    private void renderCenterDebugMarker(RopeEntity entity,
                                         MatrixStack matrices,
                                         VertexConsumerProvider vertexConsumers,
                                         Vec3d startWorld,
                                         Vec3d endWorld) {

        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hr = client.crosshairTarget;

        if (!(hr instanceof EntityHitResult ehr)) return;
        if (ehr.getEntity() != entity) return;
        Vec3d mid = startWorld.add(endWorld).multiply(0.5);
        double r = 0.10;
        Vec3d cam = client.gameRenderer.getCamera().getPos();
        double x = mid.x - cam.x;
        double y = mid.y - cam.y;
        double z = mid.z - cam.z;

        Box box = new Box(x - r, y - r, z - r, x + r, y + r, z + r);
        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getLines());
        WorldRenderer.drawBox(matrices, vc, box, 1f, 1f, 1f, 1f);
    }

    private boolean isLookingAtThisRope(RopeEntity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hr = client.crosshairTarget;
        if (!(hr instanceof EntityHitResult ehr)) return false;
        return ehr.getEntity() == entity;
    }

    private Vec3d toRenderSpace(Vec3d world) {
        Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        return world.subtract(cam);
    }
}
