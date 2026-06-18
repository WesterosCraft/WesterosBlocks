package com.westerosblocks.client;

import com.westerosblocks.block.custom.WCParticleEmitterBlock;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws a wireframe finder box around every nearby {@link WCParticleEmitterBlock} while a
 * creative-mode player holds any particle-emitter block item. Particle emitters render with a
 * fully transparent texture, so this is the only way to locate them once placed.
 *
 * <p>Mirrors the rope-tool finder in {@code RopeRenderer.shouldHighlightAllRopes()}, but because
 * emitters are blocks (not entities) there is no per-block render hook: we listen to
 * {@link WorldRenderEvents#AFTER_ENTITIES} and scan nearby blocks (cached, not per-frame).
 * The boxes draw through walls (custom always-pass depth layer) so buried emitters are findable.
 */
public final class ParticleEmitterHighlighter {

    private static final int RADIUS = 16;
    private static final int RESCAN_INTERVAL_TICKS = 20;
    // Orange, distinct from the rope tool's cyan finder box.
    private static final float R = 1.0f, G = 0.55f, B = 0.0f, A = 1.0f;

    private final List<BlockPos> cached = new ArrayList<>();
    private BlockPos lastOrigin;
    private long lastScanTime = Long.MIN_VALUE;

    private ParticleEmitterHighlighter() {
    }

    public static void register() {
        ParticleEmitterHighlighter handler = new ParticleEmitterHighlighter();
        WorldRenderEvents.AFTER_ENTITIES.register(handler::onAfterEntities);
    }

    private void onAfterEntities(WorldRenderContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!shouldHighlight(client)) {
            cached.clear();
            lastOrigin = null;
            return;
        }

        ClientWorld world = ctx.world();
        if (world == null || client.player == null) {
            return;
        }

        BlockPos origin = client.player.getBlockPos();
        long now = world.getTime();
        if (!origin.equals(lastOrigin) || now - lastScanTime >= RESCAN_INTERVAL_TICKS) {
            rescan(world, origin);
            lastOrigin = origin;
            lastScanTime = now;
        }
        if (cached.isEmpty()) {
            return;
        }

        MatrixStack matrices = ctx.matrixStack();
        VertexConsumerProvider consumers = ctx.consumers();
        if (matrices == null || consumers == null) {
            return;
        }

        Vec3d cam = ctx.camera().getPos();
        VertexConsumer lines = consumers.getBuffer(EmitterRenderLayers.THROUGH_WALLS_LINES);
        for (BlockPos pos : cached) {
            double x = pos.getX() - cam.x;
            double y = pos.getY() - cam.y;
            double z = pos.getZ() - cam.z;
            // Inset slightly so adjacent emitter boxes don't z-fight.
            Box box = new Box(x + 0.05, y + 0.05, z + 0.05, x + 0.95, y + 0.95, z + 0.95);
            WorldRenderer.drawBox(matrices, lines, box, R, G, B, A);
        }

        // Flush our custom layer now so it is drawn within this phase.
        if (consumers instanceof VertexConsumerProvider.Immediate immediate) {
            immediate.draw(EmitterRenderLayers.THROUGH_WALLS_LINES);
        }
    }

    private void rescan(ClientWorld world, BlockPos origin) {
        cached.clear();
        int minY = Math.max(world.getBottomY(), origin.getY() - RADIUS);
        int maxY = Math.min(world.getTopY() - 1, origin.getY() + RADIUS);
        for (BlockPos pos : BlockPos.iterate(
                origin.getX() - RADIUS, minY, origin.getZ() - RADIUS,
                origin.getX() + RADIUS, maxY, origin.getZ() + RADIUS)) {
            // Don't force chunk loads / read outside loaded area.
            if (!world.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4)) {
                continue;
            }
            if (world.getBlockState(pos).getBlock() instanceof WCParticleEmitterBlock) {
                cached.add(pos.toImmutable()); // BlockPos.iterate reuses a mutable cursor
            }
        }
    }

    private boolean shouldHighlight(MinecraftClient client) {
        if (client.player == null || !client.player.isCreative()) {
            return false;
        }
        return isEmitterItem(client.player.getMainHandStack())
                || isEmitterItem(client.player.getOffHandStack());
    }

    private boolean isEmitterItem(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof WCParticleEmitterBlock;
    }

    /**
     * Holder for the custom render layer. Extends {@link RenderLayer} purely so the layer
     * definition can reference the {@code protected static} {@code RenderPhase} constants
     * (inherited). The class itself is never instantiated. {@code RenderLayer.of(...)} is
     * package-private and reached via the bundled access widener.
     */
    private static final class EmitterRenderLayers extends RenderLayer {
        // Same recipe as vanilla RenderLayer.LINES, but with an always-pass depth test so the
        // boxes render on top of terrain (x-ray finder) instead of the default LEQUAL test.
        static final RenderLayer THROUGH_WALLS_LINES = RenderLayer.of(
                "westeros_emitter_through_wall_lines",
                VertexFormats.LINES,
                VertexFormat.DrawMode.LINES,
                1536,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(LINES_PROGRAM)
                        .lineWidth(FULL_LINE_WIDTH)
                        .layering(VIEW_OFFSET_Z_LAYERING)
                        .transparency(TRANSLUCENT_TRANSPARENCY)
                        .target(ITEM_ENTITY_TARGET)
                        .writeMaskState(ALL_MASK)
                        .cull(DISABLE_CULLING)
                        .depthTest(ALWAYS_DEPTH_TEST)
                        .build(false));

        private EmitterRenderLayers() {
            // Never called; RenderLayer has no no-arg constructor, so one must be provided.
            super("", null, null, 0, false, false, () -> {
            }, () -> {
            });
        }
    }
}
