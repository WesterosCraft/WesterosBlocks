package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.ToIntFunction;
import java.util.HashMap;
import java.util.Map;

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
    
    public BlockBuilder<T> noCollision() {
        this.settings = this.settings.noCollision();
        return this;
    }
    
    public BlockBuilder<T> locked(boolean locked) {
        parameters.put("locked", locked);
        return this;
    }
    
    public BlockBuilder<T> allowUnsupported(boolean allowUnsupported) {
        parameters.put("allowUnsupported", allowUnsupported);
        return this;
    }
    
    public BlockBuilder<T> woodType(String woodType) {
        parameters.put("woodType", woodType);
        return this;
    }
    
    public BlockBuilder<T> hasRecipe(boolean hasRecipe) {
        parameters.put("hasRecipe", hasRecipe);
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
    
    public BlockBuilder<T> states(int numStates) {
        parameters.put("states", numStates);
        return this;
    }
    
    public BlockBuilder<T> symmetrical(boolean symmetrical) {
        parameters.put("symmetrical", symmetrical);
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