package com.westerosblocks.item;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModToolMaterials {
    public static final ToolMaterial VALYRIAN_STEEL = new ToolMaterial(
            TagKey.of(RegistryKeys.BLOCK, WesterosBlocks.id("needs_valyrian_steel_tool")),
            2500, 12.0F, 5.0F, 20,
            TagKey.of(RegistryKeys.ITEM, WesterosBlocks.id("valyrian_steel_repair_items"))
    );
}
