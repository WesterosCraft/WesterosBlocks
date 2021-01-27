package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.items.MultiBlockItem;
import com.westeroscraft.westerosblocks.properties.PropertyMeta;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.color.IBlockColor;
import org.dynmap.modsupport.ModTextureDefinition;

public class WCFenceGateBlock extends BlockFenceGate implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block[] buildBlockClasses(WesterosBlockDef def) {
            if (!def.validateMetaValues(null, null)) {
                return null;
            }
            new_variant = PropertyMeta.create("variant", def.getDefinedBaseMeta());

            return new Block[] { new WCFenceGateBlock(def) };
        }
    }

    // Hack to pass in property, which is needed during 'block' constructor, but isn't static for us
    protected static PropertyMeta new_variant = null;
    private WesterosBlockDef def;
    private PropertyMeta variant;

    protected WCFenceGateBlock(WesterosBlockDef def) {
        super(BlockPlanks.EnumType.OAK); // for some stupid reason fence gates require a BlockPlanks enum
        this.def = def;
        def.doStandardContructorSettings(this);
    }

    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {

    }

    @Override
    public boolean initializeBlockDefinition() {
        def.doStandardInitializeActions(this);
        return true;
    }

    @Override
    public boolean registerBlockDefinition() {
        def.doStandardRegisterActions(this, MultiBlockItem.class);
        return true;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return null;
    }

    @Override
    public IBlockColor getBlockColor() {
        return null;
    }

    @Override
    public IProperty<?>[] getNonRenderingProperties() {
        return new IProperty[0];
    }
}
