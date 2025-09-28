package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.ToIntFunction;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import net.minecraft.block.BlockState;

public class BlockBuilder<T extends Block> {
    private AbstractBlock.Settings settings;
    private Map<String, Object> parameters = new HashMap<>();
    private BlockFactory factory;

    public BlockBuilder() {
        this.settings = AbstractBlock.Settings.create();
    }
    
    private BlockBuilder(BlockFactory factory) {
        this.settings = AbstractBlock.Settings.create();
        this.factory = factory;
    }
    
    // Static factory methods for each block type
    public static BlockBuilder<WCHalfDoorBlock> halfDoor() {
        return new BlockBuilder<>(new WCHalfDoorBlock.Factory());
    }
    
    public static BlockBuilder<WCDoorBlock> door() {
        return new BlockBuilder<>(new WCDoorBlock.Factory());
    }
    
    public static BlockBuilder<WCPaneBlock> pane() {
        return new BlockBuilder<>(new WCPaneBlock.Factory());
    }
    
    public static BlockBuilder<WCSolidBlock> solid() {
        return new BlockBuilder<>(new WCSolidBlock.Factory());
    }
    
    public static BlockBuilder<WCSlabBlock> slab() {
        return new BlockBuilder<>(new WCSlabBlock.Factory());
    }
    
    public static BlockBuilder<WCLogBlock> log() {
        return new BlockBuilder<>(new WCLogBlock.Factory());
    }
    
    public static BlockBuilder<WCTableBlock> table() {
        return new BlockBuilder<>(new WCTableBlock.Factory());
    }
    
    public static BlockBuilder<WCBranchBlock> branch() {
        return new BlockBuilder<>(new WCBranchBlock.Factory());
    }

    public static BlockBuilder<WCChairBlock> chair() {
        return new BlockBuilder<>(new WCChairBlock.Factory());
    }
    
    public static BlockBuilder<WCTorchBlock> torch() {
        return new BlockBuilder<>(new WCTorchBlock.Factory());
    }
    
    public static BlockBuilder<WCWallTorchBlock> wallTorch() {
        return new BlockBuilder<>(new WCWallTorchBlock.Factory());
    }
    
    public static BlockBuilder<WCArrowSlitBlock> arrowSlit() {
        return new BlockBuilder<>(new WCArrowSlitBlock.Factory());
    }
    
    public static BlockBuilder<WCRailBlock> rail() {
        return new BlockBuilder<>(new WCRailBlock.Factory());
    }
    
    public static BlockBuilder<WCFanBlock> fan() {
        return new BlockBuilder<>(new WCFanBlock.Factory());
    }
    
    public static BlockBuilder<WCWallFanBlock> wallFan() {
        return new BlockBuilder<>(new WCWallFanBlock.Factory());
    }
    
    public static BlockBuilder<WCFenceBlock> fence() {
        return new BlockBuilder<>(new WCFenceBlock.Factory());
    }

    public static BlockBuilder<WCFenceGateBlock> fenceGate() {
        return new BlockBuilder<>(new WCFenceGateBlock.Factory());
    }
    
    public static BlockBuilder<WCLayerBlock> layer() {
        return new BlockBuilder<>(new WCLayerBlock.Factory());
    }
    
    public static BlockBuilder<WCPlantBlock> plant() {
        return new BlockBuilder<>(new WCPlantBlock.Factory());
    }
    
    public static BlockBuilder<WCCropBlock> crop() {
        return new BlockBuilder<>(new WCCropBlock.Factory());
    }
    
    public static BlockBuilder<WCWebBlock> web() {
        return new BlockBuilder<>(new WCWebBlock.Factory());
    }

    public static BlockBuilder<WCWebBlock> bed() {
        return new BlockBuilder<>(new WCBedBlock.Factory());
    }
    
    public static BlockBuilder<WCFlowerbedBlock> flowerbed() {
        return new BlockBuilder<>(new WCFlowerbedBlock.Factory());
    }
    
    public static BlockBuilder<WCLeavesBlock> leaves() {
        return new BlockBuilder<>(new WCLeavesBlock.Factory());
    }
    
    public static BlockBuilder<WCVinesBlock> vines() {
        return new BlockBuilder<>(new WCVinesBlock.Factory());
    }

    public static BlockBuilder<WCLadderBlock> ladder() {
        return new BlockBuilder<>(new WCLadderBlock.Factory());
    }

    public static BlockBuilder<WCFlowerPotBlock> flowerPot() {
        return new BlockBuilder<>(new WCFlowerPotBlock.Factory());
    }

    public static BlockBuilder<WCParticleEmitterBlock> particleEmitter() {
        return new BlockBuilder<>(new WCParticleEmitterBlock.Factory());
    }

    public static BlockBuilder<WCFireBlock> fire() {
        return new BlockBuilder<>(new WCFireBlock.Factory());
    }

    public static BlockBuilder<WCFurnaceBlock> furnace() {
        return new BlockBuilder<>(new WCFurnaceBlock.Factory());
    }
    
    public BlockBuilder<T> settings(AbstractBlock.Settings settings) {
        this.settings = settings;
        return this;
    }
    
    public BlockBuilder<T> strength(float strength) {
        this.settings = this.settings.strength(strength);
        return this;
    }
    
    public BlockBuilder<T> resistance(float resistance) {
        this.settings = this.settings.resistance(resistance);
        return this;
    }
    
    public BlockBuilder<T> requiresTool() {
        this.settings = this.settings.requiresTool();
        return this;
    }
    
    public BlockBuilder<T> sounds(BlockSoundGroup soundGroup) {
        this.settings = this.settings.sounds(soundGroup);
        return this;
    }
    
    public BlockBuilder<T> luminance(ToIntFunction<BlockState> luminanceFunction) {
        this.settings = this.settings.luminance(luminanceFunction);
        return this;
    }
    
    public BlockBuilder<T> nonOpaque() {
        this.settings = this.settings.nonOpaque();
        return this;
    }

    public BlockBuilder<T> nonOpaque(Boolean condition) {
        if (Boolean.TRUE.equals(condition)) {
            this.settings = this.settings.nonOpaque();
        }
        return this;
    }

    public BlockBuilder<T> noCollision() {
        this.settings = this.settings.noCollision();
        return this;
    }

    public BlockBuilder<T> noCollision(Boolean condition) {
        if (Boolean.TRUE.equals(condition)) {
            this.settings = this.settings.noCollision();
        }
        return this;
    }

    public BlockBuilder<T> breakInstantly() {
        this.settings = this.settings.breakInstantly();
        return this;
    }
    
    public BlockBuilder<T> locked(boolean locked) {
        parameters.put("locked", locked);
        return this;
    }

    public BlockBuilder<T> locked() {
        parameters.put("locked", true);
        return this;
    }
    
    public BlockBuilder<T> allowUnsupported(boolean allowUnsupported) {
        parameters.put("allowUnsupported", allowUnsupported);
        return this;
    }
    
    public BlockBuilder<T> allowUnsupported() {
        parameters.put("allowUnsupported", true);
        return this;
    }
    
    public BlockBuilder<T> woodType(String woodType) {
        parameters.put("woodType", woodType);
        return this;
    }
    
    public BlockBuilder<T> unconnect(boolean unconnect) {
        parameters.put("unconnect", unconnect);
        return this;
    }
    
    public BlockBuilder<T> legacyModel(boolean legacyModel) {
        parameters.put("legacyModel", legacyModel);
        return this;
    }

    // For the pane block, to switch between models
    public BlockBuilder<T> barsModel(boolean barsModel) {
        parameters.put("barsModel", barsModel);
        return this;
    }
    
    public BlockBuilder<T> connectState(boolean connectState) {
        parameters.put("connectState", connectState);
        return this;
    }
    
    public BlockBuilder<T> toggleOnUse(boolean toggleOnUse) {
        parameters.put("toggleOnUse", toggleOnUse);
        return this;
    }
    
    public BlockBuilder<T> toggleOnUse() {
        parameters.put("toggleOnUse", true);
        return this;
    }
    
    public BlockBuilder<T> states(int numStates) {
        parameters.put("states", numStates);
        return this;
    }
    
    public BlockBuilder<T> symmetrical(boolean symmetrical) {
        parameters.put("symmetrical", symmetrical);
        return this;
    }
    
    public BlockBuilder<T> wallBlock(Block wallBlock) {
        parameters.put("wallBlock", wallBlock);
        return this;
    }
    
    public BlockBuilder<T> noParticle(boolean noParticle) {
        parameters.put("noParticle", noParticle);
        return this;
    }
    
    public BlockBuilder<T> layerSensitive(boolean layerSensitive) {
        parameters.put("layerSensitive", layerSensitive);
        return this;
    }
    
    public BlockBuilder<T> layerSensitive() {
        parameters.put("layerSensitive", true);
        return this;
    }
    
    public BlockBuilder<T> noInWeb(boolean noInWeb) {
        parameters.put("noInWeb", noInWeb);
        return this;
    }
    
    public BlockBuilder<T> noInWeb() {
        parameters.put("noInWeb", true);
        return this;
    }
    
    public BlockBuilder<T> stateValues(List<String> stateValues) {
        parameters.put("stateValues", stateValues);
        return this;
    }
    
    public BlockBuilder<T> betterFoliage(boolean betterFoliage) {
        parameters.put("betterFoliage", betterFoliage);
        return this;
    }
    
    public BlockBuilder<T> betterFoliage() {
        parameters.put("betterFoliage", true);
        return this;
    }
    
    public BlockBuilder<T> overlay(boolean overlay) {
        parameters.put("overlay", overlay);
        return this;
    }
    
    public BlockBuilder<T> overlay() {
        parameters.put("overlay", true);
        return this;
    }
    
    public BlockBuilder<T> noDecay(boolean noDecay) {
        parameters.put("noDecay", noDecay);
        return this;
    }
    
    public BlockBuilder<T> noDecay() {
        parameters.put("noDecay", true);
        return this;
    }
    
    public BlockBuilder<T> noClimb() {
        parameters.put("noClimb", true);
        return this;
    }
    
    public BlockBuilder<T> canGrowDownward() {
        parameters.put("canGrowDownward", true);
        return this;
    }

    public BlockBuilder<T> plant(Block plant) {
        parameters.put("plant", plant);
        return this;
    }

    public BlockBuilder<T> plantId(String plantId) {
        parameters.put("plantId", plantId);
        return this;
    }

    public BlockBuilder<T> particle(String particle) {
        parameters.put("particle", particle);
        return this;
    }

    public BlockBuilder<T> alwaysOn(boolean alwaysOn) {
        parameters.put("alwaysOn", alwaysOn);
        return this;
    }

    public BlockBuilder<T> alwaysOn() {
        parameters.put("alwaysOn", true);
        return this;
    }

    public BlockBuilder<T> parameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public T build() {
        if (settings == null) {
            throw new IllegalStateException("Settings must be provided");
        }
        if (factory == null) {
            throw new IllegalStateException("Factory must be provided - use static factory methods like BlockBuilder.halfDoor()");
        }
        return (T) factory.buildBlockClass(settings, parameters);
    }
    
    // Legacy method for backward compatibility
    @SuppressWarnings("unchecked")
    public T build(BlockFactory factory) {
        if (settings == null) {
            throw new IllegalStateException("Settings must be provided");
        }
        return (T) factory.buildBlockClass(settings, parameters);
    }
}