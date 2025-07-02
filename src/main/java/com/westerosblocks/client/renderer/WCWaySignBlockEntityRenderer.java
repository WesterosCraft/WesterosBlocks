package com.westerosblocks.client.renderer;

import com.westerosblocks.block.blockentity.WCWaySignBlockEntity;
import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class WCWaySignBlockEntityRenderer implements BlockEntityRenderer<WCWaySignBlockEntity> {
    
    private final TextRenderer textRenderer;
    private final BlockRenderManager blockRenderManager;
    
    public WCWaySignBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.textRenderer = context.getTextRenderer();
        this.blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
    }
    
    @Override
    public int getRenderDistance() {
        return 32;
    }
    
    @Override
    public void render(WCWaySignBlockEntity blockEntity, float tickDelta, MatrixStack matrices, 
                      VertexConsumerProvider vertexConsumers, int light, int overlay) {
        
        BlockPos pos = blockEntity.getPos();
        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        
        // Don't render if too far away
        if (pos.getSquaredDistance(cameraPos) > 1024) {
            return;
        }
        
        matrices.push();
        
        // Center the rendering
        matrices.translate(0.5, 0.5, 0.5);
        
        // Render the sign model
        renderSignModel(blockEntity, matrices, vertexConsumers, light, overlay);
        
        // Render the text
        renderSignText(blockEntity, matrices, vertexConsumers, light);
        
        matrices.pop();
    }
    
    private void renderSignModel(WCWaySignBlockEntity blockEntity, MatrixStack matrices, 
                               VertexConsumerProvider vertexConsumers, int light, int overlay) {
        
        Direction facing = blockEntity.getFacing();
        String woodType = blockEntity.getWoodType();
        
        // Get the model for this wood type
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, "block/custom/sign_post/way_sign");
        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
        
        if (model != null) {
            matrices.push();
            
            // Rotate to face the correct direction
            float yaw = facing.asRotation();
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            
            // Position the sign model
            matrices.translate(0, 0, -0.3125); // Move slightly forward
            
            // Render the model
            blockRenderManager.getModelRenderer().render(
                matrices.peek(),
                vertexConsumers.getBuffer(net.minecraft.client.render.RenderLayer.getCutout()),
                null,
                model,
                1.0f, 1.0f, 1.0f,
                light, overlay
            );
            
            matrices.pop();
        }
    }
    
    private void renderSignText(WCWaySignBlockEntity blockEntity, MatrixStack matrices, 
                              VertexConsumerProvider vertexConsumers, int light) {
        
        List<Text> text = blockEntity.getText();
        if (text.isEmpty()) {
            return;
        }
        
        Direction facing = blockEntity.getFacing();
        
        matrices.push();
        
        // Rotate to face the camera
        float yaw = facing.asRotation();
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        
        // Position the text on the sign
        matrices.translate(0, 0.28125, -0.3125 + 0.005);
        matrices.scale(0.010416667f, -0.010416667f, 0.010416667f);
        
        // Render each line of text
        for (int i = 0; i < text.size() && i < 4; i++) {
            Text line = text.get(i);
            if (line != null && !line.getString().isEmpty()) {
                float x = -textRenderer.getWidth(line) / 2.0f;
                float y = -4 + i * 10;
                
                textRenderer.draw(
                    line,
                    x, y,
                    0xFFFFFFFF,
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumers,
                    TextRenderer.TextLayerType.SEE_THROUGH,
                    0,
                    light
                );
            }
        }
        
        matrices.pop();
    }
} 