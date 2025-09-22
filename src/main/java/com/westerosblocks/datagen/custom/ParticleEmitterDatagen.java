package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.datagen.ModModels;

/**
 * Particle emitter block exporter for WCParticleEmitterBlock.
 * Handles powered/unpowered states with off/on textures.
 */
public class ParticleEmitterDatagen extends BaseBlockExporter {

    /**
     * Registers a particle emitter block with off and on textures.
     *
     * @param generator The block state model generator
     * @param block The particle emitter block
     * @param offTexture Texture for powered=false state
     * @param onTexture Texture for powered=true state
     */
    public static void registerCustomParticleEmitterBlock(BlockStateModelGenerator generator, Block block, String offTexture, String onTexture) {
        Identifier offModelId = createNestedModelId(block, "off");
        Identifier onModelId = createNestedModelId(block, "on");

        // Create off state model with custom parent
        Identifier offTextureId = WesterosBlocks.id("block/" + offTexture);
        TextureMap offTextureMap = new TextureMap()
            .put(TextureKey.TEXTURE, offTextureId)
            .put(TextureKey.PARTICLE, offTextureId);
        ModModels.PARTICLE_EMITTER_OFF.upload(offModelId, offTextureMap, generator.modelCollector);

        // Create on state model with custom parent
        Identifier onTextureId = WesterosBlocks.id("block/" + onTexture);
        TextureMap onTextureMap = new TextureMap()
            .put(TextureKey.TEXTURE, onTextureId)
            .put(TextureKey.PARTICLE, onTextureId);
        ModModels.PARTICLE_EMITTER_ON.upload(onModelId, onTextureMap, generator.modelCollector);

        // Create blockstate with powered variants
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
            .coordinate(BlockStateVariantMap.create(Properties.POWERED)
                .register(false, BlockStateVariant.create().put(VariantSettings.MODEL, offModelId))
                .register(true, BlockStateVariant.create().put(VariantSettings.MODEL, onModelId))));

        // Generate item model using off texture
        generator.registerParentedItemModel(block, offModelId);
    }

    public static void registerCustomParticleEmitterBlock(BlockStateModelGenerator generator, Block block, String texture) {
        registerCustomParticleEmitterBlock(generator, block, texture, texture);
    }
}
